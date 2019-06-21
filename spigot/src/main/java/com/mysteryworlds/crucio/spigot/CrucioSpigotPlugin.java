package com.mysteryworlds.crucio.spigot;

import com.mysteryworlds.crucio.api.service.LanguageService;
import com.mysteryworlds.crucio.spigot.listener.PlayerJoinListener;
import com.mysteryworlds.crucio.spigot.listener.PlayerKickListener;
import com.mysteryworlds.crucio.spigot.listener.PlayerQuitListener;
import com.mysteryworlds.crucio.spigot.service.language.LanguageServiceImpl;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CrucioSpigotPlugin extends JavaPlugin {

    // Services
    private PluginManager pluginManager;

    // Listeners
    private PlayerJoinListener playerJoinListener;
    private PlayerQuitListener playerQuitListener;
    private PlayerKickListener playerKickListener;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        // Config
        getConfig().options().copyDefaults(true);

        // Language
        LanguageService languageService = new LanguageServiceImpl(getConfig());

        pluginManager = Bukkit.getPluginManager();
        playerJoinListener = new PlayerJoinListener(languageService);
        playerQuitListener = new PlayerQuitListener(languageService);
        playerKickListener = new PlayerKickListener(languageService);

        // Register all listeners
        registerListeners();
    }

    /**
     * Register all listeners.
     */
    private void registerListeners() {

        pluginManager.registerEvents(playerJoinListener, this);
        pluginManager.registerEvents(playerQuitListener, this);
        pluginManager.registerEvents(playerKickListener, this);
    }
}
