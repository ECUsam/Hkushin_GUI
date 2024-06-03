package Constants;

import postfix.LogManager;

import java.io.*;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class Constants_GUI {
    private static ResourceBundle messages;
    private static ResourceBundle messages_description;
    private static ResourceBundle messages_function;
    private static final String CONFIG_FILE_PATH = "config.properties";
    public static Properties config;

    static {
        // 在类加载时，默认设置为中文资源
        switch_language(Locale.forLanguageTag("zh"));
        loadConfigFromFile();
    }

    public static void switch_language(Locale locale) {
        messages = ResourceBundle.getBundle("language." + locale, locale);
        messages_description = ResourceBundle.getBundle("language."+ locale +"_description", locale);
        messages_function = ResourceBundle.getBundle("language."+ locale +"_functions", locale);
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

    public static String getFunction(String key){
        try{
            return messages_function.getString(key);
        }catch (Exception e){
            return key;
        }
    }


    public static void saveConfigToFile() {
        try (OutputStream output = new FileOutputStream(CONFIG_FILE_PATH)) {
            config.store(output, "Config File");
            LogManager.addLog("Config saved to file.");
        } catch (IOException e) {
            LogManager.addLog(e.toString());
        }
    }

    public static void loadConfigFromFile() {
        try (InputStream input = new FileInputStream(CONFIG_FILE_PATH)) {
            config = new Properties();
            config.load(input);
        } catch (IOException e) {
            LogManager.addLog("打开配置文件失败");
            config = new Properties();
        }
    }
}
