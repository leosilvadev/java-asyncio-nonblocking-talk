package com.github.leosilvadev.core.server;

import com.github.leosilvadev.core.Core;
import com.github.leosilvadev.core.config.Configuration;
import com.github.leosilvadev.core.handlers.HandlerRegistration;
import com.github.leosilvadev.core.http.HTTPStatus;
import com.github.leosilvadev.core.http.MIMEType;
import com.github.leosilvadev.core.request.Request;
import com.github.leosilvadev.core.request.RequestBufferReader;
import com.github.leosilvadev.core.request.RequestBuilder;
import com.github.leosilvadev.core.response.Responder;
import com.github.leosilvadev.core.response.Response;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.reactivex.Single;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by leonardo on 5/26/18.
 */
public final class Server {

  private static final Logger logger = LoggerFactory.getLogger(Server.class);

  private ServerSocketChannel serverSocket;
  private Selector selector;

  private final ExecutorService singleExecutor;

  private final Configuration configuration;

  private final List<HandlerRegistration> handlerRegistrations;

  protected Server(final Configuration configuration, final List<HandlerRegistration> handlerRegistrations) {
    logger.info("Creating new Server...");
    this.singleExecutor = Executors.newSingleThreadExecutor(
        new ThreadFactoryBuilder().setNameFormat("core-dispatcher").build()
    );

    this.configuration = configuration;
    this.handlerRegistrations = handlerRegistrations;
  }

  public Single<Server> start() {
    return Single.create(emitter -> {
      try {
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress(
            configuration.getServer().getHost(),
            configuration.getServer().getPort())
        );
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        logger.info("Server listening at port {}", configuration.getServer().getPort());
        emitter.onSuccess(this);
        singleExecutor.execute(() -> {
          try {
            while (selector.isOpen()) {
              selector.select();

              if (!selector.isOpen()) {
                continue;
              }

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
        });

      } catch (final Exception ex) {
        logger.error("Could not start the server.", ex);
        emitter.onError(ex);

      }
    });
  }

  public static ServerConfigurer config(final Core core) {
    return new ServerConfigurer(core, core.getInstance(Configuration.class));
  }

  public static void main(final String[] args) throws IOException {
    final Core core = Core.create("application.yml");

    Server.config(core)
        .handleGet("/v1/users", request ->
            core.blockingExecutor().execute(() -> {
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

  public void shutdown() {
    logger.info("Shutting down the server...");
    this.singleExecutor.shutdown();
    try {
      this.selector.close();
      this.serverSocket.close();

    } catch (final IOException e) {
      logger.error("Error on shutting down the server.", e);

    }
  }
}
