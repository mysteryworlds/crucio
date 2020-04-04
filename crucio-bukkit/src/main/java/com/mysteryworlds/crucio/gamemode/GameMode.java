package com.mysteryworlds.crucio.gamemode;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;

public enum GameMode {
  SURVIVAL(
    org.bukkit.GameMode.SURVIVAL,
    org.bukkit.GameMode.CREATIVE,
    List.of("survival", "0")
  ),
  CREATIVE(
    org.bukkit.GameMode.CREATIVE,
    org.bukkit.GameMode.SURVIVAL,
    List.of("creative", "1")
  ),
  ADVENTURE(
    org.bukkit.GameMode.ADVENTURE,
    org.bukkit.GameMode.SURVIVAL,
    List.of("adventure", "2")
  ),
  SPECTATOR(
    org.bukkit.GameMode.SPECTATOR,
    org.bukkit.GameMode.SURVIVAL,
    List.of("spectator", "3")
  );

  private final org.bukkit.GameMode bukkitMode;
  private final org.bukkit.GameMode toggleMode;
  private final List<String> aliases;

  GameMode(
    org.bukkit.GameMode bukkitMode,
    org.bukkit.GameMode toggleMode,
    List<String> aliases
  ) {
    this.bukkitMode = bukkitMode;
    this.toggleMode = toggleMode;
    this.aliases = aliases;
  }

  public void apply(Player player) {
    Preconditions.checkNotNull(player);
    player.setGameMode(bukkitMode);
  }

  public void toggle(Player player) {
    Preconditions.checkNotNull(player);
    if (player.getGameMode() != bukkitMode) {
      throw new IllegalStateException("Can't toggle if player is in wrong source mode");
    }
    player.setGameMode(toggleMode);
  }

  public static GameMode fromString(String gameMode) {
    Preconditions.checkNotNull(gameMode);
    return Arrays.stream(values())
      .filter(mode -> mode.aliases.contains(gameMode))
      .findFirst()
      .orElse(SURVIVAL);
  }

  public static GameMode fromPlayer(Player player) {
    Preconditions.checkNotNull(player);
    return fromBukkitMode(player.getGameMode());
  }

  public static GameMode fromBukkitMode(org.bukkit.GameMode gameMode) {
    Preconditions.checkNotNull(gameMode);
    return Arrays.stream(values())
      .filter(mode -> mode.bukkitMode == gameMode)
      .findFirst()
      .orElse(SURVIVAL);
  }

  private static final List<String> ALIASES = Arrays.stream(values())
    .flatMap(gameMode -> gameMode.aliases.stream())
    .collect(Collectors.toUnmodifiableList());

  public static List<String> gameModeAliases() {
    return ALIASES;
  }
}
