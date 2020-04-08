package com.mysteryworlds.crucio.warp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@Singleton
public final class WarpRepository {
  private final Map<String, Warp> warps = Maps.newHashMap();
  private final ObjectMapper objectMapper;
  private final Path warpsPath;

  @Inject
  private WarpRepository(
    ObjectMapper objectMapper,
    @Named("warpsPath") Path warpsPath
  ) {
    this.objectMapper = objectMapper;
    this.warpsPath = warpsPath;
  }

  public Collection<Warp> findAll() {
    return List.copyOf(warps.values());
  }

  public Optional<Warp> find(String name) {
    Preconditions.checkNotNull(name);
    return Optional.ofNullable(warps.get(name));
  }

  public void loadAll() {
    var warpConfig = readWarpConfig();
    for (WarpModel warp : warpConfig.getWarps()) {
      warps.put(warp.getName(), warp.toWarp());
    }
  }

  private WarpConfig readWarpConfig() {
    try {
      return objectMapper.readValue(
        warpsPath.toFile(),
        WarpConfig.class
      );
    } catch (IOException e) {
      e.printStackTrace();
      return new WarpConfig(List.of());
    }
  }

  public void saveAll() {
    var warpConfig = buildWarpConfig();
    writeWarpConfig(warpConfig);
  }
  private WarpConfig buildWarpConfig() {
    return new WarpConfig(
      warps.values().stream()
        .map(WarpModel::fromWarp)
        .collect(Collectors.toList())
    );
  }

  private void writeWarpConfig(WarpConfig warpConfig) {
    try {
      objectMapper.writeValue(warpsPath.toFile(), warpConfig);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void save(Warp warp) {
    Preconditions.checkNotNull(warp);
    warps.put(warp.name(), warp);
  }

  public void remove(String warpName) {
    Preconditions.checkNotNull(warpName);
    warps.remove(warpName);
  }

  static final class WarpConfig {
    private List<WarpModel> warps = new ArrayList<>();

    public WarpConfig() {
    }

    public WarpConfig(List<WarpModel> warps) {
      this.warps = warps;
    }

    public List<WarpModel> getWarps() {
      return warps;
    }

    public void setWarps(List<WarpModel> warps) {
      this.warps.addAll(warps);
    }
  }

  static final class WarpModel {
    private String name;
    private String worldName;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public WarpModel() {
    }

    public WarpModel(
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

    public Warp toWarp() {
      return Warp.of(
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

    public static WarpModel fromWarp(Warp warp) {
      Preconditions.checkNotNull(warp);
      var location = warp.location();
      return new WarpModel(
        warp.name(),
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
