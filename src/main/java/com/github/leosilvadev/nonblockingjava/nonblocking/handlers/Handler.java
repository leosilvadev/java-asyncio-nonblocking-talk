package com.github.leosilvadev.nonblockingjava.nonblocking.handlers;

import com.github.leosilvadev.nonblockingjava.nonblocking.request.Request;
import com.github.leosilvadev.nonblockingjava.nonblocking.response.Response;
import io.reactivex.Single;

/**
 * Created by leonardo on 6/1/18.
 */
public interface Handler {

  public Single<Response> handle(final Request request);

}
