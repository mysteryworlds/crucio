package com.mysteryworlds.crucio.spigot.service.language;

import com.google.common.base.Preconditions;
import com.mysteryworlds.crucio.api.service.LanguageService;
import org.bukkit.ChatColor;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Locale;
import java.util.ResourceBundle;

@Singleton
public class LanguageServiceImpl implements LanguageService {

    private final String resourceBundleName;

    @Inject
    public LanguageServiceImpl(@Named("crucioResourceBundleName") String resourceBundleName) {
        this.resourceBundleName = resourceBundleName;
    }

    @Override
    public String translate(Locale locale, String messageKey, String... arguments) {
        Preconditions.checkNotNull(messageKey, "message key cannot be null");

        // Get resource bundle
        ResourceBundle resourceBundle;
        if (locale != null) {
            resourceBundle = ResourceBundle.getBundle(resourceBundleName, locale);
        } else {
            resourceBundle = ResourceBundle.getBundle(resourceBundleName);
        }

        // Load message
        String message = resourceBundle.getString(messageKey);

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
        return translate(null, messageKey, arguments);
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
}
