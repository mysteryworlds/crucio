package com.mysteryworlds.crucio.home;

import com.mysteryworlds.crucio.i18n.I18n;
import com.mysteryworlds.crucio.player.CrucioPlayer;
import com.mysteryworlds.crucio.player.CrucioPlayer.Home;
import com.mysteryworlds.crucio.player.PlayerRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
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

public final class HomeCommand implements CommandExecutor, TabCompleter {
  private final PlayerRepository playerRepository;
  private final I18n i18n;

  @Inject
  private HomeCommand(
    PlayerRepository playerRepository,
    I18n i18n
  ) {
    this.playerRepository = playerRepository;
    this.i18n = i18n;
  }

  @Override
  public boolean onCommand(
    CommandSender commandSender,
    Command command,
    String label,
    String[] args
  ) {
    switch (args.length) {
      case 0: {
        return listHomes(command, commandSender);
      }
      case 1: {
        return teleportHome(command, commandSender, args[0]);
      }
      case 2: {
        var keyword = args[0];
        if (keyword.equalsIgnoreCase("set")) {
          return createHome(command, commandSender, args[1]);
        } else if (keyword.equalsIgnoreCase("delete")) {
          return deleteHome(command, commandSender, args[1]);
        }
        return false;
      }
    }
    return false;
  }

  private boolean listHomes(
    Command command,
    CommandSender sender
  ) {
    if (!sender.hasPermission("crucio.command.home.list")) {
      sender.sendMessage(command.getPermissionMessage());
      return true;
    }
    if (!(sender instanceof Player)) {
      sender.sendMessage(i18n.translatePrefixedMessage("command-player-only"));
      return true;
    }
    var player = (Player) sender;
    var homeNames = findHomeNames(player.getUniqueId());
    listHomes(player, homeNames);
    return true;
  }

  private static final String HOME_LIST_PREFIX = ChatColor.GOLD + "- ";

  private void listHomes(Player player, Collection<String> homeNames) {
    i18n.sendPrefixedMessage(player, "home-list");
    for (String homeName : homeNames) {
      TextComponent component = new TextComponent(HOME_LIST_PREFIX + homeName);
      component.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText("Teleport")));
      component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/home " + homeName));
      player.spigot().sendMessage(component);
    }
  }

  private boolean teleportHome(
    Command command,
    CommandSender sender,
    String homeName
  ) {
    if (!sender.hasPermission("crucio.command.home")) {
      sender.sendMessage(command.getPermissionMessage());
      return true;
    }
    if (!(sender instanceof Player)) {
      sender.sendMessage(i18n.translatePrefixedMessage("command-player-only"));
      return true;
    }
    var bukkitPlayer = (Player) sender;
    var player = playerRepository.findOrCreatePlayer(bukkitPlayer.getUniqueId());
    return teleportHome(player, homeName);
  }

  private boolean teleportHome(CrucioPlayer player, String homeName) {
    var homeOptional = player.findHome(homeName);
    if (homeOptional.isEmpty()) {
      i18n.sendPrefixedMessage(player.asBukkitPlayer(), "home-not-found");
      return true;
    }
    var teleport = homeOptional.get().teleport(player.asBukkitPlayer());
    i18n.sendPrefixedMessage(
      player.asBukkitPlayer(),
      teleport ? "home-teleport" : "home-teleport-failed",
      homeName
    );
    return teleport;
  }

  private boolean createHome(
    Command command,
    CommandSender sender,
    String homeName
  ) {
    if (!sender.hasPermission("crucio.command.home.set")) {
      sender.sendMessage(command.getPermissionMessage());
      return true;
    }
    if (!(sender instanceof Player)) {
      sender.sendMessage(i18n.translatePrefixedMessage("command-player-only"));
      return true;
    }
    var bukkitPlayer = (Player) sender;
    var player = playerRepository.findOrCreatePlayer(bukkitPlayer.getUniqueId());
    var homeOptional = player.findHome(homeName);
    if (homeOptional.isPresent()) {
      i18n.sendPrefixedMessage(bukkitPlayer, "home-already-exists", homeName);
      return true;
    }
    player.addHome(homeName, bukkitPlayer.getLocation());
    i18n.sendPrefixedMessage(bukkitPlayer, "home-created", homeName);
    return true;
  }

  private boolean deleteHome(
    Command command,
    CommandSender sender,
    String homeName
  ) {
    if (!sender.hasPermission("crucio.command.home.delete")) {
      sender.sendMessage(command.getPermissionMessage());
      return true;
    }
    if (!(sender instanceof Player)) {
      sender.sendMessage(i18n.translatePrefixedMessage("command-player-only"));
      return true;
    }
    var bukkitPlayer = (Player) sender;
    var player = playerRepository.findOrCreatePlayer(bukkitPlayer.getUniqueId());
    var homeOptional = player.findHome(homeName);
    if (homeOptional.isEmpty()) {
      i18n.sendPrefixedMessage(bukkitPlayer, "home-not-found", homeName);
      return true;
    }
    player.removeHome(homeName);
    i18n.sendPrefixedMessage(bukkitPlayer, "home-deleted", homeName);
    return true;
  }

  @Override
  public List<String> onTabComplete(
    CommandSender commandSender,
    Command command,
    String label,
    String[] args
  ) {
    if (!(commandSender instanceof Player)) {
      return List.of();
    }
    switch (args.length) {
      case 1: {
        var commands = new ArrayList<>(Arrays.asList("set", "delete"));
        return StringUtil.copyPartialMatches(
          args[0],
          findHomeNames(((Player) commandSender).getUniqueId()),
          commands
        );
      }
      case 2: {
        return StringUtil.copyPartialMatches(
          args[1],
          findHomeNames(((Player) commandSender).getUniqueId()),
          new ArrayList<>()
        );
      }
    }
    return List.of();
  }

  private Collection<String> findHomeNames(UUID playerId) {
    var player = playerRepository.findOrCreatePlayer(playerId);
    return player.homes().stream().map(
      Home::name
    ).collect(Collectors.toUnmodifiableSet());
  }
}
