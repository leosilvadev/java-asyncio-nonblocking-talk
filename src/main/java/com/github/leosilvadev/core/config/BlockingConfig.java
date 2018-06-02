package com.github.leosilvadev.core.config;

/**
 * Created by leonardo on 6/2/18.
 */
public class BlockingConfig {

  private int threads;

  public BlockingConfig() {}

  public BlockingConfig(final int threads) {
    this.threads = threads;
  }

  public int getThreads() {
    return threads;
  }

  public void setThreads(final int threads) {
    this.threads = threads;
  }

  @Override
  public String toString() {
    return "Blocking{" +
        "threads=" + threads +
        '}';
  }
}