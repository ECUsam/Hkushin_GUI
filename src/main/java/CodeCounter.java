import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CodeCounter {

    private static int countLine = 0;
    private static List<File> fileList = new ArrayList<>();
    private static File root = new File(System.getProperty("user.dir") + File.separator + "src");

    public static void main(String[] args) throws IOException {
        getFile(root);
        for (File file : fileList) {
            System.out.println(file.getPath());
            readLine(file);
        }
        System.out.println("readLineSum:" + countLine);
    }

    private static void readLine(File file) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String str;
        do {
            str = bufferedReader.readLine();
            if (str != null && !str.isEmpty()) {
                countLine++;
            }
        } while (str != null);

    }

    private static void getFile(File targetFile) {
        File[] files = targetFile.listFiles(pathname -> pathname.isDirectory() || pathname.getPath().endsWith(".java") || pathname.getPath().endsWith(".fxml") || pathname.getPath().endsWith(".css"));
        for (File file : files) {
            if (file.isDirectory()) {
                getFile(file);
            } else {
                fileList.add(file);
            }
        }
    }
}
