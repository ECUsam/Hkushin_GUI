package OPcode;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class TreeNode {
    public String key;
    public Object value;
    private List<TreeNode> children;
    private TreeNode parent;

    public TreeNode(String key, Object value) {
        this.key = key;
        this.value = value;
        this.children = new ArrayList<>();
        parent = null;
    }
    public TreeNode(String key){
        this.key = key;
        this.value = null;
    }

    public void addChild(TreeNode childNode) {
        children.add(childNode);
        parent = this;
    }

    // 获取子节点列表
    public List<TreeNode> getChildren() {
        return children;
    }
}
