package GUI.Panel;

import OPcode.OPTreeNode;
import lombok.Data;

@Data
public class EventCellData{
    public int level;
    public String func;
    public String[] args;
    public DataType type;
    public String nodeKey;
    // TODO：转换为中文选项
    @Override
    public String toString(){
        var res = new StringBuilder();
        res.append("|".repeat(Math.max(0, level)));
        res.append(func);
        res.append(":");
        if(args!=null){
            for(String arg : args){
                res.append(arg);
                res.append("  ");
            }
        }
        return res.toString();
    }

    public OPTreeNode toNode(){
        var node = new OPTreeNode(func);
        return node;
    }

}