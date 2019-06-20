package com.mysteryworlds.crucio.spigot.listener;

import com.mysteryworlds.crucio.api.service.LanguageService;
import com.mysteryworlds.crucio.spigot.service.language.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.inject.Inject;

public class PlayerJoinListener implements Listener {

    private final LanguageService languageService;

    @Inject
    public PlayerJoinListener(LanguageService languageService) {
        this.languageService = languageService;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        // Obtain player
        Player player = event.getPlayer();

        // Set join message
        String displayName = player.getDisplayName();
        String messagePrefix = languageService.translate(Messages.KEY_MESSAGE_PREFIX);
        String joinMessage = languageService.translate(Messages.KEY_MESSAGE_JOIN, displayName);
        event.setJoinMessage(messagePrefix + joinMessage);
    }
}
