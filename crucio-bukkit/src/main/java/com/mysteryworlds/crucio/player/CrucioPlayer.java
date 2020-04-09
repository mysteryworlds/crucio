package com.mysteryworlds.crucio.player;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.mysteryworlds.crucio.god.GodModeToggleEvent;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public final class CrucioPlayer {
  private final OfflinePlayer player;
  private final Set<Home> homes;
  private boolean god;

  private CrucioPlayer(
    OfflinePlayer player,
    Set<Home> homes,
    boolean god
  ) {
    this.player = player;
    this.homes = homes;
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

  public int homeLimit() {
    return -1;
  }

  public Set<Home> homes() {
    return Set.copyOf(homes);
  }

  public void toggleGod() {
    god = !god;
    var event = GodModeToggleEvent.withPlayer(this);
    Bukkit.getPluginManager().callEvent(event);
  }

  public Optional<Home> findHome(String name) {
    return homes.stream().filter(
      home -> home.name.equalsIgnoreCase(name)
    ).findFirst();
  }

  public Home addHome(String name, Location location) {
    Preconditions.checkNotNull(name);
    Preconditions.checkNotNull(location);
    var home = Home.create(name, location);
    homes.add(home);
    return home;
  }

  public boolean removeHome(String name) {
    Preconditions.checkNotNull(name);
    return homes.removeIf(home -> home.name.equals(name));
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
    return new CrucioPlayer(offlinePlayer, Sets.newHashSet(), false);
  }

  public static CrucioPlayer of(
    OfflinePlayer offlinePlayer,
    Collection<Home> homes,
    boolean god
  ) {
    Preconditions.checkNotNull(offlinePlayer);
    return new CrucioPlayer(offlinePlayer, Sets.newHashSet(homes), god);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("player", player)
      .add("god", god)
      .toString();
  }

  public static class Home {
    private final String name;
    private final Location location;

    private Home(String name, Location location) {
      this.name = name;
      this.location = location;
    }

    public boolean teleport(Player player) {
      Preconditions.checkNotNull(player);
      return player.teleport(location);
    }

    public String name() {
      return name;
    }

    Location location() {
      return location;
    }

    static Home create(String name, Location location) {
      Preconditions.checkNotNull(name);
      Preconditions.checkNotNull(location);
      return new Home(name, location);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Home home = (Home) o;
      return name().equals(home.name());
    }

    @Override
    public int hashCode() {
      return Objects.hash(name());
    }
  }
}
