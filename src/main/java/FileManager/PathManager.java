package FileManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.List;

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
        return basePath.resolve(rePath);
    }

    public Path rePath(String abPath){
        return basePath.relativize(Path.of(abPath));
    }

    public static Path scriptPath_to_basePath(String scriptPath){
        Path fullPath = Paths.get(scriptPath);
        return fullPath.getParent().getParent();
    }
    public Path get_to_scriptPath() {
        Path path = findDataFolder();
        return Path.of(path + "\\script");
    }

    public Path findDataFolder(){
        List<Path> paths = getPaths(basePath.toString());
        assert paths != null;
        for(Path path : paths){
            if(checkIsDataFolder(path))return path;
        }
        return null;
    }

    public static Path findDataFolder(String basePath){
        List<Path> paths = getPaths(basePath);
        assert paths != null;
        for(Path path : paths){
            if(checkIsDataFolder(path))return path;
        }
        return null;
    }

    public static boolean checkIsDataFolder(Path path){
        AtomicInteger count = new AtomicInteger();
        List<Path> paths = getPaths(path.toString());
        assert paths != null;
        paths.forEach(path1 -> {
            path1 = path1.getFileName();
            if(path1.toString().equals("script")||path1.toString().equals("image")||path1.toString().equals("picture")||path1.toString().equals("chip")) count.addAndGet(1);
        });
        return count.get() >= 2;
    }

    public static List<Path> getPaths(String path) {
        try (Stream<Path> paths = Files.list(Paths.get(path))) {
            return paths.collect(Collectors.toList());
        } catch (IOException e) {
            return null;
        }
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
    public static void main(String[] args) {
        boolean res = checkIsDataFolder(Paths.get("D:\\新約迫真戦記―ほのぼの神話ver0.55 軽量版"));
        Path path = findDataFolder("D:\\新約迫真戦記―ほのぼの神話ver0.55 軽量版");
        ScriptReader scriptReader = new ScriptReader("D:\\新約迫真戦記―ほのぼの神話ver0.55 軽量版\\a_default\\script");
        PathManager pathManager = new PathManager("D:\\新約迫真戦記―ほのぼの神話ver0.55 軽量版");
        scriptReader.readAll();
    }
}
