package postfix;

import Constants.Constants;
import FileManager.PathManager;
import GUI.Utils;
import postfix.Token.TokenClass;
import OPcode.OPTreeNode;
import postfix.Token.TokenFeature;
import Constants.Constants_GUI;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        if(node==null)return false;
        if(!Constants.class_type.contains((String) node.value))return false;
        String className = getNameFromClassTypeTreeNode(node);
        if(className == null)return false;
        return !(classHashMap.get(className).get(3) == null);
    }

    public static OPTreeNode searchOPNodeFromName(String name){
        return dataMap.get(name);
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
    // 查找系 wiki用 class类型和unit类型推定
    public static HashMap<String, String> getUniqueNameClassFromType(String type){
        HashMap<String, String> FName_OPName = new HashMap<>();
        for(Map.Entry<String, List<String>> a : classHashMap.entrySet()){
            if(a.getValue().get(3)!=null && a.getValue().get(3).startsWith("cmd"))continue;
            if(type.equals(a.getValue().get(1)) ){
                String Fname = searchFeatureFromClass_one(dataMap.get(a.getKey()), "name");
                System.out.println(a.getKey());
                System.out.println(Fname);
                if(Fname != null){
                    if(FName_OPName.containsKey(Fname)){
                        String OPName_in = FName_OPName.get(Fname);
                        int feas_in = countFeatureOfClassFromName(OPName_in);
                        int feas_now = countFeatureOfClassFromName(a.getKey());
                        if(feas_now > feas_in){
                            FName_OPName.put(Fname, a.getKey());
                        }
                        else if(feas_now==feas_in) {
                            int level_in = getLevelOfClassFromName(OPName_in);
                            int level_now = getLevelOfClassFromName(a.getKey());
                            if (level_now < level_in){
                                FName_OPName.put(Fname, a.getKey());
                            }else {
                                if (OPName_in.length() > a.getKey().length()) {
                                    FName_OPName.put(Fname, a.getKey());
                                }
                            }
                        }
                    }else {
                        FName_OPName.put(Fname, a.getKey());
                    }
                }
            }
        }
        return FName_OPName;
    }
    public static String getTypeOfName(String name){
        if(!classHashMap.containsKey(name)){
            return null;
        }
        return classHashMap.get(name).get(1);
    }
    // 等级越小，越基类
    public static int getLevelOfClassFromName(String name){
        int res = 0;
        OPTreeNode node = dataMap.get(name);
        while (classHasFather(node)){
            res+=1;
            node = classGetFather(node);
        }

        return res;
    }

    public static int countFeatureOfClassFromName(String name){
        int res;
        OPTreeNode node = dataMap.get(name);
        res = node.getChildren().toArray().length;
        System.out.println(name);
        System.out.println(res);
        return res;
    }

    public static String searchFeatureFromClassName_one_to_fathers(String name, String feaName){
        if (!dataMap.containsKey(name)){
            return "";
        }
        return searchFeatureFromClass_one_to_fathers(dataMap.get(name), feaName);
    }
    public static String[] searchFeatureFromClassName_all_to_fathers(String name, String feaName){

        return searchFeatureFromClass_all_to_fathers(dataMap.get(name), feaName);
    }

    public static HashMap<String, String> getAttrMapFromChara(OPTreeNode chara){
        HashMap<String, String> data_map = new HashMap<>();
        var tmp_data = searchFeatureFromClass_all(chara, "consti");
        if (tmp_data!=null){
            Arrays.asList(tmp_data).forEach(d ->{
                data_map.put(d.split("\\*")[0], d.split("\\*")[1]);
            });
        }
        OPTreeNode current_node = chara;
        while (classHasFather(current_node)) {
            current_node = classGetFather(current_node);
            if (current_node != null) {
                var data = searchFeatureFromClass_all(current_node, "consti");
                if(data!=null) {
                    Arrays.asList(data).forEach(d -> {
                        String key = d.split("\\*")[0];
                        String value = d.split("\\*")[1];
                        if (!data_map.containsKey(key)) {
                            data_map.put(key, value);
                        }
                    });
                }
            }
        }

        var tmp = searchFeatureFromClass_one_to_fathers(chara, "class");
        if(tmp!=null) {
            current_node = dataMap.get(tmp);
            while (classHasFather(current_node)) {
                current_node = classGetFather(current_node);
                if (current_node != null) {
                    var data = searchFeatureFromClass_all(current_node, "consti");
                    if (data != null) {
                        Arrays.asList(data).forEach(d -> {
                            String key = d.split("\\*")[0];
                            String value = d.split("\\*")[1];
                            if (!data_map.containsKey(key)) {
                                data_map.put(key, value);
                            }
                        });
                    }
                }
            }
        }
        return data_map;
    }
    public static HashMap<String, String> getAttrMapFromCharaName(String chara){
        if (!dataMap.containsKey(chara)){
            return null;
        }
        return getAttrMapFromChara(dataMap.get(chara));
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
        if(!Objects.equals(classNode.key, TokenClass.TK_classType))return null;
        for(OPTreeNode treeNode : classNode.getChildren()){
            var feature = treeNode.value;

            if(feature instanceof TokenFeature tokenFeature){
                if(Objects.equals(tokenFeature.FeatureName, feaName)){

                    return fullwidthToHalfwidth(tokenFeature.string);
                }
            }
        }
        return null;
    }

    public static String searchFeatureFromClass_one_to_fathers(OPTreeNode classNode, String feaName){
        if(!Objects.equals(classNode.key, TokenClass.TK_classType))return null;
        for(OPTreeNode treeNode : classNode.getChildren()){
            var feature = treeNode.value;
            if(feature instanceof TokenFeature tokenFeature){
                if(Objects.equals(tokenFeature.FeatureName, feaName)){

                    return fullwidthToHalfwidth(tokenFeature.strings_feature[0]);
                }
            }
        }

        if(classGetFather(classNode)!=null){
            return searchFeatureFromClass_one_to_fathers(classGetFather(classNode), feaName);
        }
        return null;
    }

    public static String[] searchFeatureFromClass_all_to_fathers(OPTreeNode classNode, String feaName){
        if(!Objects.equals(classNode.key, TokenClass.TK_classType))return null;
        for(OPTreeNode treeNode : classNode.getChildren()){
            var feature = treeNode.value;
            if(feature instanceof TokenFeature tokenFeature){
                if(Objects.equals(tokenFeature.FeatureName, feaName)){
                    return tokenFeature.strings_feature;
                }
            }
        }

        if(classGetFather(classNode)!=null){
            return searchFeatureFromClass_all_to_fathers(classGetFather(classNode), feaName);
        }
        return null;
    }


    public static String[] searchFeatureFromClass_all(OPTreeNode classNode, String feaName){
        if(!Objects.equals(classNode.key, TokenClass.TK_classType))return null;
        for(OPTreeNode treeNode : classNode.getChildren()){
            var feature = treeNode.value;
            if(feature instanceof TokenFeature tokenFeature){
                if(Objects.equals(tokenFeature.FeatureName, feaName)){
                    return tokenFeature.strings_feature;
                }
            }
        }
        return null;
    }
    public static String[] searchFeatureFromClassName_all(String name, String feaName){
        if (!dataMap.containsKey(name)){
            return new String[]{""};
        }
        return searchFeatureFromClass_all(dataMap.get(name), feaName);
    }

    public static String searchUnitNameFormFuncName(String funcName){
        OPTreeNode node = dataMap.get(funcName);
        if (node==null)return null;
        String name = searchFeatureFromClass_one(node, "name");
        while (name==null&&node.hasFather()){
            node = node.parent;
            name = searchFeatureFromClass_one(node, "name");
        }
        return name;
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
        if(!Objects.equals(OPTreeNode.key, TokenClass.TK_classType))return null;
        for (OPTreeNode treeNode : OPTreeNode.getChildren()){
            if(Objects.equals(treeNode.key, TokenClass.TK_className)){
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

    public static String fullwidthToHalfwidth(String str) {
        // 创建正则表达式匹配器
        Pattern pattern = Pattern.compile("[Ａ-Ｚａ-ｚ]");
        Matcher matcher = pattern.matcher(str);

        System.out.println(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            char fullwidthChar = matcher.group().charAt(0);
            char halfwidthChar = (char) (fullwidthChar - 65248); // 全角字母与半角字母之间的 Unicode 编码差值为 65248
            matcher.appendReplacement(sb, Character.toString(halfwidthChar));
        }
        matcher.appendTail(sb);
        System.out.println(sb.toString());
        return sb.toString();
    }
}
