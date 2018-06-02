package com.github.leosilvadev.core.utils;

import java.util.Collection;

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

  public static void isNotEmpty(final Collection collection) {
    if (collection == null || collection.size() == 0) throw  new IllegalArgumentException("Argument cannot be null or empty");
  }

}
