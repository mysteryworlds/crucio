package com.mysteryworlds.crucio.player;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public final class CrucioPlayer {
  private final OfflinePlayer player;

  private CrucioPlayer(OfflinePlayer player) {
    this.player = player;
  }



  private Player obtainPlayerOrDie() {
    if (!player.isOnline()) {
      throw new IllegalStateException("Can't get online player of offline player");
    }
    return player.getPlayer();
  }
}
