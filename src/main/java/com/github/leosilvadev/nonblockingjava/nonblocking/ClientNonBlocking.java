package com.github.leosilvadev.nonblockingjava.nonblocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by leonardo on 4/19/18.
 */
public class ClientNonBlocking {

  public static void main(final String[] args) throws Exception {
    final Integer clients = 100;
    final Selector selector = Selector.open();
    final CountDownLatch counter = new CountDownLatch(clients);

    for (Integer client = 0 ; client < clients ; client++) {
      connect(new InetSocketAddress(3322), selector);
    }

    readNonBlocking(selector, counter);
  }

  public static void connect(final SocketAddress address, final Selector selector)
      throws IOException, InterruptedException, ExecutionException, TimeoutException {
    final long beginningOfExecution = System.currentTimeMillis();
    final SocketChannel channel = SocketChannel.open();
    channel.configureBlocking(false);
    channel.register(selector, SelectionKey.OP_CONNECT & SelectionKey.OP_READ).attach(beginningOfExecution);
    channel.connect(address);
  }

  public static void readNonBlocking(final Selector selector, final CountDownLatch counter) throws IOException {
    while(counter.getCount() > 0) {
      selector.select();

      final Set<SelectionKey> selectedKeys = selector.selectedKeys();
      final Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

      while(keyIterator.hasNext()) {
        final SelectionKey key = keyIterator.next();
        final SocketChannel channel = (SocketChannel) key.channel();

        if (key.isConnectable()) {
          channel.finishConnect();

        } else if (key.isReadable()) {
          readData(channel, (Long) key.attachment(), counter);

        } else {
          keyIterator.remove();
        }
      }
    }
  }

  private static void readData(final SocketChannel channel, final Long beginningOfExecution, final CountDownLatch counter) throws IOException {
    final ByteBuffer buffer = ByteBuffer.allocate(1024);
    channel.read(buffer);

    final String[] messages = new String(buffer.array()).trim().split("\n");
    for (String message : messages) {
      if (message.equals("<DONE>")) {
        channel.close();
        counter.countDown();
        break;
      } else {
        final long endOfExecution = System.currentTimeMillis();
        final long executionTime = endOfExecution - beginningOfExecution;
        log("(" + executionTime + " ms) " + message);
      }
    }

    buffer.clear();
  }

  private static void log(final String msg) {
    System.out.println("[" + System.currentTimeMillis() + "] " + Thread.currentThread().getName() + " - " + msg);
  }

}
