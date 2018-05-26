package com.github.leosilvadev.nonblockingjava.async;

import com.github.leosilvadev.nonblockingjava.async.services.UserServiceAsync;
import com.github.leosilvadev.nonblockingjava.utils.IOUtil;
import com.github.leosilvadev.nonblockingjava.utils.Json;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by leonardo on 4/19/18.
 */
public class ServerAsync {

  private final Integer threads;
  private final UserServiceAsync userService;

  public ServerAsync(final Integer threads) {
    this.threads = threads;
    this.userService = new UserServiceAsync(threads);
  }

  public void start(final Integer port) throws IOException {
    final ServerSocket server = new ServerSocket(port);
    final ExecutorService executorService = Executors.newFixedThreadPool(threads);
    log("Server listening at port " + port + ", using a pool with " + threads + " threads");

    executorService.execute(() -> {
      while (true) {
        try {
          final Socket socket = server.accept();
          executorService.execute(getUsersHandler(socket));

        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }

  private Runnable getUsersHandler(final Socket socket){
    return () -> {
      IOUtil.write(socket, IOUtil.BEGIN_MSG);
      userService.getUsers(users -> {
        IOUtil.write(socket, Json.toJson(users));
        IOUtil.write(socket, IOUtil.END_MSG);
      });
    };
  }

  public static void log(final String msg) {
    System.out.println("[" + System.currentTimeMillis() + "] " + Thread.currentThread().getName() + " - " + msg);
  }

  public static void main(final String[] args) throws IOException {
    final ServerAsync serverAsync = new ServerAsync(50);
    serverAsync.start(8080);
  }

}
