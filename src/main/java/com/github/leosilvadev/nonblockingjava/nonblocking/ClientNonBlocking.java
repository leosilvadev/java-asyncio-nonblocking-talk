package com.github.leosilvadev.nonblockingjava.nonblocking;

import com.github.leosilvadev.nonblockingjava.utils.IOUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

/**
 * Created by leonardo on 4/19/18.
 */
public class ClientNonBlocking {

  private final InetSocketAddress address;
  private final Selector selector;

  public ClientNonBlocking(final InetSocketAddress address, final Selector selector) {
    this.address = address;
    this.selector = selector;
  }

  public ClientNonBlocking connect() throws IOException, InterruptedException, ExecutionException, TimeoutException {
    final Context context = new Context();
    final SocketChannel channel = SocketChannel.open();
    channel.configureBlocking(false);
    channel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ).attach(context);
    channel.connect(address);
    return this;
  }

  private static void readNonBlocking(final Selector selector, final CountDownLatch counter, final Consumer<Context> callback) throws IOException {
    while(counter.getCount() > 0) {
      selector.select();

      final Set<SelectionKey> selectedKeys = selector.selectedKeys();
      final Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

      while(keyIterator.hasNext()) {
        final SelectionKey key = keyIterator.next();
        final SocketChannel channel = (SocketChannel) key.channel();
        channel.configureBlocking(false);

        if (key.isConnectable()) {
          channel.finishConnect();

        } else if (key.isReadable()) {
          final Context context = (Context) key.attachment();
          final PartialResponse partialResponse = readData(channel, context.getPartialResponse());
          if (partialResponse.isComplete()) {
            callback.accept(context.copy(partialResponse));
            key.cancel();

          } else {
            final Context updatedContext = context.copy(partialResponse);
            key.attach(updatedContext);

          }

        } else {
          keyIterator.remove();
        }
      }
    }
  }

  private static PartialResponse readData(final SocketChannel channel, final PartialResponse partialResponse) throws IOException {
    final ByteBuffer buffer = ByteBuffer.allocate(256);
    channel.read(buffer);

    final String[] messages = new String(buffer.array()).trim().split("\n");
    buffer.clear();

    for (String message : messages) {
      if (message.equals(IOUtil.BEGIN_MSG)) {
        log("Connection established...");
      } else if (message.equals(IOUtil.END_MSG)) {
        return new PartialResponse(partialResponse.getContent(), true);

      } else {
        partialResponse.appendContent(message);

      }
    }
    return new PartialResponse(partialResponse.getContent(), false);
  }

  private static void log(final String msg) {
    System.out.println("[" + System.currentTimeMillis() + "] " + Thread.currentThread().getName() + " - " + msg);
  }

  public static void main(final String[] args) throws Exception {
    final Integer clients = 50;
    final Selector selector = Selector.open();
    final CountDownLatch counter = new CountDownLatch(clients);

    for (Integer client = 0 ; client < clients ; client++) {
      new ClientNonBlocking(new InetSocketAddress(8080), selector).connect();
    }

    readNonBlocking(selector, counter, context -> {
      final long endOfExecution = System.currentTimeMillis();
      final long executionTime = endOfExecution - context.getBeganAt();
      log("(" + executionTime + " ms) ");

    });
  }

}
