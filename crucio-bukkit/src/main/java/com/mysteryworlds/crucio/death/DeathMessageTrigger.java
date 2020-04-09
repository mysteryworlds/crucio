package com.mysteryworlds.crucio.death;

import com.mysteryworlds.crucio.i18n.I18n;
import javax.inject.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public final class DeathMessageTrigger implements Listener {
  private final I18n i18n;

  @Inject
  private DeathMessageTrigger(I18n i18n) {
    this.i18n = i18n;
  }

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent playerDeath) {
    var entity = playerDeath.getEntity();
    var deathCause = DeathCause.fromPlayer(entity);
    System.out.println("Resolved death cause: " + deathCause);
    var messageKey = deathCause.messageKey();
    var deathMessage = i18n.translatePrefixedMessage(messageKey);
    deathCause.formatDeathMessage(playerDeath, deathMessage);
  }
}
