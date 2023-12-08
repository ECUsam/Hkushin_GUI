package OPcode;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class TreeNode {
    private String key;
    private Object value;
    private List<TreeNode> children;

    public TreeNode(String key, Object value) {
        this.key = key;
        this.value = value;
        this.children = new ArrayList<>();
    }
    public TreeNode(String key){
        this.key = key;
        this.value = null;
    }

    public void addChild(TreeNode childNode) {
        children.add(childNode);
    }

    // 获取子节点列表
    public List<TreeNode> getChildren() {
        return children;
    }

    public Object getValue(){
        return value;
    }

    public String getKey() {
        return key;
    }
}
