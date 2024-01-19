package GUI;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.Enumeration;

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
}
