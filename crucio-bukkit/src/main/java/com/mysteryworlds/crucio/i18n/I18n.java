package com.mysteryworlds.crucio.i18n;

import com.google.common.base.Preconditions;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@Singleton
public final class I18n {
  private static final Locale DEFAULT_LOCALE = Locale.GERMAN;
  private static final String BUNDLE_NAME = "crucio";
  private static final String PREFIX_KEY = "prefix";

  private final Plugin plugin;

  @Inject
  private I18n(Plugin plugin) {
    this.plugin = plugin;
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
    var message = findBundle(locale).getString(messageKey);
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
    var foundLocale = Locale.forLanguageTag(locale);
    if (foundLocale == null && locale.contains("_")) {
      return findLocaleOrDefault(locale.replaceAll("_", "-"));
    }
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
    var message = findBundle(locale) .getString(messageKey);
    return ChatColor.translateAlternateColorCodes('&', String.format(message, arguments));
  }

  private ResourceBundle findBundle(Locale locale) {
    return ResourceBundle.getBundle(BUNDLE_NAME, locale);
  }
}
