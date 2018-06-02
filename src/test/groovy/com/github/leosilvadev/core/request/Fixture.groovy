package com.github.leosilvadev.core.request

import java.nio.ByteBuffer
import java.nio.charset.Charset

/**
 * Created by leonardo on 6/1/18.
 */
class Fixture {

  static mockChannelRead(final ByteBuffer buffer, final List<String> parts) {
    if (parts.size() > 0) {
      def l = parts.first()
      byte[] bytes = l.getBytes(Charset.forName('UTF-8'))
      for (byte b : bytes) {
        buffer.put(b)
      }
      return [parts.tail(), bytes.size()]

    } else {
      return [[], -1]

    }
  }

}
