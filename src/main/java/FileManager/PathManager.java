package FileManager;

import GUI.INFORMATION;
import OPcode.TreeNode;
import postfix.DataManger;
import postfix.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.List;

@SuppressWarnings("unused")
public class PathManager {
    public final String basePathString;
    public final String encoding;
    private final Path basePath;
    private static PathManager instance;

    public PathManager(String basePath) {
        this.basePathString = basePath;
        this.basePath = Paths.get(basePath);
        encoding = "x-SJIS_0213";
        instance = this;
    }

    public static PathManager getInstance() {
        return instance;
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

    public static String abPath(String rePathString, String basePathString){
        return Path.of(basePathString).resolve(rePathString).toString();
    }

    public Path rePath(String abPath){
        return basePath.relativize(Path.of(abPath));
    }

    public static String rePathString(String abPath, String basePathString){
        return Path.of(basePathString).relativize(Path.of(abPath)).toString();
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
        for(Map.Entry<String, TreeNode> name : DataManger.dataMap.entrySet()){
            System.out.print(name.getKey()+"\n");
        }
    }
}
