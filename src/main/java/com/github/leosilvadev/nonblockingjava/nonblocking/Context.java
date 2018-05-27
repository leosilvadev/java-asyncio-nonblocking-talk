package com.github.leosilvadev.nonblockingjava.nonblocking;

import java.util.UUID;

/**
 * Created by leonardo on 5/27/18.
 */
public class Context {

  private final String id;
  private final long beganAt;
  private final PartialResponse response;

  public Context(final String id, final long beganAt, final PartialResponse partialResponse) {
    this.id = id;
    this.beganAt = beganAt;
    this.response = partialResponse;
  }

  public Context() {
    this(UUID.randomUUID().toString(), System.currentTimeMillis(), new PartialResponse());
  }

  public Context copy(final PartialResponse partialResponse) {
    return new Context(id, beganAt, partialResponse);
  }

  public String getId() {
    return id;
  }

  public long getBeganAt() {
    return beganAt;
  }

  public String getResponse() {
    return response.getContent().toString();
  }

  public PartialResponse getPartialResponse() {
    return response;
  }
}
