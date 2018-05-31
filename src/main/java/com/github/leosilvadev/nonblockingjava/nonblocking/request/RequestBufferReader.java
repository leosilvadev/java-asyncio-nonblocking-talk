package com.github.leosilvadev.nonblockingjava.nonblocking.request;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * Created by leonardo on 5/31/18.
 */
public class RequestBufferReader {

  private final StringBuffer payload;

  public RequestBufferReader() {
    this.payload = new StringBuffer();
  }

  public String[] read(final SocketChannel channel) throws IOException {
    while (true) {
      final ByteBuffer buffer = ByteBuffer.allocate(256);
      final int bytesRead = channel.read(buffer);

      if (bytesRead == -1) {
        break;
      }

      final String newLine = new String(buffer.array(), Charset.forName("UTF-8"));
      payload.append(newLine);
    }

    return payload.toString().split("\n");
  }
}
