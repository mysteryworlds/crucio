package com.mysteryworlds.crucio.config;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.ImmutableTable.Builder;
import com.google.common.collect.Table;
import java.util.Locale;
import java.util.Map.Entry;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public final class CrucioConfig {
  private final String chatFormat;
  private final ConfigurationSection messages;

  public CrucioConfig(
    String chatFormat,
    ConfigurationSection messages
  ) {
    this.chatFormat = chatFormat;
    this.messages = messages;
  }

  public String chatFormat() {
    return ChatColor.translateAlternateColorCodes('&', chatFormat);
  }

  public Table<Locale, String, String> messages() {
    var builder = ImmutableTable.<Locale, String, String>builder();
    fillMessages(messages, builder);
    return builder.build();
  }

  private void fillMessages(
    ConfigurationSection messages,
    Builder<Locale, String, String> builder
  ) {
    var languages = messages.getKeys(false);
    for (String language : languages) {
      Locale locale = Locale.forLanguageTag(language);
      var configurationSection = messages.getConfigurationSection(language);
      fillLocaleMessage(locale, configurationSection, builder);
    }
  }

  private void fillLocaleMessage(
    Locale locale,
    ConfigurationSection configurationSection,
    Builder<Locale, String, String> builder
  ) {
    var values = configurationSection.getValues(false);
    for (Entry<String, Object> entry : values.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();
      builder.put(locale, key, String.valueOf(value));
    }
  }

  public static CrucioConfig fromFileConfig(FileConfiguration configuration) {
    return new CrucioConfig(
      "",
      configuration.getConfigurationSection("messages")
    );
  }
}
