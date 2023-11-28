import postfix.ClassGetterFromFile;
import postfix.ClassParser;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ClassGetterFromFile a = new ClassGetterFromFile( "shift-jis");
        a.File2Class("E:\\download\\新約迫真戦記―ほのぼの神話ver0.52 豪華版\\a_default\\script\\spot_detail.dat");
        ClassParser b = new ClassParser(a.scriptClass.get(0));
        b.ParseSource();
        b.printTokenList();
    }
}
