package com.github.leosilvadev.core.http;

import com.github.leosilvadev.core.utils.Ensure;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Created by leonardo on 5/31/18.
 */
public class Headers {

  private final Set<Header> headers;

  public Headers() {
    this.headers = new HashSet<>();
  }

  public Optional<Header> get(final String name) {
    Ensure.isNotEmpty(name);
    return headers.stream().filter(h -> h.getName().equalsIgnoreCase(name)).findFirst();
  }

  public Stream<Header> stream() {
    return headers.stream();
  }

  public Headers add(final Header header) {
    this.headers.add(header);
    return this;
  }

  public int size() {
    return headers.size();
  }
}