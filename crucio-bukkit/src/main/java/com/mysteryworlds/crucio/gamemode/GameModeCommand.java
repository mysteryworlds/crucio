package com.mysteryworlds.crucio.gamemode;

import com.mysteryworlds.crucio.i18n.I18n;
import java.util.ArrayList;
import java.util.List;
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

public final class GameModeCommand implements CommandExecutor, TabCompleter {
  private final I18n i18n;

  @Inject
  private GameModeCommand(I18n i18n) {
    this.i18n = i18n;
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
        return toggleGameMode(command, sender);
      }
      case 1: {
        return changeGameMode(command, sender, args[0]);
      }
      case 2: {
        return changeGameModeOther(command, sender, args[0], args[1]);
      }
    }
    return false;
  }

  private boolean toggleGameMode(Command command, CommandSender sender) {
    if (!sender.hasPermission("crucio.command.gamemode")) {
      sender.sendMessage(command.getPermissionMessage());
      return true;
    }
    if (!(sender instanceof Player)) {
      sender.sendMessage(i18n.translatePrefixedMessage("command-player-only"));
      return true;
    }
    var player = (Player) sender;
    var gameMode = GameMode.fromPlayer(player);
    gameMode.toggle(player);
    return true;
  }

  private boolean changeGameMode(
    Command command,
    CommandSender sender,
    String gameMode
  ) {
    if (!sender.hasPermission("crucio.command.gamemode")) {
      sender.sendMessage(command.getPermissionMessage());
      return true;
    }
    if (!(sender instanceof Player)) {
      sender.sendMessage(i18n.translatePrefixedMessage("command-player-only"));
      return true;
    }
    var player = (Player) sender;
    var mode = GameMode.fromString(gameMode);
    mode.apply(player);
    return true;
  }

  private boolean changeGameModeOther(
    Command command,
    CommandSender sender,
    String gameMode,
    String targetName
  ) {
    if (!sender.hasPermission("crucio.command.gamemode.other")) {
      sender.sendMessage(command.getPermissionMessage());
      return true;
    }
    var player = Bukkit.getPlayerExact(targetName);
    if (player == null) {
      sender.sendMessage(i18n.translatePrefixedMessage("command-player-not-found"));
      return true;
    }
    var mode = GameMode.fromString(gameMode);
    mode.apply(player);
    sender.sendMessage(i18n.translatePrefixedMessage("command-gamemode-changed-other", targetName, mode.name()));
    return true;
  }

  @Override
  public List<String> onTabComplete(
    CommandSender sender,
    Command command,
    String alias,
    String[] args
  ) {
    switch (args.length) {
      case 1: {
        return StringUtil.copyPartialMatches(
          args[0],
          List.copyOf(GameMode.gameModeAliases()),
          new ArrayList<>()
        );
      }
      case 2: {
        var playerNames = Bukkit.getOnlinePlayers().stream()
          .map(HumanEntity::getName)
          .collect(Collectors.toUnmodifiableList());
        return StringUtil.copyPartialMatches(
          args[1],
          List.copyOf(playerNames),
          new ArrayList<>()
        );
      }
      default:
        return List.of();
    }
  }
}
