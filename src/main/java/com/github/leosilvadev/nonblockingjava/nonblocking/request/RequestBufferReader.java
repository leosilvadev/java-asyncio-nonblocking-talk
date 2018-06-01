package com.github.leosilvadev.nonblockingjava.nonblocking.request;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by leonardo on 5/31/18.
 */
public class RequestBufferReader {

  private final StringBuffer payload;

  public RequestBufferReader() {
    this.payload = new StringBuffer();
  }

  public List<String> read(final SocketChannel channel) throws IOException {
    while (true) {
      final ByteBuffer buffer = ByteBuffer.allocate(256);
      final int bytesRead = channel.read(buffer);

      if (bytesRead <= 0) {
        break;
      }

      final String line = new String(buffer.array(), Charset.forName("UTF-8"));
      if (line.contains("\n")) {
        payload.append(line);
      } else {
        payload.append(line.trim());
      }
    }

    return Arrays.stream(payload.toString().split("\n"))
        .map(String::trim)
        .collect(Collectors.toList());
  }
}
