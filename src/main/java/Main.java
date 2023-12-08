import postfix.ClassGetterFromFile;
import postfix.ClassLexer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ClassGetterFromFile a = new ClassGetterFromFile( "x-SJIS_0213");
        a.File2Class("E:\\download\\新約迫真戦記―ほのぼの神話ver0.52 豪華版\\a_default\\script\\Ai戦闘制御_追加部分.dat");
        ClassLexer b = new ClassLexer(a.scriptClass.get(1));
        b.ParseSource();
        //b.printTokenList();
    }
}
