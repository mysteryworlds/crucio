package com.mysteryworlds.crucio.warp;

import com.mysteryworlds.crucio.i18n.I18n;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public final class WarpCommand implements CommandExecutor, TabCompleter {
  private final I18n i18n;
  private final WarpRepository warpRepository;

  @Inject
  private WarpCommand(
    I18n i18n,
    WarpRepository warpRepository
  ) {
    this.i18n = i18n;
    this.warpRepository = warpRepository;
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
        return listWarps(sender, command);
      }
      case 1: {
        return warp(sender, command, args[0]);
      }
      case 2: {
        var keyword = args[0];
        if (keyword.equalsIgnoreCase("set")) {
          return createWarp(sender, command, args[1]);
        } else if (keyword.equalsIgnoreCase("delete")) {
          return deleteWarp(sender, command, args[1]);
        }
      }
    }
    return false;
  }


  private boolean listWarps(CommandSender sender, Command command) {
    if (!sender.hasPermission("crucio.command.warp.list")) {
      sender.sendMessage(command.getPermissionMessage());
      return true;
    }
    var warps = warpRepository.findAll();
    listWarps(sender, warps);
    return true;
  }

  private static final String WARP_LIST_PREFIX = ChatColor.GOLD + " - ";

  private void listWarps(CommandSender sender, Collection<Warp> warps) {
    var amount = warps.size();
    sender.sendMessage(i18n.translatePrefixedMessage("warp-list", amount));
    for (var warp : warps) {
      TextComponent component = new TextComponent(WARP_LIST_PREFIX + warp.name());
      component.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText("Teleport")));
      component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/warp " + warp.name()));
      sender.spigot().sendMessage(component);
    }
  }

  private boolean warp(CommandSender sender, Command command, String warpName) {
    if (!sender.hasPermission("crucio.command.warp")) {
      sender.sendMessage(command.getPermissionMessage());
      return true;
    }
    if (!(sender instanceof Player)) {
      sender.sendMessage(i18n.translatePrefixedMessage("command-player-only"));
      return true;
    }
    var warpOptional = warpRepository.find(warpName);
    if (warpOptional.isEmpty()) {
      sender.sendMessage(i18n.translatePrefixedMessage("warp-not-found"));
      return true;
    }
    var warp = warpOptional.get();
    var player = (Player) sender;
    if (!warp.isPermitted(player)) {
      sender.sendMessage(i18n.translatePrefixedMessage("warp-not-allowed"));
      return true;
    }
    warp.teleport(player);
    i18n.sendPrefixedMessage(player, "warp-teleport", warpName);
    return true;
  }

  private boolean createWarp(
    CommandSender sender,
    Command command,
    String warpName
  ) {
    if (!sender.hasPermission("crucio.command.warp.create")) {
      sender.sendMessage(command.getPermissionMessage());
      return true;
    }
    if (!(sender instanceof Player)) {
      sender.sendMessage(i18n.translatePrefixedMessage("command-player-only"));
      return true;
    }
    Player player = (Player) sender;
    var warp = Warp.of(warpName, player.getLocation());
    warpRepository.save(warp);
    i18n.sendPrefixedMessage(player, "warp-created", warp.name());
    return true;
  }

  private boolean deleteWarp(
    CommandSender sender,
    Command command,
    String warpName
  ) {
    if (!sender.hasPermission("crucio.command.warp.delete")) {
      sender.sendMessage(command.getPermissionMessage());
      return true;
    }
    var warpOptional = warpRepository.find(warpName);
    if (warpOptional.isEmpty()) {
      sender.sendMessage(i18n.translatePrefixedMessage("warp-not-found"));
      return true;
    }
    warpRepository.remove(warpName);
    sender.sendMessage(i18n.translatePrefixedMessage("warp-deleted", warpName));
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
        var commands = new ArrayList<>(Arrays.asList("set", "delete"));
        return StringUtil.copyPartialMatches(
          args[0],
          findWarpNames(),
          commands
        );
      }
      case 2: {
        if (!args[0].equalsIgnoreCase("delete")) {
          return List.of();
        }
        return StringUtil.copyPartialMatches(
          args[0],
          findWarpNames(),
          new ArrayList<>()
        );
      }
      default: return List.of();
    }
  }

  private Collection<String> findWarpNames() {
    return warpRepository.findAll().stream()
      .map(Warp::name)
      .collect(Collectors.toUnmodifiableList());
  }
}
