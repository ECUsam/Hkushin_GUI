package postfix;

import java.util.HashMap;
import java.util.List;

public class DataManger {
    // name - path
    //        codeLine
    //        parent
    //        children
    private static final HashMap<String, List<String>> classHashMap;

    static {
        classHashMap = new HashMap<>();
    }

    public static void putValue(String name, List<String> data){
        classHashMap.put(name, data);
    }
    public static List<String> getValue(String name){
        return classHashMap.get(name);
    }

    public static void classGetChildren(String cl, String ch){
        classHashMap.get(cl).add(ch);
    }

    public static boolean searchClass(String name){
        return classHashMap.containsKey(name);
    }
}
