package com.mysteryworlds.crucio.home;

import com.mysteryworlds.crucio.player.PlayerRepository;
import java.util.List;
import javax.inject.Inject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public final class HomeCommand implements CommandExecutor, TabCompleter {
  private final PlayerRepository playerRepository;

  @Inject
  private HomeCommand(
    PlayerRepository playerRepository
  ) {
    this.playerRepository = playerRepository;
  }

  @Override
  public boolean onCommand(
    CommandSender commandSender,
    Command command,
    String label,
    String[] strings
  ) {
    return false;
  }

  @Override
  public List<String> onTabComplete(
    CommandSender commandSender,
    Command command,
    String label,
    String[] strings
  ) {
    return null;
  }
}
