package com.mysteryworlds.crucio.fly;

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

public final class FlyCommand implements CommandExecutor, TabCompleter {
  private final I18n i18n;

  @Inject
  private FlyCommand(I18n i18n) {
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
        return toggleFlight(command, sender);
      }
      case 1: {
        return toggleFlightOther(command, sender, args[0]);
      }
    }
    return false;
  }

  private boolean toggleFlight(
    Command command,
    CommandSender sender
  ) {
    if (!sender.hasPermission("crucio.command.fly")) {
      sender.sendMessage(command.getPermissionMessage());
      return true;
    }
    if (!(sender instanceof Player)) {
      sender.sendMessage(i18n.translatePrefixedMessage("command-player-only"));
      return true;
    }
    var player = (Player) sender;
    boolean desiredState = !player.getAllowFlight();
    player.setAllowFlight(desiredState);
    sender.sendMessage(i18n.translatePrefixedMessage(desiredState ? "fly-enabled" : "fly-disabled"));
    return true;
  }

  private boolean toggleFlightOther(
    Command command,
    CommandSender sender,
    String targetName
  ) {
    if (!sender.hasPermission("crucio.command.fly.other")) {
      sender.sendMessage(command.getPermissionMessage());
      return true;
    }
    var target = Bukkit.getPlayerExact(targetName);
    if (target == null) {
      sender.sendMessage(i18n.translatePrefixedMessage("command-player-not-found"));
      return true;
    }
    boolean desiredState = !target.getAllowFlight();
    sender.sendMessage(i18n.translatePrefixedMessage(desiredState ? "fly-enabled-other" : "fly-disabled-other", targetName));
    i18n.sendPrefixedMessage(target, desiredState ? "fly-enabled" : "fly-disabled");
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
