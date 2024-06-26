package GUI.Panel;

import Constants.Constants_GUI;
import OPcode.OPTreeNode;
import postfix.Token.Token;
import postfix.Token.TokenClass;
import postfix.Token.TokenCommand;
import postfix.Token.TokenFeature;

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
        currentLevel = 0;
        if(Objects.equals(node.key, TokenClass.TK_classType)){
            for(OPTreeNode treeNode : node.getChildren()){
                postfixNode(treeNode);
            }
        }
        currentLevel = 0;
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
            case TK_className:
                break;
            case Tk_feature:
                if(node.value instanceof TokenFeature feature){
                    cellData.setLevel(currentLevel);
                    cellData.setFunc(feature.FeatureName);
                    cellData.setArgs(feature.strings_feature);
                    cellData.setType(DataType.Fea);
                    this.addElement(cellData);
                }
                break;
            case TK_COMMAND:
                if(node.value instanceof TokenCommand command){
                    cellData.setLevel(currentLevel);
                    cellData.setFunc(command.getCommandName());
                    cellData.setArgs(command.getFeatures());
                    cellData.setType(DataType.Command);
                    this.addElement(cellData);
                }
                break;
            case TK_and: case TK_or:
                cellData.setLevel(currentLevel);
                if(node.value instanceof Token token)
                    cellData.setFunc(token.string);
                cellData.setType(DataType.LogicSymbol);
                this.addElement(cellData);
                break;
            case TK_IF: case TK_RIF:
                cellData.setLevel(currentLevel);
                cellData.setFunc((String) node.value);
                cellData.setType(DataType.LogicSymbol);
                this.addElement(cellData);
                for(OPTreeNode cNode : node.getChildren()){
                    postfixNode(cNode);
                }
                break;
            case TK_expr:
                parseExpr(node);
                break;
            case TK_Block:
                currentLevel+=1;
                for(OPTreeNode exprC : node.getChildren()){
                    postfixNode(exprC);
                }
                currentLevel-=1;
                break;
            case TK_Logic:
                currentLevel+=1;
                for(OPTreeNode exprC : node.getChildren()){
                    postfixNode(exprC);
                }
                cellData.isSpacialType = true;
                cellData.value = "则";
                cellData.level = currentLevel;
                this.addElement(cellData);
                currentLevel-=1;
                break;
        }
    }
    private void parseExpr(OPTreeNode node){
        assert node.key == TokenClass.TK_expr;
        var cellData = new EventCellData();
        cellData.isSpacialType = true;
        cellData.setTreeNode(node);
        cellData.level = currentLevel;
        for(OPTreeNode cNode : node.getChildren()){
            switch (cNode.key){
                case TK_expr:
                    parseExpr(cNode);
                    break;
                case TK_COMMAND:
                    if (cNode.value instanceof TokenCommand cToken) {
                        cellData.CommandTokenAdder(cToken);
                        cellData.value += " ";
                    }
                    break;
                case TK_NUM:
                    if (cNode.value instanceof Token cToken) {
                        if(cToken.toCode()!=null) {
                            cellData.value += cToken.toCode();
                            cellData.value += " ";
                        }
                    }
                    break;
                case TK_IF_LT_OR_EQ:case TK_IF_EQUAL:case TK_IF_GT_OR_EQ:case TK_NOT_EQ:
                    cellData.value += Constants_GUI.getFunction(cNode.key.toString());
                    cellData.value += " ";
                    break;
                default :
                    if (cNode.value instanceof Token cToken) {
                        if(cToken.toCode()!=null) {
                            cellData.value += cToken.toCode();
                            cellData.value += " ";
                        }
                    }
            }
        }
        this.addElement(cellData);
    }
}

enum DataType{
    Fea,
    Command,
    commonToken,
    LogicSymbol,
    NoSense
}