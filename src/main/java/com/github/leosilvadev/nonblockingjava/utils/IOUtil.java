package com.github.leosilvadev.nonblockingjava.utils;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by leonardo on 5/26/18.
 */
public class IOUtil {

  public static void sleepFor(final Long time) {
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



}
