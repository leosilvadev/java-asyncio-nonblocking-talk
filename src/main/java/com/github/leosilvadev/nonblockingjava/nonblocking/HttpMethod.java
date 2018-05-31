package com.github.leosilvadev.nonblockingjava.nonblocking;

import java.util.Optional;

/**
 * Created by leonardo on 5/31/18.
 */
public enum HttpMethod {
  GET,
  POST,
  PUT,
  DELETE,
  PATCH,
  OPTIONS,
  HEAD,
  CONNECT,
  TRACE;


  public static Optional<HttpMethod> from(final String method) {
    try {
      return Optional.of(HttpMethod.valueOf(method));

    } catch (final IllegalArgumentException ex) {
      return Optional.empty();

    }
  }
}
