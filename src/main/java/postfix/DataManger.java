package postfix;

import Constants.Constants;
import FileManager.PathManager;
import GUI.Utils;
import OPcode.TreeNode;
import Token.TokenFeature;
import Constants.Constants_GUI;
import java.util.*;

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

    public static HashMap<String, TreeNode> raceMap;
    public static List<String[]> orphanList;
    public static PathManager pathManager;

    static {
        classHashMap = new HashMap<>();
        orphanList = new ArrayList<>();
        dataMap = new HashMap<>();
        raceMap = new HashMap<>();
    }

    public static void update(){
        classHashMap = new HashMap<>();
        orphanList = new ArrayList<>();
        dataMap = new HashMap<>();
        raceMap = new HashMap<>();
    }

    public static HashMap<String, TreeNode> getRaceMap(){
        for(Map.Entry<String, List<String>> a : classHashMap.entrySet()){
            if(Objects.equals(a.getValue().get(1), "race")){
                raceMap.put(a.getKey(), dataMap.get(a.getKey()));
            }
        }
        return raceMap;
    }

    // attribute格式 meta_name <name, level>
    public static HashMap<String, AbstractMap.SimpleEntry<String, String>> getAttrMap(){
        HashMap<String, AbstractMap.SimpleEntry<String, String>> AttrMap = new HashMap<>();
        for(String s : Constants.attrBase){
            var entry = new AbstractMap.SimpleEntry<>(Constants_GUI.get(s), "0");
            AttrMap.put(s, entry);
        }
        for(Map.Entry<String, List<String>> a : classHashMap.entrySet()){
            String tempName = a.getKey();
            if(tempName.startsWith("attribute")){
                TreeNode node = dataMap.get(tempName);
                for(TreeNode fea : node.getChildren()){
                    if(fea.value instanceof TokenFeature tokenFeature){
                        String feaName = tokenFeature.FeatureName;
                        String[] feaS = Utils.getOutOfSpace(tokenFeature.strings_feature[0]).split("\\*");
                        // System.out.print(feaS[0]+"\n"+feaS[1]+"\n");
                        var entry = new AbstractMap.SimpleEntry<>(feaS[0], feaS[1]);
                        AttrMap.put(feaName, entry);
                    }
                }
            }
        }
        return AttrMap;
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
