package com.github.leosilvadev.nonblockingjava.nonblocking;

import com.github.leosilvadev.nonblockingjava.nonblocking.services.UserServiceNonBlocking;
import com.github.leosilvadev.nonblockingjava.utils.IOUtil;
import com.github.leosilvadev.nonblockingjava.utils.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

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
    final Selector selector = Selector.open();
    final ServerSocketChannel serverSocket = ServerSocketChannel.open();
    serverSocket.bind(new InetSocketAddress(port));
    serverSocket.configureBlocking(false);
    serverSocket.register(selector, SelectionKey.OP_ACCEPT);
    logger.info("Server listening at port {}", port);

    while (true) {
      selector.select();

      final Set<SelectionKey> selectedKeys = selector.selectedKeys();
      final Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
      while (keyIterator.hasNext()) {
        final SelectionKey key = keyIterator.next();
        if (key.isAcceptable()) {
          final ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
          final SocketChannel client = serverSocketChannel.accept();
          client.configureBlocking(false);
          client.register(selector, SelectionKey.OP_WRITE).attach(System.currentTimeMillis());

        } else if (key.isWritable()) {
          handleGetUsers(key);
          key.cancel();

        }
        keyIterator.remove();
      }
    }
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

    logger.info("Writting users... executed and sent in {} ms", executionTime);
  }

  public static void main(final String[] args) throws IOException {
    final ServerNonBlocking server = new ServerNonBlocking();
    server.start(8080);
  }
}
