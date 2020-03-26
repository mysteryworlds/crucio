package com.mysteryworlds.crucio;

import com.google.inject.AbstractModule;

public final class CrucioModule extends AbstractModule {
  private final CrucioApp crucioApp;

  CrucioModule(CrucioApp crucioApp) {
    this.crucioApp = crucioApp;
  }

  @Override
  protected void configure() {
    install(SpigotModule.create());
    install(PluginModule.forPlugin(crucioApp));
  }
}
