package com.github.leosilvadev.nonblockingjava.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by leonardo on 5/26/18.
 */
public class IOUtil {

  public static void write(final SocketChannel socket, final String message) {
    try {
      socket.write(ByteBuffer.wrap((message + "\n").getBytes("UTF-8")));
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
