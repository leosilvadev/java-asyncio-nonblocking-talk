package com.github.leosilvadev.core.blocking;

import io.reactivex.Single;
import io.reactivex.internal.schedulers.RxThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * Created by leonardo on 6/2/18.
 */
public class Blocking {

  private static final ExecutorService executor;

  static {
    //TODO: READ FROM A GIVEN CONFIG
    executor = Executors.newCachedThreadPool(new RxThreadFactory("blocking"));
  }

  public static <T> Single<T> execute(final Supplier<T> function) {
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
