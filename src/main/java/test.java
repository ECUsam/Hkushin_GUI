import java.nio.file.Path;
import java.nio.file.Paths;

public class test {
    public static void main(String[] args) {
        // 获取当前工作目录的相对路径
        String currentRelativePath = "src/main/resources/image/biim.png";
        boolean is = Path.of(currentRelativePath).toFile().exists();
        System.out.println("Current relative path is: " + is);
    }
}
