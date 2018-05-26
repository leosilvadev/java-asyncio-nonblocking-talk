package com.github.leosilvadev.nonblockingjava.sockets;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by leonardo on 4/19/18.
 */
public class ClientAsync {

  public static void main(final String[] args) throws Exception {
    final Integer clients = 1000;
    final ExecutorService executorService = Executors.newFixedThreadPool(50);
    final CountDownLatch counter = new CountDownLatch(clients);

    for (Long client = 0L; client < clients; client++) {
      readAsyncFrom("localhost", 3322, executorService, counter::countDown);
    }

    counter.await(5, TimeUnit.MINUTES);
    executorService.shutdown();
  }

  public static void readAsyncFrom(final String host, final Integer port, final ExecutorService executorService,
                                   final Runnable onFinish) {
    final long beginningOfExecution = System.currentTimeMillis();
    executorService.execute(() -> {
      final StringBuilder builder = new StringBuilder();

      try (Socket socket = new Socket(host, port)) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
          if (line.equals("<BEGIN>")) continue;
          if (line.equals("<END>")) break;
          builder.append(line);
        }

      } catch (UnknownHostException ex) {
        log("Server not found: " + ex.getMessage());

      } catch (IOException ex) {
        log("I/O error: " + ex.getMessage());

      } finally {
        onFinish.run();
        final long endOfExecution = System.currentTimeMillis();
        final long executionTime = endOfExecution - beginningOfExecution;
        log("(" + executionTime + " ms)" + builder.toString());

      }
    });
  }

  public static void log(final String msg) {
    System.out.println("[" + System.currentTimeMillis() + "] " + Thread.currentThread().getName() + " - " + msg);
  }

}
