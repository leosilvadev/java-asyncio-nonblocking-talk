package com.github.leosilvadev.nonblockingjava.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by leonardo on 5/26/18.
 */
public class NonBlockingTimer {

  private final BlockingQueue<Task> tasks;
  private final ExecutorService executorService;

  public NonBlockingTimer() {
    this.tasks = new LinkedBlockingQueue<>();
    this.executorService = Executors.newSingleThreadExecutor();
  }

  public void schedule(final Runnable callback, final long afterMs) {
    final long now = System.currentTimeMillis();
    tasks.add(new Task(callback, now + afterMs));
  }

  public void start() {
    executorService.execute(() -> {
      while (true) {
        final Task task = tasks.poll();
        if (task == null) {
          continue;
        }
        final long now = System.currentTimeMillis();
        if (task.getExecuteAt() <= now) {
          task.getCallback().run();
        } else {
          tasks.add(task);
        }
      }
    });
  }

  public class Task {

    private final long executeAt;
    private final Runnable callback;

    public Task(final Runnable callback, final Long executeAt) {
      this.executeAt = executeAt;
      this.callback = callback;
    }

    public long getExecuteAt() {
      return executeAt;
    }

    public Runnable getCallback() {
      return callback;
    }
  }

}
