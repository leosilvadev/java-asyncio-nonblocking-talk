package com.github.leosilvadev.core.routers;

import com.github.leosilvadev.core.handlers.Handler;
import com.github.leosilvadev.core.handlers.HandlerRegistration;
import com.github.leosilvadev.core.http.HTTPMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leonardo on 6/5/18.
 */
public class Router {

  private final List<HandlerRegistration> handlerRegistrations;

  public Router() {
    this.handlerRegistrations = new ArrayList<>();
  }

  public List<HandlerRegistration> getHandlerRegistrations() {
    return handlerRegistrations;
  }

  public Router get(final String path, final Handler handler) {
    return handle(HTTPMethod.GET, path, handler);
  }

  public Router post(final String path, final Handler handler) {
    return handle(HTTPMethod.POST, path, handler);
  }

  public Router put(final String path, final Handler handler) {
    return handle(HTTPMethod.PUT, path, handler);
  }

  public Router patch(final String path, final Handler handler) {
    return handle(HTTPMethod.PATCH, path, handler);
  }

  public Router delete(final String path, final Handler handler) {
    return handle(HTTPMethod.DELETE, path, handler);
  }

  public Router handle(final HTTPMethod method, final String path, final Handler handler) {
    this.handlerRegistrations.add(new HandlerRegistration(method, path, handler));
    return this;
  }

  public static Router config() {
    return new Router();
  }
}
