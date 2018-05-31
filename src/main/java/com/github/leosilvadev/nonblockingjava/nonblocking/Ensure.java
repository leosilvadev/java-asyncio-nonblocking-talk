package com.github.leosilvadev.nonblockingjava.nonblocking;

/**
 * Created by leonardo on 5/31/18.
 */
public final class Ensure {

  public static void isNotNull(final Object object) {
    if (object == null) throw new IllegalArgumentException("Argument cannot be null");
  }

  public static void isNotEmpty(final String object) {
    if (object == null || object.isEmpty()) throw new IllegalArgumentException("Argument cannot be null or empty");
  }

}
