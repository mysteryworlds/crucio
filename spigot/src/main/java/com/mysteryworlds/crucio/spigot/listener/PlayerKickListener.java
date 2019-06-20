package com.mysteryworlds.crucio.spigot.listener;

import com.mysteryworlds.crucio.api.service.LanguageService;
import com.mysteryworlds.crucio.spigot.service.language.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

import javax.inject.Inject;

public class PlayerKickListener implements Listener {

    private final LanguageService languageService;

    @Inject
    public PlayerKickListener(LanguageService languageService) {
        this.languageService = languageService;
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {

        // Obtain player
        Player player = event.getPlayer();

        // Set leave message
        String reason = event.getReason();
        String displayName = player.getDisplayName();
        String messagePrefix = languageService.translate(Messages.KEY_MESSAGE_PREFIX);
        String kickLeaveMessage = languageService.translate(Messages.KEY_MESSAGE_KICK_LEAVE, displayName, reason);
        event.setLeaveMessage(messagePrefix + kickLeaveMessage);
    }
}
