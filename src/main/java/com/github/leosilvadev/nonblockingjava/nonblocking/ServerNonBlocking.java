package com.github.leosilvadev.nonblockingjava.nonblocking;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by leonardo on 5/26/18.
 */
public class ServerNonBlocking {

  public static void main(String[] args) throws IOException {
    final String defaultMessage = "THIS IS A DEFAULT MESSAGE!\n";
    final Selector selector = Selector.open();
    final ServerSocketChannel serverSocket = ServerSocketChannel.open();
    serverSocket.bind(new InetSocketAddress("localhost", 3322));
    serverSocket.configureBlocking(false);
    serverSocket.register(selector, SelectionKey.OP_ACCEPT);

    while (true) {
      selector.select();
      final Set<SelectionKey> selectedKeys = selector.selectedKeys();
      final Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
      while (keyIterator.hasNext()) {
        final SelectionKey key = keyIterator.next();
        if (key.isAcceptable()) {
          ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
          SocketChannel client = serverSocketChannel.accept();
          client.configureBlocking(false);
          client.register(selector, SelectionKey.OP_WRITE);

        } else if (key.isWritable()) {
          write(defaultMessage, key);

        }
        keyIterator.remove();
      }
    }
  }

  private static void write(final String message, final SelectionKey key)
      throws IOException {

    final SocketChannel client = (SocketChannel) key.channel();
    client.write(ByteBuffer.wrap(message.getBytes(Charset.forName("UTF-8"))));
    client.write(ByteBuffer.wrap("<DONE>\n".getBytes(Charset.forName("UTF-8"))));
    client.close();
  }

  private static void registerToWrite(Selector selector, ServerSocketChannel serverSocket)
      throws IOException {

    SocketChannel client = serverSocket.accept();
    client.configureBlocking(false);
    client.register(selector, SelectionKey.OP_WRITE & SelectionKey.OP_READ);
  }
}
