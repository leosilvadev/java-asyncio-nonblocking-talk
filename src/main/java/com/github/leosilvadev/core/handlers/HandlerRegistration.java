package com.github.leosilvadev.core.handlers;

import com.github.leosilvadev.core.http.HTTPMethod;

/**
 * Created by leonardo on 6/1/18.
 */
public final class HandlerRegistration {

  private final HTTPMethod method;
  private final String path;
  private final Handler handler;

  public HandlerRegistration(final HTTPMethod method, final String path, final Handler handler) {
    this.method = method;
    this.path = path;
    this.handler = handler;
  }

  public HTTPMethod getMethod() {
    return method;
  }

  public String getPath() {
    return path;
  }

  public Handler getHandler() {
    return handler;
  }
}
