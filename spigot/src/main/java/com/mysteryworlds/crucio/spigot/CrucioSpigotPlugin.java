package com.mysteryworlds.crucio.spigot;

import com.mysteryworlds.crucio.api.service.LanguageService;
import com.mysteryworlds.crucio.spigot.listener.PlayerChatListener;
import com.mysteryworlds.crucio.spigot.listener.PlayerJoinListener;
import com.mysteryworlds.crucio.spigot.listener.PlayerKickListener;
import com.mysteryworlds.crucio.spigot.listener.PlayerQuitListener;
import com.mysteryworlds.crucio.spigot.service.language.LanguageServiceImpl;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class CrucioSpigotPlugin extends JavaPlugin {

    // Services
    private PluginManager pluginManager;

    // Vault
    private Chat chat;

    // Listeners
    private PlayerJoinListener playerJoinListener;
    private PlayerQuitListener playerQuitListener;
    private PlayerKickListener playerKickListener;
    private PlayerChatListener playerChatListener;

    @Override
    public void onEnable() {

        pluginManager = Bukkit.getPluginManager();
        saveDefaultConfig();

        // Config
        getConfig().options().copyDefaults(true);

        // Vault
        RegisteredServiceProvider<Chat> registration = Bukkit.getServicesManager().getRegistration(Chat.class);
        if (registration == null) {
            getLogger().severe("Disabling plugin. Couldn't find chat provider.");
            pluginManager.disablePlugin(this);
            return;
        }

        chat = registration.getProvider();

        // Language
        LanguageService languageService = new LanguageServiceImpl(getConfig());

        playerJoinListener = new PlayerJoinListener(chat, languageService);
        playerQuitListener = new PlayerQuitListener(languageService);
        playerKickListener = new PlayerKickListener(languageService);
        playerChatListener = new PlayerChatListener();

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
        pluginManager.registerEvents(playerChatListener, this);
    }
}
