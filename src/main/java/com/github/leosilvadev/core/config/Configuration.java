package com.github.leosilvadev.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by leonardo on 6/2/18.
 */
public class Configuration {

  private static final Logger logger = LoggerFactory.getLogger(Configuration.class);

  private ServerConfig server;
  private BlockingConfig blocking;

  public Configuration() {}

  public Configuration(final ServerConfig server, final BlockingConfig blocking) {
    this.server = server;
    this.blocking = blocking;
  }

  public ServerConfig getServer() {
    return server;
  }

  public void setServer(final ServerConfig server) {
    this.server = server;
  }

  public BlockingConfig getBlocking() {
    return blocking;
  }

  public void setBlocking(final BlockingConfig blocking) {
    this.blocking = blocking;
  }

  @Override
  public String toString() {
    return "Configuration{" +
        "server=" + server +
        ", blocking=" + blocking +
        '}';
  }

  public static Configuration load() {
    try {
      return load(ClassLoader.getSystemResource("application.yml").toURI());

    } catch (final URISyntaxException e) {
      final Configuration configuration= getDefault();
      logger.warn("Configuration file 'application.yml' not found, using default configuration. {}", configuration);
      return configuration;

    }
  }

  public static Configuration load(final String uri) {
    try {
      return load(ClassLoader.getSystemResource(uri).toURI());

    } catch (final URISyntaxException e) {
      final Configuration configuration= getDefault();
      logger.warn("Configuration file 'application.yml' not found, using default configuration. {}", configuration);
      return configuration;

    }
  }

  public static Configuration load(final URI uri) {
    Configuration configuration;
    final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    try {
      final String content = new String(Files.readAllBytes(Paths.get(uri)), "UTF-8");
      configuration = mapper.readValue(content, Configuration.class);
      logger.info("Using configuration {}", configuration);

    } catch (final IOException e) {
      configuration = getDefault();
      logger.warn("Configuration file 'application.yml' not found, using default configuration. {}", configuration);

    }
    return configuration;
  }

  private static Configuration getDefault() {
    return new Configuration(new ServerConfig("0.0.0.0", 8080), new BlockingConfig(50));
  }

}
