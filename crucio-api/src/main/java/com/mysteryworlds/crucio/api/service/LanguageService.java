package com.mysteryworlds.crucio.api.service;

import java.util.Locale;

public interface LanguageService {

  /**
   * Translate the message with the given message key into the given locale and replace the given
   * arguments.
   *
   * @param locale The locale.
   * @param messageKey The message key.
   * @param arguments The arguments.
   * @return The translated message.
   */
  String translate(Locale locale, String messageKey, String... arguments);

  /**
   * Translate the message with the given message key into the default language and replace the
   * given arguments.
   *
   * @param messageKey The message key.
   * @param arguments The arguments.
   * @return The translated message.
   */
  String translate(String messageKey, String... arguments);

  /**
   * Get the default locale.
   *
   * @return The default locale.
   */
  Locale getDefaultLocale();

  /**
   * Set the default locale.
   *
   * @param locale The locale.
   */
  void setDefaultLocale(Locale locale);
}
