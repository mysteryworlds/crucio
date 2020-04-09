package com.mysteryworlds.crucio.death;

import com.google.common.base.Functions;
import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public enum DeathCause {
  SUFFOCATION(DamageCause.SUFFOCATION, "death-suffocation"),
  VOID(DamageCause.VOID, "death-void"),
  LAVA(DamageCause.LAVA, "death-lava"),
  FALL(DamageCause.FALL, "death-fall"),
  DROWNING(DamageCause.DROWNING, "death-drowning"),
  FIRE(DamageCause.FIRE, "death-fire"),
  STARVATION(DamageCause.STARVATION, "death-starvation"),
  LIGHTNING(DamageCause.LIGHTNING, "death-lightning"),
  POISON(DamageCause.POISON, "death-poison"),
  WITHER(DamageCause.WITHER, "death-wither"),
  PROJECTILE(DamageCause.PROJECTILE, "death-projectile"),
  FALLING_BLOCK(DamageCause.FALLING_BLOCK, "death-falling-block"),
  MAGIC(DamageCause.MAGIC, "death-magic"),
  SUICIDE(DamageCause.SUICIDE, "death-suicide"),
  THORNS(DamageCause.THORNS, "death-thorns"),
  CRAMMING(DamageCause.CRAMMING, "death-cramming"),
  DRAGON_BREATH(DamageCause.DRAGON_BREATH, "death-dragon-breath"),
  CUSTOM(DamageCause.CUSTOM, "death-custom"),
  DRYOUT(DamageCause.DRYOUT, "death-dryout"),
  HOT_FLOOR(DamageCause.HOT_FLOOR, "death-hot-floor"),
  FLY_INTO_WALL(DamageCause.FLY_INTO_WALL, "death-fly-into-wall"),
  MELTING(DamageCause.MELTING, "death-melting"),
  FIRE_TICK(DamageCause.FIRE_TICK, "death-fire-tick"),
  CONTACT(
    DamageCause.CONTACT,
    "death-contact",
    DeathCause::formatVictimAndKiller
  ),
  ENTITY_EXPLOSION(
    DamageCause.ENTITY_EXPLOSION,
    "death-entity-explosion"
  ),
  BLOCK_EXPLOSION(
    DamageCause.BLOCK_EXPLOSION,
    "death-block-explosion"
  ),
  ENTITY_ATTACK(
    DamageCause.ENTITY_ATTACK,
    "death-entity-attack",
    DeathCause::formatVictimAndKiller),
  ENTITY_SWEEP_ATTACK(DamageCause.ENTITY_SWEEP_ATTACK,
    "death-entity-sweep-attack",
    DeathCause::formatVictimAndKiller
  );

  private final DamageCause damageCause;
  private final String messageKey;
  private final BiFunction<EntityDeathEvent, String, String> formatter;

  DeathCause(DamageCause damageCause, String messageKey) {
    this(damageCause, messageKey, DeathCause::formatVictim);
  }

  DeathCause(
    DamageCause damageCause,
    String messageKey,
    BiFunction<EntityDeathEvent, String, String> formatter
  ) {
    this.damageCause = damageCause;
    this.messageKey = messageKey;
    this.formatter = formatter;
  }

  public void formatDeathMessage(
    PlayerDeathEvent playerDeath,
    String deathMessage
  ) {
    Preconditions.checkNotNull(playerDeath);
    Preconditions.checkNotNull(deathMessage);
    var formattedMessage = formatter.apply(playerDeath, deathMessage);
    playerDeath.setDeathMessage(formattedMessage);
  }

  public String messageKey() {
    return messageKey;
  }

  private static String formatVictim(
    EntityDeathEvent entityDeath,
    String message
  ) {
    var entity = entityDeath.getEntity();
    return String.format(message, entity.getCustomName());
  }

  private static String formatVictimAndKiller(
    EntityDeathEvent entityDeath,
    String message
  ) {
    var entity = entityDeath.getEntity();
    var killerName = resolveKillerName(entityDeath);
    return String.format(message, entity.getCustomName(), killerName);
  }

  private static String resolveKillerName(EntityDeathEvent entityDeath) {
    var entity = entityDeath.getEntity();
    var killer = entity.getKiller();
    if (killer != null) {
      return killer.getName();
    }
    var lastDamageCause = entity.getLastDamageCause();
    if (lastDamageCause instanceof EntityDamageByEntityEvent) {
      var entityDamageByEntity = (EntityDamageByEntityEvent) lastDamageCause;
      return entityDamageByEntity.getDamager().getName();
    }
    return "N/A";
  }

  private static final Map<DamageCause, DeathCause> CAUSES = Arrays
    .stream(DeathCause.values())
    .collect(Collectors.toMap(
      deathCause -> deathCause.damageCause,
      Functions.identity()
    ));

  public static DeathCause fromPlayer(Player entity) {
    Preconditions.checkNotNull(entity);
    var lastDamageCause = entity.getLastDamageCause();
    var cause = lastDamageCause.getCause();
    return CAUSES.get(cause);
  }
}
