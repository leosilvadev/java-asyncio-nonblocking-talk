package com.github.leosilvadev.nonblockingjava.domains;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by leonardo on 5/26/18.
 */
public class User {

  private static final AtomicLong idGenerator = new AtomicLong(0);

  private final Long id;
  private final String uid;
  private final String email;
  private final String password;

  @JsonCreator
  public User(@JsonProperty("id") final Long id,
              @JsonProperty("uid") final String uid,
              @JsonProperty("email") final String email,
              @JsonProperty("password") final String password) {
    this.id = id;
    this.uid = uid;
    this.email = email;
    this.password = password;
  }

  public User(final String email, final String password) {
    this.id = idGenerator.incrementAndGet();
    this.uid = UUID.randomUUID().toString();
    this.email = email;
    this.password = password;
  }

  public static AtomicLong getIdGenerator() {
    return idGenerator;
  }

  public Long getId() {
    return id;
  }

  public String getUid() {
    return uid;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  @Override
  public String toString() {
    return "User{" +
        "id=" + id +
        ", uid='" + uid + '\'' +
        ", email='" + email + '\'' +
        ", password='" + password + '\'' +
        '}';
  }
}
