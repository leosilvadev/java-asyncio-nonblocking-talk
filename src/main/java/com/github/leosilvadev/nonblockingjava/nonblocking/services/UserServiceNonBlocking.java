package com.github.leosilvadev.nonblockingjava.nonblocking.services;

import com.github.leosilvadev.nonblockingjava.domains.User;
import com.github.leosilvadev.nonblockingjava.utils.NonBlockingTimer;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * Created by leonardo on 5/26/18.
 */
public class UserServiceNonBlocking {

  private final NonBlockingTimer timer;
  private final List<User> users;

  public UserServiceNonBlocking() {
    this.timer = new NonBlockingTimer();
    this.users = LongStream.range(1, 101)
        .mapToObj(n -> new User(n + "user@gmail.com", n + "secret"))
        .collect(Collectors.toList());
    this.timer.start();
  }

  public void getUsers(final Consumer<List<User>> callback) {
    timer.schedule(() -> callback.accept(users), 300L);
  }

}
