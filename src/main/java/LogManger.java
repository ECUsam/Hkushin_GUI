import java.util.logging.Logger;

public class LogManger {
    private static final Logger LOGGER = Logger.getLogger(LogManger.class.getName());

    public static void addLog(String data, char[] current, int endPoint) {
        int length = Math.min(16, endPoint - current.length);
        String str = new String(current, 0, length);
        LOGGER.info(String.format("%s current=%d, endPoint=%d, val=%d, ch=%s", data, current, endPoint, (int) current[0], str));
    }

}
