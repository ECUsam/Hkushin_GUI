import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {
    public static void main(String[] args) {
        String fullwidthString = "Oh yeah, $ I'm just waiting for $ next customers. @nisendoru_dead.wav";
        String halfwidthString = fullwidthToHalfwidth(fullwidthString);
        System.out.println(halfwidthString); // 输出：ABCDEFG
    }

    public static String fullwidthToHalfwidth(String str) {
        // 创建正则表达式匹配器
        Pattern pattern = Pattern.compile("[Ａ-Ｚａ-ｚ]");
        Matcher matcher = pattern.matcher(str);

        // 使用匹配器进行替换
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            char fullwidthChar = matcher.group().charAt(0);
            char halfwidthChar = (char) (fullwidthChar - 65248); // 全角字母与半角字母之间的 Unicode 编码差值为 65248
            matcher.appendReplacement(sb, Character.toString(halfwidthChar));
        }
        matcher.appendTail(sb);

        return sb.toString();
    }
}