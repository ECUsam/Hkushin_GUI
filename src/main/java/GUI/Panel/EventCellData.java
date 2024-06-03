package GUI.Panel;

import Constants.Constants_GUI;
import OPcode.OPTreeNode;
import postfix.Token.TokenCommand;
import lombok.Data;

@Data
public class EventCellData{
    public int level;
    public String func;
    public String[] args;
    public DataType type;
    public String nodeKey;
    public OPTreeNode treeNode;
    public boolean isSpacialType = false;
    public String value="";

    public void CommandTokenAdder( TokenCommand command){
        this.setFunc(command.getCommandName());
        this.setArgs(command.getFeatures());
        var res = new StringBuilder();
        res.append(Constants_GUI.getFunction(func));
        res.append("(");
        if(args!=null){
            for(String arg : args){
                res.append(arg);
                res.append("  ");
            }
        }
        res.append(")");
        value+=res;
    }

    @Override
    public String toString(){
        if(isSpacialType)return value;
        var res = new StringBuilder();
        //res.append(" ".repeat(Math.max(0, level)));
        res.append(Constants_GUI.getFunction(func));
        res.append("  ");
        if(args!=null){
            for(String arg : args){
                res.append(arg);
                res.append("  ");
            }
        }
        return res.toString();
    }


}