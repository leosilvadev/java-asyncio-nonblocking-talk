package com.github.leosilvadev.core.request;

import com.github.leosilvadev.core.exceptions.InvalidHTTPDefinitionException;
import com.github.leosilvadev.core.utils.Ensure;
import com.github.leosilvadev.core.http.Header;
import com.github.leosilvadev.core.http.Headers;

import java.util.List;
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

  public Request build(final List<String> lines) {
    Ensure.isNotEmpty(lines);

    final String firstLine = lines.get(0);
    this.definition = RequestDefinition.from(firstLine).orElseThrow(InvalidHTTPDefinitionException::new);

    boolean readingHeaders = true;
    for (final String line : lines) {
      if (line.isEmpty()) {
        readingHeaders = false;
        continue;
      }

      if (readingHeaders) {
        final Optional<Header> maybeHeader = Header.from(line);
        maybeHeader.ifPresent(header -> this.headers.add(header));

      } else if (this.definition.isGet()) {
        break;

      } else {
        this.body = line;
        break;

      }
    }

    return build();
  }

}
