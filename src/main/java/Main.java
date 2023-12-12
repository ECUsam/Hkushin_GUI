import postfix.ClassGetterFromFile;
import postfix.ClassLexer;
import postfix.ClassParse;

import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ClassGetterFromFile a = new ClassGetterFromFile( "x-SJIS_0213");
        //Ai戦闘制御_追加部分.dat
        a.File2Class("E:\\download\\新約迫真戦記―ほのぼの神話ver0.52 豪華版\\a_default\\test.dat");
        ClassLexer b = new ClassLexer(a.scriptClass.get(0));
        b.ParseSource();
        //b.printTokenList();
        ClassParse c = new ClassParse(a.scriptClass.get(0));
        c.run();
        var json = c.getJson();
        System.out.print(json.toString(4));

        try (FileWriter file = new FileWriter("output.json")) {
            file.write(json.toString(4));
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
