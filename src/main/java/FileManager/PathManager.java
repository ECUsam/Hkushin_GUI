package FileManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SuppressWarnings("unused")
public class PathManager {
    public final String basePathString;
    public final String encoding;
    private final Path basePath;

    public PathManager(String basePath) {
        this.basePathString = basePath;
        this.basePath = Paths.get(basePath);
        encoding = "utf-16";
    }

    public PathManager(String basePath, String encoding) {
        this.basePathString = basePath;
        this.basePath = Paths.get(basePath);
        this.encoding = encoding;
    }

    public String abPathString(String path) {
        return basePath + "\\" + path;
    }

    public Path abPath(String rePath){
        System.out.print(basePath.resolve(rePath));
        return basePath.resolve(rePath);
    }

    // 失灵
    public BufferedReader open(String pathString) {
        Path path = Paths.get(pathString);
        if (path.isAbsolute()) {
            try (
                    BufferedReader reader = Files.newBufferedReader(path, Charset.forName(encoding))
            ) {
                return reader;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            try (
                    BufferedReader reader = Files.newBufferedReader(abPath(pathString), Charset.forName(encoding))
            ) {
                return reader;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
