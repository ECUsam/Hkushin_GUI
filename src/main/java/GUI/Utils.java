package GUI;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.*;

public class Utils {
    public static DefaultMutableTreeNode findNode(DefaultMutableTreeNode root, String target) {
        Enumeration<TreeNode> depthFirstEnumeration = root.children();
        while (depthFirstEnumeration.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) depthFirstEnumeration.nextElement();
            if (node.getUserObject().equals(target)) {
                return node;
            }
        }
        return null; // 未找到匹配节点
    }
    public static String getOutOfSpace(String s){
        return s.replace("　", "").replace(" ", "").replace("\t", "");
    }
    // 我在干嘛
    public static List<String> getSortedList(HashMap<String, AbstractMap.SimpleEntry<String, String>> map){
        List<Integer> n = new ArrayList<>();
        for(var entry : map.entrySet()){
            int number = Integer.parseInt(entry.getValue().getValue());
            n.add(number);
        }
        Collections.sort(n);
        List<String> res = new ArrayList<>();
        for(int num : n){
            for(var entry : map.entrySet()){
                int number = Integer.parseInt(entry.getValue().getValue());
                if(num == number && !res.contains(entry.getValue().getKey())) res.add(
                        getOutOfSpace(entry.getValue().getKey())
                );
            }
        }
        return res;
    }
}
