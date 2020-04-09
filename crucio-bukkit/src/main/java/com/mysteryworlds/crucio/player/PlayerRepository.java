package com.mysteryworlds.crucio.player;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.mysteryworlds.crucio.player.CrucioPlayer.Home;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@Singleton
public final class PlayerRepository {
  private final Map<UUID, CrucioPlayer> players = Maps.newConcurrentMap();
  private final Gson gson;
  private final Path usersPath;

  @Inject
  private PlayerRepository(
    Gson gson,
    @Named("usersPath") Path usersPath
  ) {
    this.gson = gson;
    this.usersPath = usersPath;
  }

  public CrucioPlayer findOrCreatePlayer(UUID uniqueId) {
    Preconditions.checkNotNull(uniqueId);
    return players.computeIfAbsent(uniqueId, this::createPlayer);
  }

  private CrucioPlayer createPlayer(UUID uuid) {
    return CrucioPlayer.create(uuid);
  }

  public Optional<CrucioPlayer> findPlayer(String name) {
    Preconditions.checkNotNull(name);
    return players.values().stream()
      .filter(player -> player.name().equals(name))
      .findFirst();
  }

  public void loadAll() {
    try (var paths = Files.newDirectoryStream(usersPath)) {
      for (Path path : paths) {
        String content;
        try {
          content = Files.readString(path);
        } catch (IOException e) {
          e.printStackTrace();
          continue;
        }
        var playerModel = gson.fromJson(content, PlayerModel.class);
        players.put(playerModel.id, playerModel.toCrucioPlayer());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void saveAll() {
    for (CrucioPlayer player : players.values()) {
      var path = usersPath.resolve(player.id().toString() + ".json");
      var content = gson.toJson(PlayerModel.fromCrucioPlayer(player));
      try {
        Files.writeString(path, content);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static final class PlayerModel {
    private UUID id;
    private List<HomeModel> homes;
    private boolean god;

    public PlayerModel() {

    }

    public PlayerModel(UUID id, List<HomeModel> homes, boolean god) {
      this.id = id;
      this.homes = homes;
      this.god = god;
    }

    public List<HomeModel> getHomes() {
      return homes;
    }

    public UUID getId() {
      return id;
    }

    public boolean isGod() {
      return god;
    }

    public void setGod(boolean god) {
      this.god = god;
    }

    public void setHomes(List<HomeModel> homes) {
      this.homes = homes;
    }

    public void setId(UUID id) {
      this.id = id;
    }

    public CrucioPlayer toCrucioPlayer() {
      return CrucioPlayer.of(
        Bukkit.getOfflinePlayer(id),
        homes.stream().map(HomeModel::toHome).collect(Collectors.toSet()),
        god
      );
    }

    public static PlayerModel fromCrucioPlayer(CrucioPlayer player) {
      return new PlayerModel(
        player.id(),
        player.homes().stream().map(HomeModel::fromHome).collect(Collectors.toList()),
        player.god()
      );
    }
  }

  static final class HomeModel {
    private String name;
    private String worldName;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public HomeModel() {
    }

    HomeModel(
      String name,
      String worldName,
      double x,
      double y,
      double z,
      float yaw,
      float pitch
    ) {
      this.name = name;
      this.worldName = worldName;
      this.x = x;
      this.y = y;
      this.z = z;
      this.yaw = yaw;
      this.pitch = pitch;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getWorldName() {
      return worldName;
    }

    public void setWorldName(String worldName) {
      this.worldName = worldName;
    }

    public double getX() {
      return x;
    }

    public void setX(double x) {
      this.x = x;
    }

    public double getY() {
      return y;
    }

    public void setY(double y) {
      this.y = y;
    }

    public double getZ() {
      return z;
    }

    public void setZ(double z) {
      this.z = z;
    }

    public float getYaw() {
      return yaw;
    }

    public void setYaw(float yaw) {
      this.yaw = yaw;
    }

    public float getPitch() {
      return pitch;
    }

    public void setPitch(float pitch) {
      this.pitch = pitch;
    }

    public Home toHome() {
      return Home.create(
        name,
        new Location(
          Bukkit.getWorld(worldName),
          x,
          y,
          z,
          pitch,
          yaw
        )
      );
    }

    public static HomeModel fromHome(Home home) {
      Preconditions.checkNotNull(home);
      var location = home.location();
      return new HomeModel(
        home.name(),
        location.getWorld().getName(),
        location.getX(),
        location.getY(),
        location.getZ(),
        location.getYaw(),
        location.getPitch()
      );
    }
  }
}
