package GUI.Panel;

import OPcode.OPTreeNode;
import Token.TokenCommand;
import Token.TokenFeature;

import javax.swing.*;
import java.util.Objects;
import java.util.Vector;

public class EventListModel extends DefaultListModel<EventCellData> {
    private Vector<EventCellData> delegate;
    private int currentLevel;

    public EventListModel(){
        delegate = new Vector<>();
    }

    @Override
    public int getSize() {
        return delegate.size();
    }

    public void clearData(){
        removeAllElements();
        fireContentsChanged(this, 0, getSize() - 1);
        currentLevel = 0;
    }

    public void removeAllElements() {
        int index1 = delegate.size()-1;
        delegate.removeAllElements();
        if (index1 >= 0) {
            fireIntervalRemoved(this, 0, index1);
        }
    }

    @Override
    public EventCellData getElementAt(int index) {
        return delegate.get(index);
    }

    public void addElement(EventCellData s){
        delegate.add(s);
        adjustListCellSize();
    }

    public void update(OPTreeNode node){
        currentLevel = 1;
        if(Objects.equals(node.key, "classType")){
            for(OPTreeNode treeNode : node.getChildren()){
                postfixNode(treeNode);
            }
        }
        currentLevel = 1;
        fireContentsChanged(this, 0, getSize() - 1);
    }

    private void adjustListCellSize() {
        EventList list = new EventList();
        list.setModel(this);
        list.setFixedCellWidth(list.getWidth());
        list.setPrototypeCellValue(list.getModel().getElementAt(0));
    }

    public void postfixNode(OPTreeNode node){
        var cellData = new EventCellData();
        cellData.setTreeNode(node);
        switch (node.key){
            case "className":
                break;
            case "TK_Feature":
                if(node.value instanceof TokenFeature feature){
                    cellData.setLevel(currentLevel);
                    cellData.setFunc(feature.FeatureName);
                    cellData.setArgs(feature.strings_feature);
                    cellData.setType(DataType.Fea);
                    this.addElement(cellData);
                }
                break;
            case "TK_COMMAND":
                if(node.value instanceof TokenCommand command){
                    cellData.setLevel(currentLevel);
                    cellData.setFunc(command.getCommandName());
                    cellData.setArgs(command.getFeatures());
                    cellData.setType(DataType.Command);
                    this.addElement(cellData);
                }
                break;
            case "if": case "rif":
                cellData.setLevel(currentLevel);
                cellData.setFunc((String) node.value);
                cellData.setType(DataType.LogicSymbol);
                this.addElement(cellData);
                for(OPTreeNode cNode : node.getChildren()){
                    postfixNode(cNode);
                }
                break;
            case "expr":
                parseExpr(node);
             case "Logic": case "Block":
                currentLevel+=1;
                for(OPTreeNode exprC : node.getChildren()){
                    postfixNode(exprC);
                }
                currentLevel-=1;
                break;
        }
    }
    private void parseExpr(OPTreeNode node){
        switch (node.key){
            default :

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