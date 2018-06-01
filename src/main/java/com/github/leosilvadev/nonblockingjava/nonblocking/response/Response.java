package com.github.leosilvadev.nonblockingjava.nonblocking.response;

import com.github.leosilvadev.nonblockingjava.nonblocking.http.HTTPStatus;
import com.github.leosilvadev.nonblockingjava.nonblocking.http.Headers;

/**
 * Created by leonardo on 5/31/18.
 */
public final class Response {

  private final HTTPStatus status;
  private final Headers headers;
  private final String body;

  public Response(final HTTPStatus status, final Headers headers, final String body) {
    this.status = status;
    this.headers = headers;
    this.body = body == null ? "" : body;
  }

  public HTTPStatus getStatus() {
    return status;
  }

  public Headers getHeaders() {
    return headers;
  }

  public String getBody() {
    return body;
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append(status.toString());
    builder.append("\n");
    headers.stream().forEach(header -> builder.append(header.toString()).append("\n"));
    builder.append("\n");
    builder.append(body);
    return builder.toString();
  }
}
