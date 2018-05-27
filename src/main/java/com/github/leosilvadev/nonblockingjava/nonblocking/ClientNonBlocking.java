package com.github.leosilvadev.nonblockingjava.nonblocking;

import com.github.leosilvadev.nonblockingjava.utils.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Created by leonardo on 4/19/18.
 */
public class ClientNonBlocking {

  private static final Logger logger = LoggerFactory.getLogger(ClientNonBlocking.class);

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
    final ByteBuffer buffer = ByteBuffer.allocate(4096);
    channel.read(buffer);

    final String[] messages = new String(buffer.array()).trim().split("\n");
    buffer.clear();

    for (String message : messages) {
      if (message.equals(IOUtil.BEGIN_MSG)) {
      } else if (message.equals(IOUtil.END_MSG)) {
        return new PartialResponse(partialResponse.getContent(), true);

      } else {
        partialResponse.appendContent(message);

      }
    }
    return new PartialResponse(partialResponse.getContent(), false);
  }

  public static void main(final String[] args) throws Exception {
    final Integer clients = 400;
    final Selector selector = Selector.open();
    final CountDownLatch counter = new CountDownLatch(clients);

    for (Integer client = 0 ; client < clients ; client++) {
      new ClientNonBlocking(new InetSocketAddress(8080), selector).connect();
    }

    final AtomicInteger id = new AtomicInteger(0);

    readNonBlocking(selector, counter, context -> {
      final long endOfExecution = System.currentTimeMillis();
      final long executionTime = endOfExecution - context.getBeganAt();
      logger.info("[{}] ({} ms) ", id.incrementAndGet(), executionTime);

    });
  }

}
