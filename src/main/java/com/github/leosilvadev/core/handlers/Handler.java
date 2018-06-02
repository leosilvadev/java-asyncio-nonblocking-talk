package com.github.leosilvadev.core.handlers;

import com.github.leosilvadev.core.request.Request;
import com.github.leosilvadev.core.response.Response;
import io.reactivex.Single;

/**
 * Created by leonardo on 6/1/18.
 */
public interface Handler {

  public Single<Response> handle(final Request request);

}
