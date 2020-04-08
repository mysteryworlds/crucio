package com.mysteryworlds.crucio.warp;

import com.google.common.base.Preconditions;
import java.util.Objects;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class Warp {
  private static final String PERMISSION_WILDCARD = "crucio.warp.all";
  private static final String PERMISSION_PREFIX = "crucio.warp.";
  private final String name;
  private final Location location;

  private Warp(String name, Location location) {
    this.name = name;
    this.location = location;
  }

  public String name() {
    return name;
  }

  public boolean isPermitted(Player player) {
    return player.hasPermission(permission());
  }

  public void teleport(Player player) {
    Preconditions.checkNotNull(player);
    player.teleport(location);
  }

  private String permission() {
    return PERMISSION_PREFIX + name;
  }

  Location location() {
    return location;
  }

  public static Warp of(
    String name,
    Location location
  ) {
    Preconditions.checkNotNull(name);
    Preconditions.checkNotNull(location);
    return new Warp(name, location);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Warp warp = (Warp) o;
    return name.equals(warp.name) &&
      location.equals(warp.location);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, location);
  }
}
