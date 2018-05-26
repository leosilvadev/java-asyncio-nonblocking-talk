package com.github.leosilvadev.nonblockingjava.services;

import com.github.leosilvadev.nonblockingjava.domains.User;
import com.github.leosilvadev.nonblockingjava.utils.IOUtil;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * Created by leonardo on 5/26/18.
 */
public class UserService {

  private final ExecutorService executor;
  private final List<User> users;

  public UserService(final Integer threads) {
    executor = Executors.newFixedThreadPool(threads);
    users = LongStream.range(1, 101)
        .mapToObj(n -> new User(n + "user@gmail.com", n + "secret"))
        .collect(Collectors.toList());
  }

  public void getUsers(final Consumer<List<User>> callback) {
    executor.execute(() -> {
      IOUtil.sleepFor(300L);
      callback.accept(users);
    });
  }

}
