package com.mysteryworlds.crucio.spigot.listener;

import com.mysteryworlds.crucio.api.service.LanguageService;
import com.mysteryworlds.crucio.spigot.service.language.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final LanguageService languageService;

    public PlayerQuitListener(LanguageService languageService) {
        this.languageService = languageService;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        // Obtain player
        Player player = event.getPlayer();

        // Set quit message
        String displayName = player.getDisplayName();
        String messagePrefix = languageService.translate(Messages.KEY_MESSAGE_PREFIX);
        String quitMessage = languageService.translate(Messages.KEY_MESSAGE_QUIT, displayName);
        event.setQuitMessage(messagePrefix + quitMessage);
    }
}
