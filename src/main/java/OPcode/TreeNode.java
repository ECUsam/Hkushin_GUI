package OPcode;

import Token.Token;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public String toCode(){
        StringBuilder code = new StringBuilder();
        if(value instanceof Token){
            code.append(((Token) value).toCode());
            if(!Objects.equals(this.parent.key, "expr")) code.append('\n');
        }else if(value!=null){
            code.append(value);
            code.append('\n');
        }
        if(!children.isEmpty())code.append('\t');
        for (TreeNode child : children) {
            code.append(child.toCode());
        }
        return code.toString();
    }
}
