package com.github.leosilvadev.nonblockingjava.nonblocking.request;

import com.github.leosilvadev.nonblockingjava.nonblocking.http.HTTPMethod;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by leonardo on 5/31/18.
 */
public final class RequestDefinition {

  private static final Pattern METHODS_PATTERN;

  static {
    final String pattern = Arrays.stream(HTTPMethod.values())
        .map(HTTPMethod::toString)
        .collect(Collectors.joining("|"));

    METHODS_PATTERN = Pattern.compile("(" + pattern + ") (/.*) (HTTP/1\\.1)");
  }

  private final HTTPMethod method;
  private final String path;

  /*
  * Only http version 1 supported so far
  * */
  private final String version;

  private RequestDefinition(final HTTPMethod method, final String path, final String version) {
    this.method = method;
    this.path = path;
    this.version = version;
  }

  public HTTPMethod getMethod() {
    return method;
  }

  public String getPath() {
    return path;
  }

  public String getVersion() {
    return version;
  }

  public boolean isGet() {
    return HTTPMethod.GET.equals(this.method);
  }

  public static Optional<RequestDefinition> from(final String line) {
    final Matcher matcher = METHODS_PATTERN.matcher(line);
    if (matcher.matches()) {
      final Optional<HTTPMethod> maybeMethod = HTTPMethod.from(matcher.group(1));
      if (maybeMethod.isPresent()) {
        return Optional.of(new RequestDefinition(maybeMethod.get(), matcher.group(2), matcher.group(3)));

      }
    }

    return Optional.empty();
  }
}
