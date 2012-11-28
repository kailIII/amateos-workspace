package org.inftel.socialwind.client.desktop.view;

import java.beans.Beans;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
    private Messages() {
        // do not instantiate
    }

    private static final String BUNDLE_NAME = "org.inftel.socialwind.client.desktop.view.messages"; //$NON-NLS-1$
    private static final ResourceBundle RESOURCE_BUNDLE = loadBundle();

    private static ResourceBundle loadBundle() {
        return ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault());
    }

    public static String getString(String key) {
        try {
            ResourceBundle bundle = Beans.isDesignTime() ? loadBundle() : RESOURCE_BUNDLE;
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return "!" + key + "!";
        }
    }
}
