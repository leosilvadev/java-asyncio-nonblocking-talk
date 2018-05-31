package com.github.leosilvadev.nonblockingjava.nonblocking.request

import spock.lang.Specification

import java.nio.ByteBuffer
import java.nio.channels.SocketChannel

/**
 * Created by leonardo on 5/31/18.
 */
class RequestBufferReaderSpec extends Specification {

  def 'Should read a POST request with headers and body'() {
    def parts = [
      'POST /v1/use',
      'rs HTTP/1.1\n',
      'Content-Ty',
      'pe: application/json\n',
      'Accept: applicati',
      'on/json\n\n',
      '{"name":',
      '"Jao"}'
    ]

    given:
    def channel = Mock(SocketChannel)

    and:
    channel.read(_ as ByteBuffer) >> { ByteBuffer buffer ->
      def (tail, bytesRead) = Fixture.mockChannelRead(buffer, parts)
      parts = tail
      return bytesRead
    }

    and:
    def reader = new RequestBufferReader()

    when:
    def lines = reader.read(channel)

    then:
    lines.size() == 5
  }

}
