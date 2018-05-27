package com.github.leosilvadev.nonblockingjava.async;

import com.github.leosilvadev.nonblockingjava.async.services.UserServiceAsync;
import com.github.leosilvadev.nonblockingjava.utils.IOUtil;
import com.github.leosilvadev.nonblockingjava.utils.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by leonardo on 4/19/18.
 */
public class ServerAsync {

  private static final Logger logger = LoggerFactory.getLogger(ServerAsync.class);

  private final Integer threads;
  private final UserServiceAsync userService;

  public ServerAsync(final Integer threads) {
    this.threads = threads;
    this.userService = new UserServiceAsync(threads);
  }

  public void start(final Integer port) throws IOException {
    final ServerSocket server = new ServerSocket(port);
    final ExecutorService executorService = Executors.newFixedThreadPool(threads);
    logger.info("Server listening at port {}, using a pool with {} threads", port, threads);

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

  public static void main(final String[] args) throws IOException {
    final ServerAsync serverAsync = new ServerAsync(50);
    serverAsync.start(8080);
  }

}
