package br.com.EasyTable.Shared.Properties;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageResources {
    private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");

    public static String get(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return "!" + key + "!";
        }
    }
}
