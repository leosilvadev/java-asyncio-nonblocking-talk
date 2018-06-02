package com.github.leosilvadev.core.http;

import java.util.Optional;

/**
 * Created by leonardo on 5/31/18.
 */
public enum HTTPMethod {
  GET,
  POST,
  PUT,
  DELETE,
  PATCH,
  OPTIONS,
  HEAD,
  CONNECT,
  TRACE;


  public static Optional<HTTPMethod> from(final String method) {
    try {
      return Optional.of(HTTPMethod.valueOf(method));

    } catch (final IllegalArgumentException ex) {
      return Optional.empty();

    }
  }
}
