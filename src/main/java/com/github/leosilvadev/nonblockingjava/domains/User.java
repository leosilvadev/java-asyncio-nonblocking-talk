package com.github.leosilvadev.nonblockingjava.domains;

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
}
