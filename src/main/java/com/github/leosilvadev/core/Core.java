package com.github.leosilvadev.core;

import com.github.leosilvadev.core.blocking.Blocking;
import com.github.leosilvadev.core.config.exceptions.MissingConfigurationException;
import com.github.leosilvadev.core.consumers.DeadEventConsumer;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import io.reactivex.internal.schedulers.RxThreadFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.Executors;

/**
 * Created by leonardo on 6/2/18.
 */
public class Core {

  private final Injector injector;
  private final EventBus eventBus;
  private final Set<Object> consumers;

  protected Core(final URI configPath, final Iterable<Module> modules) {
    this.injector = Guice.createInjector(modules);
    this.eventBus = new AsyncEventBus(
        "core-eventbus",
        Executors.newSingleThreadExecutor(new RxThreadFactory("core"))
    );
    this.consumers = new HashSet<>();

    this.registerConsumer(new DeadEventConsumer());
  }

  public <T> T getInstance(final Class<T> clazz) {
    return injector.getInstance(clazz);
  }

  public Blocking blockingExecutor() {
    return injector.getInstance(Blocking.class);
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

  public static Core create(final String configPath, final Module... modules) {
    try {
      final URI uri = ClassLoader.getSystemResource(configPath).toURI();
      final List<Module> coreModules = new ArrayList<>();
      coreModules.add(new CoreModule(uri));
      coreModules.addAll(Arrays.asList(modules));
      return new Core(uri, coreModules);

    } catch (final URISyntaxException e) {
      throw new MissingConfigurationException(configPath);

    }
  }
}
