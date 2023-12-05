package Constants;

import java.util.Locale;
import java.util.ResourceBundle;

public class Constants_GUI {
    private static ResourceBundle messages;

    static {
        // 在类加载时，默认设置为中文资源
        switch_language(Locale.forLanguageTag("zh"));
    }

    public static void switch_language(Locale locale) {
        messages = ResourceBundle.getBundle("language." + locale, locale);
    }

    public static String get(String key) {
        try {
            return messages.getString(key);
        } catch (Exception e) {
            return key; // 如果没有找到对应的key，返回key本身
        }
    }
}
