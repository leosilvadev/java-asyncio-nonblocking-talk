package com.github.leosilvadev.core.response;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by leonardo on 6/1/18.
 */
public final class Responder {

  private final Response response;

  public Responder(final Response response) {
    this.response = response;
  }

  public void respond(final SocketChannel socket) {
    try {
      socket.write(ByteBuffer.wrap((response.toString()).getBytes("UTF-8")));
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
