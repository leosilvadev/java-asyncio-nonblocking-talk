package com.github.leosilvadev.nonblockingjava.nonblocking.http;

import com.github.leosilvadev.nonblockingjava.nonblocking.utils.Ensure;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by leonardo on 5/31/18.
 */
public class Header {

  private static final Pattern HEADER_PATTERN;

  static {
    HEADER_PATTERN = Pattern.compile("(.+): (.+)");
  }

  private final String name;
  private final String value;

  public Header(final String name, final String value) {
    Ensure.isNotEmpty(name);
    Ensure.isNotEmpty(value);
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

  public static Optional<Header> from(final String line) {
    final Matcher matcher = HEADER_PATTERN.matcher(line);
    if (matcher.matches()) {
      return Optional.of(new Header(matcher.group(1), matcher.group(2)));
    }

    return Optional.empty();
  }
}
