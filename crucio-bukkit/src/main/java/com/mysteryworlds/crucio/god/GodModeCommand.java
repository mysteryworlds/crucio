package com.mysteryworlds.crucio.god;

import com.google.inject.internal.cglib.core.$CodeEmitter;
import com.mysteryworlds.crucio.i18n.I18n;
import com.mysteryworlds.crucio.player.CrucioPlayer;
import com.mysteryworlds.crucio.player.PlayerRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public final class GodModeCommand implements CommandExecutor, TabCompleter {
  private final I18n i18n;
  private final PlayerRepository playerRepository;

  @Inject
  private GodModeCommand(
    I18n i18n,
    PlayerRepository playerRepository
  ) {
    this.i18n = i18n;
    this.playerRepository = playerRepository;
  }

  @Override
  public boolean onCommand(
    CommandSender sender,
    Command command,
    String label,
    String[] args
  ) {
    switch (args.length) {
      case 0: {
        return toggleGodMode(command, sender);
      }
      case 1: {
        return toggleGodModeOther(command, sender, args[0]);
      }
    }
    return false;
  }

  private boolean toggleGodMode(
    Command command,
    CommandSender sender
  ) {
    if (!sender.hasPermission("crucio.command.godmode")) {
      sender.sendMessage(command.getPermissionMessage());
      return false;
    }
    if (!(sender instanceof Player)) {
      sender.sendMessage(i18n.translatePrefixedMessage("command-player-only"));
      return false;
    }
    var bukkitPlayer = (Player) sender;
    var player = playerRepository.findOrCreatePlayer(bukkitPlayer.getUniqueId());
    player.toggleGod();
    return true;
  }

  private boolean toggleGodModeOther(
    Command command,
    CommandSender sender,
    String targetName
  ) {
    if (!sender.hasPermission("crucio.command.godmode.other")) {
      sender.sendMessage(command.getPermissionMessage());
      return false;
    }
    var player = playerRepository.findPlayer(targetName);
    if (player.isEmpty()) {
      sender.sendMessage(i18n.translatePrefixedMessage("command-player-not-found"));
      return true;
    }
    player.get().toggleGod();
    sender.sendMessage(i18n.translatePrefixedMessage(
      player.get().god() ? "command-godmode-changed-other-enabled" : "command-godmode-changed-other-disabled",
      targetName
    ));
    return true;
  }

  @Override
  public List<String> onTabComplete(
    CommandSender sender,
    Command command,
    String alias,
    String[] args
  ) {
    if (args.length == 1) {
      var playerNames = Bukkit.getOnlinePlayers().stream()
        .map(HumanEntity::getName)
        .collect(Collectors.toUnmodifiableList());
      return StringUtil.copyPartialMatches(
        args[0],
        List.copyOf(playerNames),
        new ArrayList<>()
      );
    }
    return List.of();
  }
}
