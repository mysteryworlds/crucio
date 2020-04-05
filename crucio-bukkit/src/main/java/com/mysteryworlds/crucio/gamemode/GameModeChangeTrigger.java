package com.mysteryworlds.crucio.gamemode;

import com.mysteryworlds.crucio.i18n.I18n;
import javax.inject.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public final class GameModeChangeTrigger implements Listener {
  private final I18n i18n;

  @Inject
  private GameModeChangeTrigger(I18n i18n) {
    this.i18n = i18n;
  }

  @EventHandler
  public void onGameModeChange(PlayerGameModeChangeEvent gameModeChange) {
    var gameMode = GameMode.fromBukkitMode(gameModeChange.getNewGameMode());
    i18n.sendPrefixedMessage(
      gameModeChange.getPlayer(),
      "gamemode-changed",
      gameMode.name()
    );
  }
}
