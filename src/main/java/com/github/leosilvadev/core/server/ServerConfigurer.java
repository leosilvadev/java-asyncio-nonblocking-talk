package com.github.leosilvadev.core.server;

import com.github.leosilvadev.core.Core;
import com.github.leosilvadev.core.config.Configuration;
import com.github.leosilvadev.core.handlers.Handler;
import com.github.leosilvadev.core.handlers.HandlerRegistration;
import com.github.leosilvadev.core.http.HTTPMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leonardo on 6/1/18.
 */
final class ServerConfigurer {

  private final Core core;
  private Configuration configuration;

  private List<HandlerRegistration> handlerRegistrations;

  public ServerConfigurer(final Core core, final Configuration configuration) {
    this.configuration = configuration;
    this.core = core;
    this.handlerRegistrations = new ArrayList<>();
  }

  public ServerConfigurer handleGet(final String path, final Handler handler) {
    return handle(HTTPMethod.GET, path, handler);
  }

  public ServerConfigurer handlePost(final String path, final Handler handler) {
    return handle(HTTPMethod.POST, path, handler);
  }

  public ServerConfigurer handlePut(final String path, final Handler handler) {
    return handle(HTTPMethod.PUT, path, handler);
  }

  public ServerConfigurer handleDelete(final String path, final Handler handler) {
    return handle(HTTPMethod.DELETE, path, handler);
  }

  public ServerConfigurer handlePatch(final String path, final Handler handler) {
    return handle(HTTPMethod.PATCH, path, handler);
  }

  public ServerConfigurer handle(final HTTPMethod method, final String path, final Handler handler) {
    this.handlerRegistrations.add(new HandlerRegistration(method, path, handler));
    return this;
  }

  public Server build() {
    return new Server(core, configuration, handlerRegistrations);
  }

}
