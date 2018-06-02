package com.github.leosilvadev.core;

import com.github.leosilvadev.core.blocking.Blocking;
import com.github.leosilvadev.core.config.Configuration;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import java.net.URI;

/**
 * Created by leonardo on 6/2/18.
 */
public class CoreModule extends AbstractModule {

  private final URI configPath;

  public CoreModule(final URI configPath) {
    this.configPath = configPath;
  }

  @Override
  protected void configure() {
    bind(Configuration.class).toProvider(() -> Configuration.load(configPath)).in(Scopes.SINGLETON);
    bind(Blocking.class).in(Scopes.SINGLETON);
  }
}
