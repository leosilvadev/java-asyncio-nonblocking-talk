package com.github.leosilvadev.core.config;

import java.net.URI;

/**
 * Created by leonardo on 6/2/18.
 */
public class ConfigLocaltion {

  private final URI path;

  public ConfigLocaltion(final URI path) {
    this.path = path;
  }

  public URI getPath() {
    return path;
  }
}
