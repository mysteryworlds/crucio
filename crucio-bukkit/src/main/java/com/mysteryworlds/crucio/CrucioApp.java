package com.mysteryworlds.crucio;

import com.google.inject.Guice;
import com.mysteryworlds.crucio.config.CrucioConfig;
import com.mysteryworlds.crucio.cosmetic.PlayerCosmeticTrigger;
import com.mysteryworlds.crucio.fly.FlyCommand;
import com.mysteryworlds.crucio.gamemode.GameModeChangeTrigger;
import com.mysteryworlds.crucio.gamemode.GameModeCommand;
import com.mysteryworlds.crucio.god.GodModeCommand;
import com.mysteryworlds.crucio.god.GodModeTrigger;
import com.mysteryworlds.crucio.player.PlayerRepository;
import com.mysteryworlds.crucio.spawn.SpawnCommand;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.inject.Inject;
import javax.inject.Named;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CrucioApp extends JavaPlugin {
  @Inject
  private PluginManager pluginManager;
  @Inject
  private GameModeCommand gameModeCommand;
  @Inject
  private FlyCommand flyCommand;
  @Inject
  private GameModeChangeTrigger gameModeChangeTrigger;
  @Inject
  private PlayerCosmeticTrigger cosmeticTrigger;
  @Inject
  private GodModeTrigger godModeTrigger;
  @Inject
  private GodModeCommand godModeCommand;
  @Inject
  private SpawnCommand spawnCommand;

  @Inject
  @Named("usersPath")
  private Path usersPath;
  @Inject
  private PlayerRepository playerRepository;

  @Override
  public void onEnable() {
    saveDefaultResources();
    var configuration = loadConfiguration();
    var config = CrucioConfig.fromFileConfig(configuration);
    var module = new CrucioModule(config, this);
    var injector = Guice.createInjector(module);
    injector.injectMembers(this);
    ensureDirectories();
    registerFeatures();
    playerRepository.loadAll();
  }

  private void ensureDirectories() {
    try {
      Files.createDirectories(usersPath);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static final String CONFIG_LOCATION = "config.yml";

  private Configuration loadConfiguration() {
    var configuration = YamlConfiguration.loadConfiguration(
      new File(getDataFolder(), CONFIG_LOCATION)
    );
    configuration.options().copyDefaults(true);
    configuration.addDefaults(getConfig());
    return configuration;
  }

  private void saveDefaultResources() {
    saveDefaultConfig();
  }

  private void registerFeatures() {
    registerGameModeTriggerAndCommand();
    registerFlyCommand();
    registerCosmetic();
    registerGodModeTriggerAndCommand();
    registerSpawnCommand();
  }

  private void registerGameModeTriggerAndCommand() {
    var gamemode = getCommand("gamemode");
    gamemode.setExecutor(gameModeCommand);
    gamemode.setTabCompleter(gameModeCommand);
    pluginManager.registerEvents(gameModeChangeTrigger, this);
  }

  private void registerFlyCommand() {
    var fly = getCommand("fly");
    fly.setExecutor(flyCommand);
    fly.setTabCompleter(flyCommand);
  }

  private void registerCosmetic() {
    pluginManager.registerEvents(cosmeticTrigger, this);
  }

  private void registerGodModeTriggerAndCommand() {
    var godmode = getCommand("godmode");
    godmode.setExecutor(godModeCommand);
    godmode.setTabCompleter(godModeCommand);
    pluginManager.registerEvents(godModeTrigger, this);
  }

  private void registerSpawnCommand() {
    var spawn = getCommand("spawn");
    spawn.setExecutor(spawnCommand);
    spawn.setTabCompleter(spawnCommand);
  }

  @Override
  public void onDisable() {
    playerRepository.saveAll();
  }
}
