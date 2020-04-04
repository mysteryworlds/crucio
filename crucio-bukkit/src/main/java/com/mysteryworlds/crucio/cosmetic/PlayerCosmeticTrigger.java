package com.mysteryworlds.crucio.cosmetic;

import com.mysteryworlds.crucio.i18n.I18n;
import javax.inject.Inject;
import javax.inject.Named;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerCosmeticTrigger implements Listener {
  private final I18n i18n;
  private final String chatFormat;

  @Inject
  private PlayerCosmeticTrigger(
    I18n i18n,
    @Named("chatFormat") String chatFormat
  ) {
    this.i18n = i18n;
    this.chatFormat = chatFormat;
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent playerJoin) {
    setupJoinMessage(playerJoin);
  }

  private void setupJoinMessage(PlayerJoinEvent playerJoin) {
    var message = i18n.translatePrefixedMessage(
      "player-join",
      playerJoin.getPlayer().getDisplayName()
    );
    playerJoin.setJoinMessage(message);
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent playerQuit) {
    setupQuitMessage(playerQuit);
  }

  private void setupQuitMessage(PlayerQuitEvent playerQuit) {
    var message = i18n.translatePrefixedMessage(
      "player-quit",
      playerQuit.getPlayer().getDisplayName()
    );
    playerQuit.setQuitMessage(message);
  }

  @EventHandler
  public void onPlayerChat(AsyncPlayerChatEvent chat) {
    chat.setFormat(chatFormat);
  }
}
