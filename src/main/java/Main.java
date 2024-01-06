import FileManager.PathManager;
import postfix.ClassGetterFromFile;
import postfix.ClassLexer;
import postfix.ClassParse;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        PathManager pathManager = new PathManager("E:\\download\\新約迫真戦記―ほのぼの神話ver0.52 豪華版", "x-SJIS_0213");
        ClassGetterFromFile a = new ClassGetterFromFile(pathManager);
        //Ai戦闘制御_追加部分.dat
        a.File2Class("E:\\download\\新約迫真戦記―ほのぼの神話ver0.52 豪華版\\a_default\\script\\Ai戦闘制御_追加部分.dat");
        ClassLexer b = new ClassLexer(a.scriptClass.entrySet().iterator().next().getKey());
        // 迭代器选择
        Iterator<Map.Entry<String, Integer>> iterator = a.scriptClass.entrySet().iterator();
        Map.Entry<String, Integer> firstEntry  = iterator.next();
        Map.Entry<String, Integer> secondEntry = iterator.next();

       //  System.out.print(secondEntry.getKey());

        b.ParseSource();
        // b.printTokenList();

        ClassParse c = new ClassParse(secondEntry.getKey());
        c.run();
        var json = c.getJson();
        // System.out.print(json.toString(2));
        System.out.print(c.toCode());
        try (FileWriter file = new FileWriter("output.json")) {
            file.write(json.toString(4));
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
