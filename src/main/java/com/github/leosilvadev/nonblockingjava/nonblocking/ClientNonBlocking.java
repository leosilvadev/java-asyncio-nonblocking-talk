package com.github.leosilvadev.nonblockingjava.nonblocking;

import com.github.leosilvadev.nonblockingjava.utils.IOUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by leonardo on 4/19/18.
 */
public class ClientNonBlocking {

  public static class Context {

    private final String id;
    private final long beganAt;
    private final JsonPartialResponse response;

    public Context(final String id, final long beganAt, final JsonPartialResponse partialResponse) {
      this.id = id;
      this.beganAt = beganAt;
      this.response = partialResponse;
    }

    public Context() {
      this(UUID.randomUUID().toString(), System.currentTimeMillis(), new JsonPartialResponse());
    }

    public Context copy(final JsonPartialResponse partialResponse) {
      return new Context(id, beganAt, partialResponse);
    }

    public String getId() {
      return id;
    }

    public long getBeganAt() {
      return beganAt;
    }

    public String getResponse() {
      return response.getContent().toString();
    }

    public JsonPartialResponse getPartialResponse() {
      return response;
    }
  }

  private static final Set<Context> contexts = new HashSet<>();

  public static void main(final String[] args) throws Exception {
    final Integer clients = 500;
    final Selector selector = Selector.open();
    final CountDownLatch counter = new CountDownLatch(clients);

    for (Integer client = 0 ; client < clients ; client++) {
      connect(new InetSocketAddress(8080), selector);
    }

    readNonBlocking(selector, counter);
  }

  public static void connect(final SocketAddress address, final Selector selector) throws IOException, InterruptedException, ExecutionException, TimeoutException {
    final Context context = new Context();
    contexts.add(context);

    final SocketChannel channel = SocketChannel.open();
    channel.configureBlocking(false);
    channel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ).attach(context);
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
        channel.configureBlocking(false);

        if (key.isConnectable()) {
          channel.finishConnect();

        } else if (key.isReadable()) {
          final Context context = (Context) key.attachment();
          final JsonPartialResponse partialResponse = readData(channel, context.getPartialResponse(), counter);
          if (partialResponse.isComplete()) {
            final long endOfExecution = System.currentTimeMillis();
            final long executionTime = endOfExecution - context.getBeganAt();
            log("(" + executionTime + " ms) ");
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

  private static JsonPartialResponse readData(final SocketChannel channel, final JsonPartialResponse jsonPartialResponse, final CountDownLatch counter) throws IOException {
    final ByteBuffer buffer = ByteBuffer.allocate(256);
    channel.read(buffer);

    final String[] messages = new String(buffer.array()).trim().split("\n");
    buffer.clear();

    for (String message : messages) {
      if (message.equals(IOUtil.BEGIN_MSG)) {
      } else if (message.equals(IOUtil.END_MSG)) {
        counter.countDown();
        return new JsonPartialResponse(jsonPartialResponse.getContent(), true);

      } else {
        jsonPartialResponse.appendContent(message);

      }
    }
    return new JsonPartialResponse(jsonPartialResponse.getContent(), false);
  }

  private static void log(final String msg) {
    System.out.println("[" + System.currentTimeMillis() + "] " + Thread.currentThread().getName() + " - " + msg);
  }

  public static class JsonPartialResponse {

    private final StringBuffer content;
    private final boolean complete;

    public JsonPartialResponse(final StringBuffer content, final boolean complete) {
      this.content = content;
      this.complete = complete;
    }

    public JsonPartialResponse() {
      this.content = new StringBuffer();
      this.complete = false;
    }

    public StringBuffer getContent() {
      return content;
    }

    public boolean isComplete() {
      return complete;
    }

    public void appendContent(final String text) {
      this.content.append(text);
    }
  }

}
