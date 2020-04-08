package com.mysteryworlds.crucio.colorsign;

import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public final class ColoredSignTrigger implements Listener {
  private static final String PERMISSION_COLORED_SIGN = "crucio.coloredsigns";

  @EventHandler
  public void onSignChange(SignChangeEvent signChange) {
    var player = signChange.getPlayer();
    if (!player.hasPermission(PERMISSION_COLORED_SIGN)) {
      return;
    }
    transformSignColors(signChange);
  }

  private void transformSignColors(SignChangeEvent signChange) {
    var lines = signChange.getLines();
    lines = transformLineColors(lines);
    applyTransformedLines(signChange, lines);
  }

  private String[] transformLineColors(String[] lines) {
    return Arrays.stream(lines)
      .map(ColoredSignTrigger::translateColor)
      .toArray(String[]::new);
  }

  private static String translateColor(String line) {
    return ChatColor.translateAlternateColorCodes('&', line);
  }

  private void applyTransformedLines(
    SignChangeEvent signChange,
    String[] lines
  ) {
    for (int currentLine = 0; currentLine < lines.length; currentLine++) {
      signChange.setLine(currentLine, lines[currentLine]);
    }
  }
}
