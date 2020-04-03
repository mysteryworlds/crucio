package com.mysteryworlds.crucio;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mysteryworlds.crucio.config.CrucioConfig;
import com.mysteryworlds.crucio.i18n.I18n;
import javax.inject.Singleton;

public final class CrucioModule extends AbstractModule {
  private final CrucioConfig config;
  private final CrucioApp crucioApp;

  CrucioModule(CrucioConfig config,
    CrucioApp crucioApp) {
    this.config = config;
    this.crucioApp = crucioApp;
  }

  @Override
  protected void configure() {
    install(SpigotModule.create());
    install(PluginModule.forPlugin(crucioApp));
  }

  @Provides
  @Singleton
  I18n provideI18n() {
    return I18n.withMessages(config.messages());
  }
}
