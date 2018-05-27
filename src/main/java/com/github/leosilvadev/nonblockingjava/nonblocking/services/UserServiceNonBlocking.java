package com.github.leosilvadev.nonblockingjava.nonblocking.services;

import com.github.leosilvadev.nonblockingjava.domains.User;
import com.github.leosilvadev.nonblockingjava.utils.Json;
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
  private final String usersJson;

  public UserServiceNonBlocking() {
    this.timer = new NonBlockingTimer();
    this.users = LongStream.range(1, 101)
        .mapToObj(n -> new User(n + "user@gmail.com", n + "secret"))
        .collect(Collectors.toList());
    this.usersJson = Json.toJson(users);
    this.timer.start();
  }

  public void getUsers(final Consumer<List<User>> callback) {
    timer.schedule(() -> callback.accept(users), 300L);
  }

  // Using the home-made non-blocking timer, that instead of using a thread pool and sleeping threads,
  // it keeps a thread running and monitoring all the scheduled tasks
  public void getUsersJson(final Consumer<String> callback) {
    timer.schedule(() -> callback.accept(usersJson), 300L);
  }

}
