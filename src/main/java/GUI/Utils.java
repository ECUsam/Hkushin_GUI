package GUI;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

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

    public static boolean openFileManager(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.OPEN)) {
                desktop.open(file);
                return true;
            } else {
                System.out.println("不支持打开文件管理器的操作");
                return false;
            }
        } else {
            System.out.println("指定路径不存在");
            return false;
        }
    }

    public static void sortTree(DefaultMutableTreeNode node) {
        // 使用插入排序对子节点进行排序
        int n = node.getChildCount();
        for (int i = 1; i < n; i++) {
            for (int j = i; j > 0; j--) {
                DefaultMutableTreeNode child1 = (DefaultMutableTreeNode) node.getChildAt(j - 1);
                DefaultMutableTreeNode child2 = (DefaultMutableTreeNode) node.getChildAt(j);
                if (compareNodes(child1, child2) > 0) {
                    node.insert(child2, j - 1);
                    node.insert(child1, j);
                }
            }
        }

        // 递归对子节点进行排序
        Enumeration<TreeNode> children = node.children();
        while (children.hasMoreElements()) {
            TreeNode child = children.nextElement();
            if (child instanceof DefaultMutableTreeNode) {
                sortTree((DefaultMutableTreeNode) child);
            }
        }
    }

    private static int compareNodes(DefaultMutableTreeNode node1, DefaultMutableTreeNode node2) {
        String name1 = node1.toString();
        String name2 = node2.toString();
        return name1.compareToIgnoreCase(name2);
    }
}
