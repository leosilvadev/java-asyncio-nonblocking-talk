package com.github.leosilvadev.core.request;

import com.github.leosilvadev.core.http.Headers;
import com.github.leosilvadev.core.http.HTTPMethod;

import java.util.Optional;

/**
 * Created by leonardo on 5/31/18.
 */
public class Request {

  private final RequestDefinition definition;
  private final String body;

  private final Headers headers;

  Request(final RequestDefinition definition,
                 final String body,
                 final Headers headers) {
    this.definition = definition;
    this.body = body;
    this.headers = headers;
  }

  public HTTPMethod getMethod() {
    return definition.getMethod();
  }

  public String getPath() {
    return definition.getPath();
  }

  public Headers getHeaders() {
    return headers;
  }

  public String getBody() {
    return Optional.ofNullable(body).orElse("");
  }

}