package com.mysteryworlds.crucio;

import com.google.inject.Guice;
import com.mysteryworlds.crucio.cosmetic.PlayerCosmeticTrigger;
import com.mysteryworlds.crucio.gamemode.GameModeChangeTrigger;
import com.mysteryworlds.crucio.gamemode.GameModeCommand;
import javax.inject.Inject;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CrucioApp extends JavaPlugin {
  @Inject
  private PluginManager pluginManager;
  @Inject
  private GameModeCommand gameModeCommand;
  @Inject
  private GameModeChangeTrigger gameModeChangeTrigger;
  @Inject
  private PlayerCosmeticTrigger cosmeticTrigger;

  @Override
  public void onEnable() {
    saveDefaultResources();
    var module = new CrucioModule(this);
    var injector = Guice.createInjector(module);
    injector.injectMembers(this);
    registerFeatures();
  }

  private void saveDefaultResources() {
    saveDefaultConfig();
  }

  private void registerFeatures() {
    registerGameModeTriggerAndCommand();
    registerCosmetic();
  }

  private void registerGameModeTriggerAndCommand() {
    var gamemode = getCommand("gamemode");
    gamemode.setExecutor(gameModeCommand);
    gamemode.setTabCompleter(gameModeCommand);
    pluginManager.registerEvents(gameModeChangeTrigger, this);
  }

  private void registerCosmetic() {
    pluginManager.registerEvents(cosmeticTrigger, this);
  }
}
