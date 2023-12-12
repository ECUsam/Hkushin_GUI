package OPcode;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class TreeNode {
    public String key;
    public Object value;
    private List<TreeNode> children;
    public TreeNode parent;
    public TreeNode(String key, Object value) {
        this.key = key;
        this.value = value;
        this.children = new ArrayList<>();
        parent = null;
    }
    public TreeNode(String key){
        this.key = key;
        this.value = null;
        this.children = new ArrayList<>();
    }

    public void addChild(TreeNode childNode) {
        children.add(childNode);
        childNode.parent = this;
    }

    // 获取子节点列表
    public List<TreeNode> getChildren() {
        return children;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();

        json.put("value", value);
        json.put("key", key);

        List<JSONObject> jsonChildren = new ArrayList<>();
        for (TreeNode child : children) {
            jsonChildren.add(child.toJSON());
        }

        if(!children.isEmpty())json.put("children", jsonChildren);

        return json;
    }
}
