package com.github.leosilvadev.nonblockingjava.nonblocking.response;

import com.github.leosilvadev.nonblockingjava.nonblocking.http.HTTPStatus;
import com.github.leosilvadev.nonblockingjava.nonblocking.http.Header;
import com.github.leosilvadev.nonblockingjava.nonblocking.http.Headers;

/**
 * Created by leonardo on 6/1/18.
 */
public class ResponseBuilder {

  private HTTPStatus status;
  private String body;
  private Headers headers;

  public ResponseBuilder() {
    this.headers = new Headers();
  }

  public ResponseBuilder withStatus(final HTTPStatus status) {
    this.status = status;
    return this;
  }

  public ResponseBuilder withBody(final String body) {
    this.body = body;
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
