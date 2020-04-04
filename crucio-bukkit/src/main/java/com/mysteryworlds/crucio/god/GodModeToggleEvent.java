package com.mysteryworlds.crucio.god;

import com.google.common.base.Preconditions;
import com.mysteryworlds.crucio.player.CrucioPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class GodModeToggleEvent extends Event {
  private static final HandlerList HANDLER_LIST = new HandlerList();
  private final CrucioPlayer player;

  private GodModeToggleEvent(CrucioPlayer player) {
    super();
    this.player = player;
  }

  public static GodModeToggleEvent withPlayer(CrucioPlayer player) {
    Preconditions.checkNotNull(player);
    return new GodModeToggleEvent(player);
  }

  public CrucioPlayer player() {
    return player;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }
}
