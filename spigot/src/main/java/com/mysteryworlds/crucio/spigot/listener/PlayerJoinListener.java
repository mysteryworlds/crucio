package com.mysteryworlds.crucio.spigot.listener;

import com.mysteryworlds.crucio.api.service.LanguageService;
import com.mysteryworlds.crucio.spigot.service.language.Messages;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final Chat chat;
    private final LanguageService languageService;

    public PlayerJoinListener(Chat chat, LanguageService languageService) {
        this.chat = chat;
        this.languageService = languageService;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        // Obtain player
        Player player = event.getPlayer();

        // Set display name
        String displayName = chat.getPlayerPrefix(player) + player.getDisplayName() + chat.getPlayerSuffix(player);
        player.setDisplayName(displayName);

        // Set join message
        String messagePrefix = languageService.translate(Messages.KEY_MESSAGE_PREFIX);
        String joinMessage = languageService.translate(Messages.KEY_MESSAGE_JOIN, displayName);
        event.setJoinMessage(messagePrefix + joinMessage);
    }
}
