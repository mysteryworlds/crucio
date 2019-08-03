package com.mysteryworlds.crucio.api.service;

import com.google.common.util.concurrent.ListenableFuture;
import java.util.UUID;

public interface PlayerService {

  /**
   * Get the current name of the player with the given unique id.
   *
   * @param uniqueId The unique id.
   * @return The name of the player.
   */
  String getNameSync(UUID uniqueId);

  /**
   * Get the name of the player with the given unique id asynchronously.
   *
   * @param uniqueId The unique id of the player.
   * @return The future of the name.
   */
  ListenableFuture<String> getName(UUID uniqueId);

  /**
   * Get the unique id of the player with the given name.
   *
   * @param playerName The name of the player.
   * @return The unique id.
   */
  UUID getUniqueIdSync(String playerName);

  /**
   * Get the unique id of the player with the given name asynchronously.
   *
   * @param playerName The name of the player.
   * @return The future of the unique id.
   */
  ListenableFuture<UUID> getUniqueId(String playerName);
}
