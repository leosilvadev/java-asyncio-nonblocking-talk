package com.github.leosilvadev.nonblockingjava.nonblocking.response;

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
      socket.write(ByteBuffer.wrap((response.toString() + "\n").getBytes("UTF-8")));
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}