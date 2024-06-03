package GUI.Panel;

import GUI.UI.RTree;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TreeAndContentPanel extends JPanel {
    public RTree listTree;
    public JPanel contentPanel;
    private JSplitPane splitPanel;
    public TreeAndContentPanel(RTree tree, JPanel content){
        super();
        listTree = tree;
        contentPanel = content;
    }
    private void init(){
        splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listTree, contentPanel);
        splitPanel.setDividerLocation(250);
        this.add(splitPanel, BorderLayout.CENTER);
    }
    private void treeSetting(){
        listTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event){
                TreePath path = listTree.getPathForLocation(listTree.getX(), getY());
                if(path != null){
                    listTree.setSelectionPath(path);
                    if(SwingUtilities.isLeftMouseButton(event)) {
                        if (event.getClickCount() == 1) {
                            treeClickedOnce(event, path);
                        }else if(event.getClickCount() == 2){
                            treeClickedTwice(event, path);
                        }
                    }else if(SwingUtilities.isRightMouseButton(event)){
                        if (event.getClickCount() == 1) {
                            treeRightClickedOnce(event, path);
                        }
                    }
                }
            }
        });
    }
    public void treeClickedTwice(MouseEvent event, TreePath path){}

    public void treeClickedOnce(MouseEvent event, TreePath path){}

    public void treeRightClickedOnce(MouseEvent event, TreePath path){}
}
