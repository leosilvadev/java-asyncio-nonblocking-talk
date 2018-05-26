package com.github.leosilvadev.nonblockingjava.utils;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by leonardo on 5/26/18.
 */
public class IOUtil {

  public static final String BEGIN_MSG = "<BEGIN>";
  public static final String END_MSG = "<END>";

  public static void sleepFor(final long time) {
    try {
      Thread.sleep(time);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static void write(final Socket socket, final String message) {
    try {
      socket.getOutputStream().write((message + "\n").getBytes("UTF-8"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void write(final SocketChannel socket, final String message) {
    try {
      socket.write(ByteBuffer.wrap((message + "\n").getBytes("UTF-8")));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
