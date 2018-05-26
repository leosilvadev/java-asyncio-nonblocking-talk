package com.github.leosilvadev.nonblockingjava.async;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.leosilvadev.nonblockingjava.domains.User;
import com.github.leosilvadev.nonblockingjava.dto.Response;
import com.github.leosilvadev.nonblockingjava.utils.Json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * Created by leonardo on 4/19/18.
 */
public class ClientAsync {

  private final String host;
  private final Integer port;
  private final ExecutorService executorService;

  public ClientAsync(final String host, final Integer port, final ExecutorService executorService) {
    this.host = host;
    this.port = port;
    this.executorService = executorService;
  }

  public void getUsers(final Consumer<Response> callback) {
    readAsyncFrom(callback);
  }

  private void readAsyncFrom(final Consumer<Response> callback) {
    executorService.execute(() -> {
      final long beginOfExecution = System.currentTimeMillis();
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
        final long endOfExecution = System.currentTimeMillis();
        final long executionTime = endOfExecution - beginOfExecution;

        final List<User> users = Json.fromJson(builder.toString(), new TypeReference<List<User>>() {});
        callback.accept(new Response(executionTime, users));
      }
    });
  }

  public static void log(final String msg) {
    System.out.println("[" + System.currentTimeMillis() + "] " + Thread.currentThread().getName() + " - " + msg);
  }

  public static void main(final String[] args) throws InterruptedException {
    final Integer clients = 50;
    final ExecutorService executorService = Executors.newFixedThreadPool(clients);
    final CountDownLatch counter = new CountDownLatch(clients);

    IntStream.range(0, clients)
        .forEach(client -> {
          new ClientAsync("localhost", 8080, executorService).getUsers(response -> {
            counter.countDown();
            log("(" + response.getExecutionTime() + " ms) " + counter.getCount());

          });
        });

    counter.await(5, TimeUnit.MINUTES);
    executorService.shutdown();
  }

}
