package com.github.leosilvadev.nonblockingjava.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by leonardo on 5/26/18.
 */
public class Json {

  private static final ObjectMapper mapper = new ObjectMapper();

  public static String toJson(final Object object) {
    try {
      return mapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      return "";
    }
  }

  public static <T> T fromJson(final String json, final TypeReference<T> typeReference) {
    try {
      return mapper.readValue(json, typeReference);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

}
