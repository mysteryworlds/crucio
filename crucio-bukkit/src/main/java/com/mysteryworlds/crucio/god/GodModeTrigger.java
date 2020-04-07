package com.mysteryworlds.crucio.god;

import com.mysteryworlds.crucio.i18n.I18n;
import com.mysteryworlds.crucio.player.PlayerRepository;
import javax.inject.Inject;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public final class GodModeTrigger implements Listener {
  private static final double ZERO_DAMAGE = 0.0D;
  private final I18n i18n;
  private final PlayerRepository playerRepository;

  @Inject
  private GodModeTrigger(
    I18n i18n,
    PlayerRepository playerRepository
  ) {
    this.i18n = i18n;
    this.playerRepository = playerRepository;
  }

  @EventHandler(ignoreCancelled = true)
  public void onPlayerDamage(EntityDamageEvent entityDamage) {
    if (!(entityDamage.getEntity() instanceof Player)) {
      return;
    }
    var player = playerRepository.findOrCreatePlayer(
      entityDamage.getEntity().getUniqueId()
    );
    if (player.god()) {
      entityDamage.setCancelled(true);
      entityDamage.setDamage(ZERO_DAMAGE);
    }
  }

  @EventHandler
  public void onHunger(FoodLevelChangeEvent foodLevelChange) {
    if (!(foodLevelChange.getEntity() instanceof Player)) {
      return;
    }
    var player = playerRepository.findOrCreatePlayer(
      foodLevelChange.getEntity().getUniqueId()
    );
    if (player.god()) {
      foodLevelChange.setCancelled(true);
    }
  }

  @EventHandler
  public void onGodModeToggle(GodModeToggleEvent godModeToggle) {
    var player = godModeToggle.player();
    if (player.isOnline()) {
      i18n.sendPrefixedMessage(
        player.asBukkitPlayer(),
        player.god() ? "godmode-enabled" : "godmode-disabled"
      );
    }
  }
}
