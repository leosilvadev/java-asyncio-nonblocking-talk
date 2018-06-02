package com.github.leosilvadev.core.response;

import com.github.leosilvadev.core.http.HTTPStatus;
import com.github.leosilvadev.core.http.Header;
import com.github.leosilvadev.core.http.Headers;
import com.github.leosilvadev.core.http.MIMEType;
import com.github.leosilvadev.core.json.Json;

/**
 * Created by leonardo on 6/1/18.
 */
public final class ResponseBuilder {

  private HTTPStatus status;
  private String body;
  private Headers headers;

  protected ResponseBuilder() {
    this.headers = new Headers();
  }

  public ResponseBuilder withStatus(final HTTPStatus status) {
    this.status = status;
    return this;
  }

  public ResponseBuilder withBody(final String body, final String contentType) {
    this.body = body;
    return this.withContentType(contentType);
  }

  public ResponseBuilder json(final String body) {
    this.body = body;
    return this.withContentType(MIMEType.APPLICATION_JSON);
  }

  public ResponseBuilder json(final Object body) {
    this.body = Json.toJson(body);
    return this.withContentType(MIMEType.APPLICATION_JSON);
  }

  public ResponseBuilder withContentType(final String contentType) {
    this.addHeader("Content-Type", contentType);
    return this;
  }

  public ResponseBuilder addHeader(final String key, final String value) {
    this.headers.add(new Header(key, value));
    return this;
  }

  public Response build() {
    return new Response(status, headers, body);
  }

}
