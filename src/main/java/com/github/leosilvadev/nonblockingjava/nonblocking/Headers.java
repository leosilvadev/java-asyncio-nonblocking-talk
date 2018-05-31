package com.github.leosilvadev.nonblockingjava.nonblocking;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

  public void add(final Header header) {
    this.headers.add(header);
  }

  public int size() {
    return headers.size();
  }
}