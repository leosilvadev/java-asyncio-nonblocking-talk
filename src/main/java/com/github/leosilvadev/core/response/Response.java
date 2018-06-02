package com.github.leosilvadev.core.response;

import com.github.leosilvadev.core.http.HTTPStatus;
import com.github.leosilvadev.core.http.Headers;

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

  public static ResponseBuilder builder() {
    return new ResponseBuilder();
  }

  public static ResponseBuilder ok() {
    return new ResponseBuilder().withStatus(HTTPStatus.OK);
  }

  public static ResponseBuilder created() {
    return new ResponseBuilder().withStatus(HTTPStatus.CREATED);
  }

  public static ResponseBuilder accepted() {
    return new ResponseBuilder().withStatus(HTTPStatus.ACCEPTED);
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
