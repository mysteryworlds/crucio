package com.mysteryworlds.crucio;

import com.google.inject.Guice;
import org.bukkit.plugin.java.JavaPlugin;

public final class CrucioApp extends JavaPlugin {
  @Override
  public void onEnable() {
    var module = new CrucioModule(this);
    var injector = Guice.createInjector(module);
    injector.injectMembers(this);
  }
}
