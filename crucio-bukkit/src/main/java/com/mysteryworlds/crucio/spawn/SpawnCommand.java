package com.mysteryworlds.crucio.spawn;

import com.mysteryworlds.crucio.i18n.I18n;
import java.util.List;
import javax.inject.Inject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public final class SpawnCommand implements CommandExecutor, TabCompleter {
  private final I18n i18n;

  @Inject
  private SpawnCommand(I18n i18n) {
    this.i18n = i18n;
  }

  @Override
  public boolean onCommand(
    CommandSender sender,
    Command command,
    String label,
    String[] args
  ) {
    if (!command.testPermission(sender)) {
      return true;
    }
    if (!(sender instanceof Player)) {
      sender.sendMessage(i18n.translatePrefixedMessage("command-player-only"));
      return true;
    }
    var player = (Player) sender;
    var teleport = player.teleport(player.getWorld().getSpawnLocation());
    if (teleport) {
      i18n.sendPrefixedMessage(player, "spawn-teleport");
    } else {
      i18n.sendPrefixedMessage(player, "spawn-teleport-failed");
    }
    return true;
  }

  @Override
  public List<String> onTabComplete(
    CommandSender sender,
    Command command,
    String alias,
    String[] args
  ) {
    return List.of();
  }
}
