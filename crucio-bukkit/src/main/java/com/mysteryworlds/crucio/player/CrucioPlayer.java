package com.mysteryworlds.crucio.player;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.mysteryworlds.crucio.god.GodModeToggleEvent;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public final class CrucioPlayer {
  private final OfflinePlayer player;
  private boolean god;

  private CrucioPlayer(OfflinePlayer player, boolean god) {
    this.player = player;
    this.god = god;
  }

  public UUID id() {
    return player.getUniqueId();
  }

  private Player obtainPlayerOrDie() {
    if (!player.isOnline()) {
      throw new IllegalStateException("Can't get online player of offline player");
    }
    return player.getPlayer();
  }

  public boolean god() {
    return god;
  }

  public String name() {
    return player.getName();
  }

  public void toggleGod() {
    god = !god;
    var event = GodModeToggleEvent.withPlayer(this);
    Bukkit.getPluginManager().callEvent(event);
  }

  public boolean isOnline() {
    return player.isOnline();
  }

  public Player asBukkitPlayer() {
    return obtainPlayerOrDie();
  }

  public static CrucioPlayer create(UUID uniqueId) {
    Preconditions.checkNotNull(uniqueId);
    var offlinePlayer = Bukkit.getOfflinePlayer(uniqueId);
    return new CrucioPlayer(offlinePlayer, false);
  }

  public static CrucioPlayer of(OfflinePlayer offlinePlayer, boolean god) {
    Preconditions.checkNotNull(offlinePlayer);
    return new CrucioPlayer(offlinePlayer, god);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("player", player)
      .add("god", god)
      .toString();
  }
}
