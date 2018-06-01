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

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final Header header = (Header) o;

    if (name != null ? !name.equals(header.name) : header.name != null) return false;
    return value != null ? value.equals(header.value) : header.value == null;
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (value != null ? value.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return String.format("%s: %s", name, value);
  }
}
