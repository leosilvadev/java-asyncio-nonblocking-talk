package com.github.leosilvadev.core;

import com.github.leosilvadev.core.consumers.DeadEventConsumer;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import io.reactivex.internal.schedulers.RxThreadFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by leonardo on 6/2/18.
 */
public class Core {

  private final EventBus eventBus;
  private final Set<Object> consumers;

  public Core() {
    final ExecutorService executor = Executors.newSingleThreadExecutor(new RxThreadFactory("core"));

    this.eventBus = new AsyncEventBus("core-eventbus", executor);
    this.consumers = new HashSet<>();

    this.registerConsumer(new DeadEventConsumer());
  }

  public Core publish(final Object event) {
    this.eventBus.post(event);
    return this;
  }

  public Core registerConsumer(final Object consumer) {
    this.eventBus.register(consumer);
    this.consumers.add(consumer);
    return this;
  }

  public Core unregisterConsumer(final Object consumer) {
    this.eventBus.unregister(consumer);
    return this;
  }

  public Core unregisterAllConsumers() {
    this.consumers.forEach(this.eventBus::unregister);
    return this;
  }
}
