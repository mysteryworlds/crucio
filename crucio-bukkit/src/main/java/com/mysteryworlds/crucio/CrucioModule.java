package com.mysteryworlds.crucio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mysteryworlds.crucio.config.CrucioConfig;
import com.mysteryworlds.crucio.i18n.I18n;
import java.nio.file.Path;
import javax.inject.Named;
import javax.inject.Singleton;

public final class CrucioModule extends AbstractModule {
  private final CrucioConfig config;
  private final CrucioApp crucioApp;

  CrucioModule(
    CrucioConfig config,
    CrucioApp crucioApp
  ) {
    this.config = config;
    this.crucioApp = crucioApp;
  }

  @Override
  protected void configure() {
    install(SpigotModule.create());
  }

  @Provides
  @Singleton
  I18n provideI18n() {
    return I18n.withMessages(config.messages());
  }

  @Provides
  @Singleton
  @Named("chatFormat")
  String provideChatFormat() {
    return config.chatFormat();
  }

  private static final String USERS_SUB_PATH = "users";

  @Provides
  @Singleton
  @Named("usersPath")
  Path provideUsersPath() {
    return Path.of(crucioApp.getDataFolder().getAbsolutePath(), USERS_SUB_PATH);
  }

  @Provides
  @Singleton
  Gson provideGson() {
    return new GsonBuilder()
      .setLenient()
      .setPrettyPrinting()
      .create();
  }
}
