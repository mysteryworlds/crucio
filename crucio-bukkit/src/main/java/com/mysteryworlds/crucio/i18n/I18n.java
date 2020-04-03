package com.mysteryworlds.crucio.i18n;

import com.google.common.base.Preconditions;
import com.google.common.collect.Table;
import java.util.Locale;
import javax.inject.Singleton;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@Singleton
public final class I18n {
  private static final Locale DEFAULT_LOCALE = Locale.GERMANY;
  private static final String PREFIX_KEY = "prefix";

  private final Table<Locale, String, String> messages;

  private I18n(Table<Locale, String, String> messages) {
    this.messages = messages;
  }

  public void sendMessage(
    Player player,
    String messageKey,
    Object... arguments
  ) {
    Preconditions.checkNotNull(player);
    Preconditions.checkNotNull(messageKey);
    Preconditions.checkNotNull(arguments);
    var locale = findLocaleOrDefault(player.getLocale());
    var message = translateMessage(messageKey, locale, arguments);
    player.sendMessage(message);
  }

  public void sendPrefixedMessage(
    Player player,
    String messageKey,
    Object... arguments
  ) {
    Preconditions.checkNotNull(player);
    Preconditions.checkNotNull(messageKey);
    Preconditions.checkNotNull(arguments);
    var locale = findLocaleOrDefault(player.getLocale());
    var message = translatePrefixedMessage(messageKey, locale, arguments);
    player.sendMessage(message);
  }

  public String translatePrefixedMessage(
    String messageKey,
    Locale locale,
    Object... arguments
  ) {
    var message = findMessage(locale, messageKey);
    var prefix = translateMessage(PREFIX_KEY, locale);
    message = String.format(message, arguments);
    message =  String.format("%s%s", prefix, message);
    return ChatColor.translateAlternateColorCodes('&', message);
  }

  public String translatePrefixedMessage(
    String messageKey,
    Object... arguments
  ) {
    return translatePrefixedMessage(messageKey, DEFAULT_LOCALE, arguments);
  }

  private Locale findLocaleOrDefault(String locale) {
    var localeKey = locale.replace("_", "-");
    var foundLocale = Locale.forLanguageTag(localeKey);
    return foundLocale != null ? foundLocale : DEFAULT_LOCALE;
  }

  public String translateMessage(
    String messageKey,
    Object... arguments
  ) {
    return translateMessage(messageKey, DEFAULT_LOCALE, arguments);
  }

  public String translateMessage(
    String messageKey,
    Locale locale,
    Object... arguments
  ) {
    var message = findMessage(locale, messageKey);
    message = String.format(message, arguments);
    return ChatColor.translateAlternateColorCodes('&', message);
  }

  private String findMessage(Locale locale, String messageKey) {
    return messages.get(locale, messageKey);
  }

  public static I18n withMessages(Table<Locale, String, String> messages) {
    Preconditions.checkNotNull(messages);
    return new I18n(messages);
  }
}
