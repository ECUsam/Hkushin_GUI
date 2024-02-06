import FileManager.PathManager;
import postfix.ClassGetterFromFile;
import postfix.ClassParse;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        PathManager pathManager = new PathManager("D:\\新約迫真戦記―ほのぼの神話ver0.55 軽量版", "x-SJIS_0213");
        ClassGetterFromFile a = new ClassGetterFromFile(pathManager);
        //Ai戦闘制御_追加部分.dat
        a.File2Class("D:\\新約迫真戦記―ほのぼの神話ver0.55 軽量版\\a_default\\script\\defense.dat");
        // 迭代器选择
        Iterator<Map.Entry<String, Integer>> iterator = a.scriptClass.entrySet().iterator();
        ClassParse c = new ClassParse("null", "");

        while (iterator.hasNext()){
            var next = iterator.next();
            // System.out.print(next.getKey());
            c.updateSource(next.getKey(), "", next.getValue());
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
            break;
        }
       //  System.out.print(secondEntry.getKey());

        // b.ParseSource();
        // b.printTokenList();

    }
}
