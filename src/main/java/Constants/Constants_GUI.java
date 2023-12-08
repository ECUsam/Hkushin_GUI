package Constants;

import java.util.Locale;
import java.util.ResourceBundle;

public class Constants_GUI {
    private static ResourceBundle messages;
    private static ResourceBundle messages_description;

    static {
        // 在类加载时，默认设置为中文资源
        switch_language(Locale.forLanguageTag("zh"));
    }

    public static void switch_language(Locale locale) {
        messages = ResourceBundle.getBundle("language." + locale, locale);
        messages_description = ResourceBundle.getBundle("language."+ locale +"_description", locale);
    }

    public static String get(String key) {
        try {
            return messages.getString(key);
        } catch (Exception e) {
            return key; // 如果没有找到对应的key，返回key本身
        }
    }
    // ToolTip的描述之类的
    public static String getDescription(String key){
        try{
            return messages_description.getString(key);
        }catch (Exception e){
            return key;
        }
    }
}
