package com.github.leosilvadev.nonblockingjava.nonblocking;

import com.github.leosilvadev.nonblockingjava.nonblocking.request.Request;
import com.github.leosilvadev.nonblockingjava.nonblocking.request.RequestBufferReader;
import com.github.leosilvadev.nonblockingjava.nonblocking.request.RequestBuilder;
import com.github.leosilvadev.nonblockingjava.nonblocking.request.RequestDefinition;
import com.github.leosilvadev.nonblockingjava.nonblocking.services.UserServiceNonBlocking;
import com.github.leosilvadev.nonblockingjava.utils.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * Created by leonardo on 5/26/18.
 */
public class ServerNonBlocking {

  private static final Logger logger = LoggerFactory.getLogger(ServerNonBlocking.class);

  private final UserServiceNonBlocking userService;

  public ServerNonBlocking() {
    this.userService = new UserServiceNonBlocking();
  }

  public void start(final int port) throws IOException {

    // Selector is only one instance, that will manage/monitor all the IO channels
    final Selector selector = Selector.open();

    // Only one Server Socket channel. This channel will just wait for connections and accept then
    // the socket connection itself is another channel
    final ServerSocketChannel serverSocket = ServerSocketChannel.open();
    serverSocket.bind(new InetSocketAddress(port));

    // This is required, only non-blocking channels can be registered in a selector
    serverSocket.configureBlocking(false);
    serverSocket.register(selector, SelectionKey.OP_ACCEPT);
    logger.info("Server listening at port {}", port);

    while (true) {
      // Select all the channels that are ready for some IO operation. It blocks until some is ready
      selector.select();

      // Get all the channels (by keys, that represent the channels) that are ready for IO operation
      final Set<SelectionKey> selectedKeys = selector.selectedKeys();
      final Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
      while (keyIterator.hasNext()) {
        final SelectionKey key = keyIterator.next();

        // If the channel is acceptable (only Socket Server Channel was registered for ACCEPT operation)
        // then we get the Socket Server Channel and accept the connection. This will return a Socket Channel (that represents the client connection)
        // we then need to set this channel as non-blocking and register it to the selector for WRITE IO operation
        // we also attache the current time in the key, so we can check how long it took to finish the operation after the connection become established
        if (key.isAcceptable()) {
          final ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
          final SocketChannel client = serverSocketChannel.accept();
          client.configureBlocking(false);
          client.register(selector, SelectionKey.OP_WRITE).attach(new Context());

          // If the channel is writable (only Socket Channel was registered for WRITE operation)
          // then we get the users and write then through the socket
        } else if (key.isWritable()) {
          handleGetUsers(key);
          key.cancel();

        }
        keyIterator.remove();
      }
    }
  }

  private static String readData(final SocketChannel channel) throws IOException {
    final List<String> lines = new RequestBufferReader().read(channel);
    final Request request = new RequestBuilder().build(lines);


    return "";
  }

  private void handleGetUsers(final SelectionKey key) {
    userService.getUsersJson(users -> write(users, key));
  }

  private void write(final String message, final SelectionKey key) {
    final SocketChannel client = (SocketChannel) key.channel();
    IOUtil.write(client, IOUtil.BEGIN_MSG);
    IOUtil.write(client, message);
    IOUtil.write(client, IOUtil.END_MSG);

    final Long beganAt = (Long) key.attachment();
    final Long finishedAt = System.currentTimeMillis();
    final Long executionTime = finishedAt - beganAt;

    logger.info("Writing users... executed and sent in {} ms", executionTime);
  }

  public static void main(final String[] args) throws IOException {
    final ServerNonBlocking server = new ServerNonBlocking();
    server.start(8080);
  }
}
