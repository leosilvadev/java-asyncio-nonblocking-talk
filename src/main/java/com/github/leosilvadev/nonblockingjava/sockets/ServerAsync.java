package com.github.leosilvadev.nonblockingjava.sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by leonardo on 4/19/18.
 */
public class ServerBlocking {

  public static void main(final String[] args) throws IOException {
    final ServerSocket server = new ServerSocket(3322);
    final ExecutorService executorService = Executors.newFixedThreadPool(100);
    final String defaultMessage = "THIS IS A DEFAULT MESSAGE!";

    while (true) {
      final Socket socket = server.accept();
      log("New connection!");
      executorService.execute(() -> {
        try {
          Thread.sleep(100);
          final String message = defaultMessage + "\n";
          socket.getOutputStream().write(message.getBytes());
          socket.getOutputStream().write("<DONE>\n".getBytes());
          log(message);
        } catch (final Exception ex) {
          ex.printStackTrace();
          throw new RuntimeException(ex);
        }
      });
    }

  }

  public static void log(final String msg) {
    System.out.println("[" + System.currentTimeMillis() + "] " + Thread.currentThread().getName() + " - " + msg);
  }

}
