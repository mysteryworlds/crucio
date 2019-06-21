package com.mysteryworlds.crucio.spigot.service.language;

import com.google.common.base.Preconditions;
import com.mysteryworlds.crucio.api.service.LanguageService;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Locale;

public class LanguageServiceImpl implements LanguageService {

    private final Configuration configuration;

    public LanguageServiceImpl(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String translate(Locale locale, String messageKey, String... arguments) {
        Preconditions.checkNotNull(messageKey, "message key cannot be null");

        // Get resource bundle

        // Load message
        String message = getLocalizedMessage(locale, messageKey);

        // Colorized message
        String colorizedMessage = ChatColor.translateAlternateColorCodes('&', message);

        // Check for arguments and return message if none are there
        if (arguments == null || arguments.length == 0) {
            return colorizedMessage;
        }

        return String.format(colorizedMessage, (Object[]) arguments);
    }

    @Override
    public String translate(String messageKey, String... arguments) {

        // Use default locale
        return translate(getDefaultLocale(), messageKey, arguments);
    }

    @Override
    public Locale getDefaultLocale() {

        return Locale.getDefault();
    }

    @Override
    public void setDefaultLocale(Locale locale) {
        Preconditions.checkNotNull(locale, "Default locale cannot be null.");

        Locale.setDefault(locale);
    }

    /**
     * Get a localized message in the given locale.
     *
     * @param locale     The locale.
     * @param messageKey The key of the message.
     *
     * @return The localized message.
     */
    private String getLocalizedMessage(Locale locale, String messageKey) {

        ConfigurationSection language = configuration.getConfigurationSection("language");
        if (language == null) {
            return "";
        }

        ConfigurationSection configurationSection = language.getConfigurationSection(locale.toLanguageTag());
        if (configurationSection == null) {
            return "";
        }

        return configurationSection.getString(messageKey);
    }
}
