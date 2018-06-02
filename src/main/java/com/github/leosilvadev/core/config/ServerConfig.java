package com.github.leosilvadev.core.config;

/**
 * Created by leonardo on 6/2/18.
 */
public class ServerConfig {

  private String host;
  private int port;

  public ServerConfig() {}

  public ServerConfig(final String host, final int port) {
    this.host = host;
    this.port = port;
  }

  public String getHost() {
    return host;
  }

  public void setHost(final String host) {
    this.host = host;
  }

  public int getPort() {
    return port;
  }

  public void setPort(final int port) {
    this.port = port;
  }

  @Override
  public String toString() {
    return "ServerConfig{" +
        "host='" + host + '\'' +
        ", port=" + port +
        '}';
  }
}
