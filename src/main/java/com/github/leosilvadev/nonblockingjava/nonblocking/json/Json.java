package com.github.leosilvadev.nonblockingjava.nonblocking.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by leonardo on 6/1/18.
 */
public final class Json {

  private static final ObjectMapper mapper;

  static {
    mapper = new ObjectMapper();
  }

  public static String toJson(final Object object) {
    try {
      return mapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      return "";
    }
  }

  public static <T> Optional<T> parse(final String json, final Class<T> clazz) {
    try {
      return Optional.ofNullable(mapper.readValue(json, clazz));

    } catch (IOException e) {
      return Optional.empty();

    }
  }
}
