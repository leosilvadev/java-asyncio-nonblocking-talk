package com.github.leosilvadev.core.integration

import com.github.leosilvadev.core.Core
import com.github.leosilvadev.core.config.BlockingConfig
import com.github.leosilvadev.core.config.Configuration
import com.github.leosilvadev.core.config.ServerConfig
import com.github.leosilvadev.core.handlers.Handler
import com.github.leosilvadev.core.server.Server

/**
 * Created by leonardo on 6/2/18.
 */
class Fixture {

  static Server buildTestServer(final List<Handler> handlers) {
    def configuration = new Configuration().with {
      server = new ServerConfig("localhost", 9000)
      blocking = new BlockingConfig(1)
      it
    }
    def core = Core.create(configuration)
    def server = Server.config(core)
  }

}
