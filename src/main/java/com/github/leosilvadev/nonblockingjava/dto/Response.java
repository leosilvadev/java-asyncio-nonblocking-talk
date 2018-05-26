package com.github.leosilvadev.nonblockingjava.dto;

import com.github.leosilvadev.nonblockingjava.domains.User;

import java.util.List;

/**
 * Created by leonardo on 5/26/18.
 */
public class Response {

  private final long executionTime;
  private final List<User> users;

  public Response(final long executionTime, final List<User> users) {
    this.executionTime = executionTime;
    this.users = users;
  }

  public long getExecutionTime() {
    return executionTime;
  }

  public List<User> getUsers() {
    return users;
  }
}
