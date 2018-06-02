package com.github.leosilvadev.core.server;

import com.github.leosilvadev.core.Core;
import com.github.leosilvadev.core.blocking.Blocking;
import com.github.leosilvadev.core.config.Configuration;
import com.github.leosilvadev.core.handlers.HandlerRegistration;
import com.github.leosilvadev.core.http.HTTPStatus;
import com.github.leosilvadev.core.http.MIMEType;
import com.github.leosilvadev.core.request.Request;
import com.github.leosilvadev.core.request.RequestBufferReader;
import com.github.leosilvadev.core.request.RequestBuilder;
import com.github.leosilvadev.core.response.Responder;
import com.github.leosilvadev.core.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by leonardo on 5/26/18.
 */
public final class Server {

  private static final Logger logger = LoggerFactory.getLogger(Server.class);

  private final Core core;

  private final Configuration configuration;

  private final List<HandlerRegistration> handlerRegistrations;

  protected Server(final Core core, final Configuration configuration, final List<HandlerRegistration> handlerRegistrations) {
    this.configuration = configuration;
    this.core = core;
    this.handlerRegistrations = handlerRegistrations;
  }

  public void start() {
    try {
      final Selector selector = Selector.open();
      final ServerSocketChannel serverSocket = ServerSocketChannel.open();
      serverSocket.bind(new InetSocketAddress(
              configuration.getServer().getHost(),
              configuration.getServer().getPort())
      );
      serverSocket.configureBlocking(false);
      serverSocket.register(selector, SelectionKey.OP_ACCEPT);
      logger.info("Server listening at port {}", configuration.getServer().getPort());

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
            client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);

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
                    new Responder(response).respond(channel);

                  }, ex -> {
                    final Response response = Response.builder()
                        .withStatus(HTTPStatus.INTERNAL_SERVER_ERROR)
                        .withBody(ex.getMessage(), MIMEType.TEXT_PLAIN)
                        .build();

                    new Responder(response).respond(channel);

                  });

            } else {
              final Response response = Response.builder()
                  .withStatus(HTTPStatus.NOT_FOUND)
                  .build();
              new Responder(response).respond(channel);
            }

            key.cancel();

          }
          keyIterator.remove();
        }
      }
    } catch (final IOException ex) {
      logger.error("Unhandled IO Exception in main execution.", ex);

    }
  }

  public static ServerConfigurer config(final Core core, final Configuration configuration) {
    return new ServerConfigurer(core, configuration);
  }

  public static void main(final String[] args) throws IOException {
    final Core core = new Core();
    final Configuration configuration = Configuration.load("application.yml");
    final Blocking blocking = new Blocking(configuration);

    Server.config(core, configuration)
        .handleGet("/v1/users", request ->
          blocking.execute(() -> {
            try {
              Thread.sleep(2000);
              return "{\"name\":\"JAO\"}";
            } catch (InterruptedException e) {
              throw new RuntimeException(e);
            }
          }).map(json -> Response.ok().json(json).build())
        )
        .build()
        .start();
  }
}
