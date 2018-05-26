package com.github.leosilvadev.nonblockingjava.sockets;

import com.github.leosilvadev.nonblockingjava.services.UserService;
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

  public static void main(final String[] args) throws IOException {
    final ServerSocket server = new ServerSocket(3322);
    final ExecutorService executorService = Executors.newFixedThreadPool(50);

    final UserService userService = new UserService();

    while (true) {
      final Socket socket = server.accept();
      log("New connection!");
      executorService.execute(() -> {
        IOUtil.write(socket, "<BEGIN>");
        userService.getUsers(users -> {
          IOUtil.write(socket, Json.toJson(users));
          IOUtil.write(socket, "<END>");
        });
      });
    }

  }

  public static void log(final String msg) {
    System.out.println("[" + System.currentTimeMillis() + "] " + Thread.currentThread().getName() + " - " + msg);
  }

}
