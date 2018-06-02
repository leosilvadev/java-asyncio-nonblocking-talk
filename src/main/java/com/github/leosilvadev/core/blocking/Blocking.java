package com.github.leosilvadev.core.blocking;

import com.github.leosilvadev.core.config.Configuration;
import io.reactivex.Single;
import io.reactivex.internal.schedulers.RxThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * Created by leonardo on 6/2/18.
 */
public class Blocking {

  private final ExecutorService executor;

  public Blocking(final Configuration configuration) {
    final int blockingThreads = configuration.getBlocking().getThreads();
    final RxThreadFactory rxThreadFactory = new RxThreadFactory("blocking");
    executor = blockingThreads == 0 ?
        Executors.newCachedThreadPool(rxThreadFactory) :
        Executors.newFixedThreadPool(blockingThreads, rxThreadFactory);
  }

  public <T> Single<T> execute(final Supplier<T> function) {
    return Single.create(emitter ->
        executor.execute(() -> {
          try {
            final T response = function.get();
            emitter.onSuccess(response);

          } catch (final Exception ex) {
            emitter.onError(ex);

          }
        })
    );
  }

}
