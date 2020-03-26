package com.mysteryworlds.crucio;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import java.util.logging.Logger;
import javax.inject.Singleton;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.scoreboard.ScoreboardManager;

public final class SpigotModule extends AbstractModule {
  private SpigotModule() {
  }

  @Provides
  @Singleton
  Server provideServer() {
    return Bukkit.getServer();
  }

  @Provides
  @Singleton
  PluginManager providePluginManager(Server server) {
    return server.getPluginManager();
  }

  @Provides
  @Singleton
  ServicesManager provideServicesManager(Server server) {
    return server.getServicesManager();
  }

  @Provides
  @Singleton
  ScoreboardManager provideScoreboardManager(Server server) {
    return server.getScoreboardManager();
  }

  @Provides
  @Singleton
  @ServerLogger
  Logger provideServerLogger(Server server) {
    return server.getLogger();
  }

  public static SpigotModule create() {
    return new SpigotModule();
  }
}
