package FileManager;

import OPcode.OPTreeNode;
import postfix.DataManger;

import javax.swing.tree.TreePath;
import java.io.BufferedReader;
import java.io.File;
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
    public String basePathString;
    public String encoding;
    private Path basePath;
    private static PathManager instance;

    public PathManager(String basePath) {
        init(basePath, "x-SJIS_0213");
        instance = this;
    }

    public static PathManager getInstance() {
        return instance;
    }

    public PathManager(String basePath, String encoding) {
        init(basePath, encoding);
        instance = this;
    }

    private void init(String path, String encoding){
        Path testPath = Path.of(path);
        while(!checkIsBasicFolder(String.valueOf(testPath))){
            testPath = testPath.getParent();
            if(testPath == null){
                throw new RuntimeException();
            }
        }
        basePath = testPath;
        basePathString = testPath.toString();
        this.encoding = encoding;
    }

    public String getFullPathFromTree(TreePath treePath){
        Object[] pathArray = treePath.getPath();
        StringBuilder filePathBuilder = new StringBuilder();
        filePathBuilder.append(findDataFolder());
        for (int i = 1; i < pathArray.length; i++) {
            filePathBuilder.append(File.separator); // 使用文件分隔符
            filePathBuilder.append(pathArray[i].toString());
        }

        return filePathBuilder.toString();
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

    public Path getDataNamePath(String name){
        Path path = findDataFolder(basePathString);
        assert path != null;
        return path.resolve(name);
    }

    public File getFaceFile(String faceName){
        Path facePath = getDataNamePath("face").resolve(faceName);
        return facePath.toFile();
    }

    public File getDataFile(String dataType, String dataName){
        Path dataPath = getDataNamePath(dataType).resolve(dataName);
        return dataPath.toFile();
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

    public static boolean checkIsBasicFolder(String pathString){
        List<Path> paths = getPaths(pathString);
        assert paths != null;
        for(Path path : paths){
            if(checkIsDataFolder(path))return true;
        }
        return false;
    }

    public static boolean checkIsDataFolder(Path path){
        AtomicInteger count = new AtomicInteger();
        List<Path> paths = getPaths(path.toString());
        if(paths==null)return false;
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
        for(Map.Entry<String, OPTreeNode> name : DataManger.dataMap.entrySet()){
            System.out.print(name.getKey()+"\n");
        }
    }
}
