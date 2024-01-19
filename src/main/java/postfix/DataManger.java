package postfix;

import FileManager.PathManager;
import OPcode.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class DataManger {
    // name - path      0
    //        type      1
    //        codeLine  2
    //        parent    3
    //        children  4
    public static HashMap<String, List<String>> classHashMap;
    // String[] - parent child
    // 名字 节点
    public static HashMap<String, TreeNode> dataMap;
    public static List<String[]> orphanList;
    public static PathManager pathManager;
    static {
        classHashMap = new HashMap<>();
        orphanList = new ArrayList<>();
        dataMap = new HashMap<>();
    }

    public static void update(){
        classHashMap = new HashMap<>();
        orphanList = new ArrayList<>();
        dataMap = new HashMap<>();
    }

    public static void putValue(String name, List<String> data){
        classHashMap.put(name, data);
    }
    public static List<String> getValue(String name){
        return classHashMap.get(name);
    }
    public static boolean checkExist(String name){
        return classHashMap.containsKey(name);
    }

    public static void ThereIsOrphanFound(String cl, String ch){
        String[] orphan = {cl, ch};
        orphanList.add(orphan);
    }

    public static void killOrphans(){
        Iterator<String[]> iterator = orphanList.iterator();
        while (iterator.hasNext()) {
            String[] orphan = iterator.next();

            if (searchClass(orphan[0])) {
                classGetChildren(orphan[0], orphan[1]);
                iterator.remove(); // 使用迭代器的 remove 方法安全删除元素
            }
        }
    }


    public static void classGetChildren(String cl, String ch){
        classHashMap.get(cl).add(ch);
    }

    public static boolean searchClass(String name){
        return classHashMap.containsKey(name);
    }
}
