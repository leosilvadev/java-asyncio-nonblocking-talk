package com.github.leosilvadev.nonblockingjava.nonblocking.request;

import com.github.leosilvadev.nonblockingjava.nonblocking.Header;
import com.github.leosilvadev.nonblockingjava.nonblocking.Headers;
import com.github.leosilvadev.nonblockingjava.nonblocking.InvalidHTTPDefinition;

import java.util.Optional;

/**
 * Created by leonardo on 5/31/18.
 */
public class RequestBuilder {

  private RequestDefinition definition;
  private String body;

  private Headers headers;

  public RequestBuilder() {
    this.headers = new Headers();
  }

  private Request build() {
    return new Request(definition, body, headers);
  }

  public Request build(final String[] lines) {
    final String firstLine = lines[0];
    this.definition = RequestDefinition.from(firstLine).orElseThrow(InvalidHTTPDefinition::new);

    boolean readingHeaders = true;
    for (final String line : lines) {
      if (line.isEmpty()) {
        readingHeaders = false;
        continue;
      }

      if (readingHeaders) {
        final Optional<Header> maybeHeader = Header.from(line);
        maybeHeader.ifPresent(header -> this.headers.add(header));

      } else {
        this.body = line;
        break;

      }
    }

    return build();
  }

}
