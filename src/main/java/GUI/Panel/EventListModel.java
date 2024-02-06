package GUI.Panel;

import OPcode.OPTreeNode;
import Token.TokenCommand;
import Token.TokenFeature;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EventListModel extends DefaultListModel {
    private List<EventCellData> data;
    private int currentLevel;

    public EventListModel(){
        data = new ArrayList<>();
    }

    @Override
    public int getSize() {
        return data.size();
    }

    @Override
    public EventCellData getElementAt(int index) {
        return data.get(index);
    }

    public void addElement(EventCellData s){
        data.add(s);
    }

    public void update(OPTreeNode node){
        currentLevel = 0;
        if(Objects.equals(node.key, "classType")){
            for(OPTreeNode treeNode : node.getChildren()){
                postfixNode(treeNode);
            }
        }
    }

    public void postfixNode(OPTreeNode node){
        this.addElement(new EventCellData());
        switch (node.key){
            case "className":
                break;
            case "Feature":
                if(node.value instanceof TokenFeature feature){
                    var cellData = new EventCellData();
                    cellData.setLevel(currentLevel);
                    cellData.setFunc(feature.FeatureName);
                    cellData.setArgs(feature.strings_feature);
                    cellData.setType(DataType.Fea);
                    this.addElement(cellData);
                }
                break;
            case "Command":
                if(node.value instanceof TokenCommand command){
                    var cellData = new EventCellData();
                    cellData.setLevel(currentLevel);
                    cellData.setFunc(command.getCommandName());
                    cellData.setArgs(command.getFeatures());
                    cellData.setType(DataType.LogicSymbol);
                    this.addElement(cellData);
                }
                break;
            case "if": case "rif":
                var cellData = new GUI.Panel.EventCellData();
                cellData.setLevel(currentLevel);
                cellData.setFunc((String) node.value);
                cellData.setType(DataType.Command);
            case "expr":
                currentLevel+=1;
                for(OPTreeNode exprC : node.getChildren()){
                    postfixNode(exprC);
                }
                break;
        }
        for(OPTreeNode cNode : node.getChildren()){
            postfixNode(cNode);
        }
    }

}

enum DataType{
    Fea,
    Command,
    commonToken,
    LogicSymbol,
    NoSense
}