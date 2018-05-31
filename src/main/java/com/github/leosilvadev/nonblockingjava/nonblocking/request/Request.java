package com.github.leosilvadev.nonblockingjava.nonblocking;

import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by leonardo on 5/31/18.
 */
public class Request {

  private final HttpMethod method;
  private final String path;

  private final Buffer body;

  private final String contentType;
  private final String accept;

  private final Headers headers;

  private Request(final HttpMethod method,
                 final String path,
                 final Buffer body,
                 final String contentType,
                 final String accept,
                 final Headers headers) {
    this.method = method;
    this.path = path;
    this.body = body;
    this.contentType = contentType;
    this.accept = accept;
    this.headers = headers;
  }

  public HttpMethod getMethod() {
    return method;
  }

  public String getPath() {
    return path;
  }

  public Headers getHeaders() {
    return headers;
  }

  public Buffer getBody() {
    return body;
  }

  public String getContentType() {
    return contentType;
  }

  public String getAccept() {
    return accept;
  }

  public class Headers {
    private final Map<String, String> map;

    public Headers() {
      this.map = new HashMap<>();
    }

    public Optional<String> get(final String key) {
      return Optional.ofNullable(map.get(key));
    }

    public void add(final String key, final String value) {
      this.map.put(key, value);
    }
  }

  public static class Builder {

    private HttpMethod method;
    private String path;

    private Buffer body;

    private String contentType;
    private String accept;

    private Headers headers;

    public Builder withMethod(final HttpMethod method) {
      this.method = method;
      return this;
    }

    public Builder withPath(final String path) {
      this.path = path;
      return this;
    }

    public Builder withBody(final Buffer body) {
      this.body = body;
      return this;
    }

    public Builder withContentType(final String contentType) {
      this.contentType = contentType;
      return this;
    }

    public Builder withAccept(final String accept) {
      this.accept = accept;
      return this;
    }

    public Builder addHeader(final String key, final String value) {
      this.headers.add(key, value);
      return this;
    }

    public Request build() {
      //TODO: VALIDATE MISSING DATA
      return new Request(method, path, body, contentType, accept, headers);
    }
  }
}
