package com.github.leosilvadev.nonblockingjava.nonblocking.server;

import com.github.leosilvadev.nonblockingjava.nonblocking.handlers.Handler;
import com.github.leosilvadev.nonblockingjava.nonblocking.handlers.HandlerRegistration;
import com.github.leosilvadev.nonblockingjava.nonblocking.http.HTTPMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leonardo on 6/1/18.
 */
public final class ServerConfig {

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

  public ServerConfig handle(final HTTPMethod method, final String path, final Handler handler) {
    this.handlerRegistrations.add(new HandlerRegistration(method, path, handler));
    return this;
  }

  public Server build() {
    return new Server(host, port, handlerRegistrations);
  }

}
