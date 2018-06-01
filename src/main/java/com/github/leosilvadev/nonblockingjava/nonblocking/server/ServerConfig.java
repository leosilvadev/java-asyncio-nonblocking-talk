package com.github.leosilvadev.nonblockingjava.nonblocking.server;

import com.github.leosilvadev.nonblockingjava.nonblocking.handlers.Handler;
import com.github.leosilvadev.nonblockingjava.nonblocking.handlers.HandlerRegistration;
import com.github.leosilvadev.nonblockingjava.nonblocking.http.HTTPMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leonardo on 6/1/18.
 */
final class ServerConfig {

  private String host;
  private Integer port;

  private List<HandlerRegistration> handlerRegistrations;

  public ServerConfig() {
    this.host = "0.0.0.0";
    this.port = 8080;
    this.handlerRegistrations = new ArrayList<>();
  }

  public ServerConfig withHost(final String host) {
    this.host = host;
    return this;
  }

  public ServerConfig withPort(final int port) {
    this.port = port;
    return this;
  }

  public ServerConfig handleGet(final String path, final Handler handler) {
    return handle(HTTPMethod.GET, path, handler);
  }

  public ServerConfig handlePost(final String path, final Handler handler) {
    return handle(HTTPMethod.POST, path, handler);
  }

  public ServerConfig handlePut(final String path, final Handler handler) {
    return handle(HTTPMethod.PUT, path, handler);
  }

  public ServerConfig handleDelete(final String path, final Handler handler) {
    return handle(HTTPMethod.DELETE, path, handler);
  }

  public ServerConfig handlePatch(final String path, final Handler handler) {
    return handle(HTTPMethod.PATCH, path, handler);
  }

  public ServerConfig handle(final HTTPMethod method, final String path, final Handler handler) {
    this.handlerRegistrations.add(new HandlerRegistration(method, path, handler));
    return this;
  }

  public Server build() {
    return new Server(host, port, handlerRegistrations);
  }

}
