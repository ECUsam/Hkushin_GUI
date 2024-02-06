package postfix;

import Constants.Constants;
import FileManager.PathManager;
import GUI.Utils;
import OPcode.OPTreeNode;
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
    public static HashMap<String, OPTreeNode> dataMap;

    public static HashMap<String, OPTreeNode> raceMap;
    public static List<String[]> orphanList;
    public static PathManager pathManager;

    static {
        classHashMap = new HashMap<>();
        orphanList = new ArrayList<>();
        dataMap = new HashMap<>();
        raceMap = new HashMap<>();
    }

    public static boolean classHasFather(OPTreeNode node){
        if(!Constants.class_type.contains((String) node.value))return false;
        String className = getNameFromClassTypeTreeNode(node);
        if(className == null)return false;
        return !(classHashMap.get(className).get(3) == null);
    }

    public static OPTreeNode classGetFather(OPTreeNode node){
        if(classHasFather(node)){
            String className = getNameFromClassTypeTreeNode(node);
            String fatherName = classHashMap.get(className).get(3);
            return dataMap.get(fatherName);
        }
        return null;
    }

    public static void update(){
        classHashMap = new HashMap<>();
        orphanList = new ArrayList<>();
        dataMap = new HashMap<>();
        raceMap = new HashMap<>();
    }

    public static HashMap<String, OPTreeNode> getRaceMap(){
        for(Map.Entry<String, List<String>> a : classHashMap.entrySet()){
            if(Objects.equals(a.getValue().get(1), "race")){
                raceMap.put(a.getKey(), dataMap.get(a.getKey()));
            }
        }
        return raceMap;
    }

    public static String searchFeatureFromClass_one(OPTreeNode classNode, String feaName){
        if(!Objects.equals(classNode.key, "classType"))return null;
        for(OPTreeNode treeNode : classNode.getChildren()){
            var feature = treeNode.value;
            if(feature instanceof TokenFeature tokenFeature){
                if(Objects.equals(tokenFeature.FeatureName, feaName)){
                    return tokenFeature.string;
                }
            }
        }
        return null;
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
                OPTreeNode node = dataMap.get(tempName);
                for(OPTreeNode fea : node.getChildren()){
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

    public static String getNameFromClassTypeTreeNode(OPTreeNode OPTreeNode){
        if(!Objects.equals(OPTreeNode.key, "classType"))return null;
        for (OPTreeNode treeNode : OPTreeNode.getChildren()){
            if(Objects.equals(treeNode.key, "className")){
                return (String) treeNode.value;
            }
        }
        return null;
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
