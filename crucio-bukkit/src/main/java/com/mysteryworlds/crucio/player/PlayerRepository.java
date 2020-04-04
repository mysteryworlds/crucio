package com.mysteryworlds.crucio.player;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.bukkit.Bukkit;

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
    private boolean god;

    public PlayerModel() {

    }

    public PlayerModel(UUID id, boolean god) {
      this.id = id;
      this.god = god;
    }

    public CrucioPlayer toCrucioPlayer() {
      return CrucioPlayer.of(Bukkit.getOfflinePlayer(id), god);
    }

    public static PlayerModel fromCrucioPlayer(CrucioPlayer player) {
      return new PlayerModel(player.id(), player.god());
    }
  }
}
