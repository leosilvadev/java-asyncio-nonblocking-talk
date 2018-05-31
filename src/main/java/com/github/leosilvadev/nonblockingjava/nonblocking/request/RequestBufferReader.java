package com.github.leosilvadev.nonblockingjava.nonblocking.request;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by leonardo on 5/31/18.
 */
public class RequestBufferReader {

  private final StringBuffer payload;

  public RequestBufferReader() {
    this.payload = new StringBuffer();
  }

  public String[] read(final SocketChannel channel) throws IOException {
    final ByteBuffer buffer = ByteBuffer.allocate(256);
    while (true) {
      channel.read(buffer);

      final String newLine = new String(buffer.array());
      buffer.clear();
      payload.append(newLine);
      if (newLine.isEmpty()) {
        break;
      }
    }

    return payload.toString().split("\n");
  }
}
