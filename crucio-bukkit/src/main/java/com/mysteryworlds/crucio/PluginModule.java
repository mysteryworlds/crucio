package com.mysteryworlds.crucio;

import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import java.util.logging.Logger;
import javax.inject.Singleton;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public final class PluginModule extends AbstractModule {
  private final Plugin plugin;

  private PluginModule(Plugin plugin) {
    this.plugin = plugin;
  }

  @Override
  protected void configure() {
  }

  @Provides
  @Singleton
  @PluginLogger
  Logger providePluginLogger() {
    return plugin.getLogger();
  }

  @Provides
  @Singleton
  PluginDescriptionFile providePluginDescription() {
    return plugin.getDescription();
  }

  public static PluginModule forPlugin(Plugin plugin) {
    Preconditions.checkNotNull(plugin);
    return new PluginModule(plugin);
  }
}
