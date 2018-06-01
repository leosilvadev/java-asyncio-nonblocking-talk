package com.github.leosilvadev.nonblockingjava.nonblocking.server;

import com.github.leosilvadev.nonblockingjava.nonblocking.Context;
import com.github.leosilvadev.nonblockingjava.nonblocking.handlers.HandlerRegistration;
import com.github.leosilvadev.nonblockingjava.nonblocking.http.HTTPMethod;
import com.github.leosilvadev.nonblockingjava.nonblocking.http.HTTPStatus;
import com.github.leosilvadev.nonblockingjava.nonblocking.request.Request;
import com.github.leosilvadev.nonblockingjava.nonblocking.request.RequestBufferReader;
import com.github.leosilvadev.nonblockingjava.nonblocking.request.RequestBuilder;
import com.github.leosilvadev.nonblockingjava.nonblocking.response.Response;
import com.github.leosilvadev.nonblockingjava.nonblocking.response.ResponseBuilder;
import com.github.leosilvadev.nonblockingjava.utils.IOUtil;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * Created by leonardo on 5/26/18.
 */
public class Server {

  private static final Logger logger = LoggerFactory.getLogger(Server.class);

  private final String host;
  private final int port;

  private final List<HandlerRegistration> handlerRegistrations;

  protected Server(final String host, final int port, final List<HandlerRegistration> handlerRegistrations) {
    this.host = host;
    this.port = port;
    this.handlerRegistrations = handlerRegistrations;
  }

  public void start() throws IOException {
    final Selector selector = Selector.open();
    final ServerSocketChannel serverSocket = ServerSocketChannel.open();
    serverSocket.bind(new InetSocketAddress(host, port));
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
          client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ).attach(new Context());

        } else if (key.isReadable() && key.isWritable()) {
          final SocketChannel channel = (SocketChannel) key.channel();
          final List<String> lines = new RequestBufferReader().read(channel);
          final Request request = new RequestBuilder().build(lines);

          final Optional<HandlerRegistration> maybeHandler = handlerRegistrations.stream().filter(r -> {
            final boolean sameMethod = request.getMethod().equals(r.getMethod());
            final boolean samePath = request.getPath().equalsIgnoreCase(r.getPath());
            return sameMethod && samePath;

          }).findFirst();

          if (maybeHandler.isPresent()) {
            maybeHandler.get().getHandler().handle(request)
                .subscribe(response -> {
                  IOUtil.write(channel, response.toString());

                }, ex -> {
                  final Response response = new ResponseBuilder()
                      .withStatus(HTTPStatus.INTERNAL_SERVER_ERROR)
                      .withBody(ex.getMessage())
                      .build();
                  IOUtil.write(channel, response.toString());

                });

          } else {
            final Response response = new ResponseBuilder()
                .withStatus(HTTPStatus.NOT_FOUND)
                .build();
            IOUtil.write(channel, response.toString());
          }

          key.cancel();

        }
        keyIterator.remove();
      }
    }
  }

  public static ServerConfig config() {
    return new ServerConfig();
  }

  public static void main(final String[] args) throws IOException {
    Server.config()
        .withPort(8080)
        .handle(HTTPMethod.GET, "/v1/users", request -> {
          return Single.just(new ResponseBuilder().withStatus(HTTPStatus.OK).withBody("{\"name\":\"JAO\"}").build());
        })
        .build()
        .start();
  }
}
