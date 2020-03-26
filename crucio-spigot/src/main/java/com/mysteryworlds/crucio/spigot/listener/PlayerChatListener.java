package com.mysteryworlds.crucio.spigot.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

  @EventHandler
  public void onPlayerChat(AsyncPlayerChatEvent event) {

    Player player = event.getPlayer();
    event.setFormat(String.format("%s: %s$s", player.getDisplayName(), ChatColor.WHITE));
  }
}
