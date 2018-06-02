package com.github.leosilvadev.core.config.exceptions;

/**
 * Created by leonardo on 6/2/18.
 */
public class MissingConfigurationException extends RuntimeException {

  private final String path;

  public MissingConfigurationException(final String path) {
    this.path = path;
  }

  public String getMessage() {
    return String.format("Config file could not be found at %s", path);
  }

}
