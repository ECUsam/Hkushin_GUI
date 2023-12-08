package postfix;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogManager {
    private static final Logger LOGGER = Logger.getLogger(LogManager.class.getName());

    static {
        try {
            // 指定日志文件的路径
            String logDirectoryPath = "logs";
            String logFilePath = logDirectoryPath + "/logfile.log";

            // 创建日志文件的目录（如果它不存在）
            File logDirectory = new File(logDirectoryPath);
            if (!logDirectory.exists()) {
                logDirectory.mkdirs(); // 创建目录
            }

            FileHandler fileHandler = new FileHandler(logFilePath, true);

            fileHandler.setFormatter(new SimpleFormatter());

            LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            LOGGER.warning("FileHandler initialization failed: " + e.getMessage());
        }
    }

    public static void addLog(String data, char[] current, int endPoint) {
        int length = Math.min(16, endPoint - current.length);
        String str = new String(current, 0, length);
        LOGGER.info(String.format("%s current=%s, endPoint=%d, val=%d, ch=%s",
                data, new String(current), endPoint, (int) current[0], str));
    }

    public static void addLog(String data){
        LOGGER.info(data);
    }

    public static void main(String[] args) {
        char[] arr = {'a', 'b', 'c', 'd', 'e', 'f', 'g'};
        LogManager.addLog("Test log", arr, 7);
    }
}
