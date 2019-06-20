package com.mysteryworlds.crucio.spigot;

import com.google.inject.Binder;
import com.mysteryworlds.crucio.spigot.listener.PlayerJoinListener;
import com.mysteryworlds.crucio.spigot.listener.PlayerKickListener;
import com.mysteryworlds.crucio.spigot.listener.PlayerQuitListener;
import com.mysteryworlds.crucio.spigot.module.CrucioSpigotModule;
import de.d3adspace.isabelle.spgiot.theresa.IsabelleSpigotExtension;
import de.d3adspace.theresa.lifecycle.annotation.WarmUp;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

public class CrucioSpigotPlugin extends IsabelleSpigotExtension {

    // Services
    @Inject
    private PluginManager pluginManager;

    // Listeners
    @Inject private PlayerJoinListener playerJoinListener;
    @Inject private PlayerQuitListener playerQuitListener;
    @Inject private PlayerKickListener playerKickListener;

    @WarmUp
    public void onStart() {

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

    @PreDestroy
    public void onStop() {

        // Unregister all listeners
        HandlerList.unregisterAll(this);
    }

    @Override
    public void configure(Binder binder) {

        binder.install(new CrucioSpigotModule());
    }
}
