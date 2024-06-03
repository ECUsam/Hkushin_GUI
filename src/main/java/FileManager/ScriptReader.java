package FileManager;

import postfix.ClassGetterFromFile;
import postfix.ClassParse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;


public class ScriptReader {
    private String script_path;
    public ClassParse classParse;
    public ClassGetterFromFile classGetterFromFile;
    public PathManager pathManager;
    public ScriptReader(String script_path){
        this.script_path = script_path;
        this.classParse = new ClassParse("null");
        //x-SJIS_0213
        PathManager pathManager = new PathManager(PathManager.scriptPath_to_basePath(script_path).toString(), "x-SJIS_0213");
        this.pathManager = pathManager;
        this.classGetterFromFile = new ClassGetterFromFile(pathManager);
    }
    public ScriptReader(PathManager pathManager){
        this.pathManager = pathManager;
        this.classParse = new ClassParse("null");
        this.classGetterFromFile = new ClassGetterFromFile(pathManager);
        this.script_path = pathManager.get_to_scriptPath().toString();
    }
    public void readAll() {
        try {
            // System.out.print(script_path);
            Stream<Path> pathStream = Files.walk(Path.of(script_path));
            pathStream.filter(Files::isRegularFile).filter(path -> path.toString().endsWith(".dat")).forEach(path -> {
            // System.out.print(pathManager.rePath(path.toString()));
            // System.out.print("\n");
            classGetterFromFile.File2Class(path.toString());
            classGetterFromFile.scriptClass.forEach((s, integer) -> {
                classParse.updateSource(s, path.toString());
                classParse.run();
            });
            classGetterFromFile.update();
        });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
