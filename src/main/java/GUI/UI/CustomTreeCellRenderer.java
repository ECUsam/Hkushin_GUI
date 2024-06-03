package GUI.UI;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class CustomTreeCellRenderer  extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                                                  boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        String nodeType = node.getUserObject().toString();
        if(nodeType.endsWith(".dat")){
            setIcon(UIManager.getIcon("FileView.fileIcon"));
        }
        if ("Folder".equals(nodeType)) {
            setIcon(UIManager.getIcon("FileView.directoryIcon"));
        }
        if(leaf)setIcon(null);
        return this;
    }
}
