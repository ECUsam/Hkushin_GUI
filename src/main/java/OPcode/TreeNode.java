package OPcode;

import Constants.Constants;
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
    private String father;
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

    public void getFather(String father){
        this.father = father;
    }
    public boolean hasFather(){
        return !(father==null);
    }
    public String takeFather(){
        return father;
    }


    public void addChild(TreeNode childNode) {
        children.add(childNode);
        childNode.parent = this;
    }
    // 调试用
    public void getAllParents(){
        System.out.print(this.key+"  ");
        if(this.parent != null){
            parent.getAllParents();
        }

    }

    public boolean hasChild(){
        return !children.isEmpty();
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

    private boolean isNoNameType(){
        return Constants.ClassNoName.contains(key);
    }

    public String Tree2Code(){
        if(value instanceof Token)return ((Token) value).toCode();
        return (String) value;
    }
    // 节点返回表达式字符串就可以
    @Deprecated
    public String toCode(){
        StringBuilder code = new StringBuilder();
        if(value instanceof Token){
            code.append(((Token) value).toCode());
            if(!Objects.equals(this.parent.key, "expr")) code.append('\n');
        }else if(value!=null){
            if(Objects.equals(key, "if"))code.append("(");
            code.append(value);
            if(Objects.equals(this.key, "className")){code.append("{\n");}
            code.append(" ");
            if(Objects.equals(key, "classType")&&isNoNameType())code.append("\n{");
        }

        for (TreeNode child : children) {
            code.append(child.toCode());
        }
        return code.toString();
    }
}

