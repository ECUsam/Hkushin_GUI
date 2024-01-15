package GUI;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

@SuppressWarnings("unused")
public class UnitPanel extends JPanel {
    private JList jList = new JList<>();
    private JTree tree;
    private DefaultMutableTreeNode root;
    private DefaultTreeModel treeModel;
    private JPanel cards;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    public UnitPanel(){
        super();
        initUI();
    }
    private void initUI(){
        root = new DefaultMutableTreeNode("root");
        treeModel = new DefaultTreeModel(root);
        tree = new JTree(treeModel);

        tree.setCellRenderer(new CustomTreeCellRenderer());
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                TreePath path = tree.getPathForLocation(e.getX(), e.getY());
                if(path!=null){
                    if(e.getClickCount() == 2){
                        System.out.print("path"+ path.getLastPathComponent());
                    }
                }
            }
        });

        this.setLayout(new BorderLayout());


        contentPanel = new JPanel();

        // 使用JSplitPane分隔左右两边的组件
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createTreePanel(), contentPanel);
        splitPane.setDividerLocation(200); // 设置初始分隔条位置
        this.add(splitPane, BorderLayout.CENTER);
//        this.add(new JScrollPane(tree), BorderLayout.WEST);
//        this.add(contentPanel, BorderLayout.CENTER);

//        cards = new JPanel();
//        cardLayout = new CardLayout();
//        cards.setLayout(cardLayout);
//        jList.addListSelectionListener(e -> {
//            if (!e.getValueIsAdjusting()) {
//                String selectedItem = (String) jList.getSelectedValue();
//                cardLayout.show(cards, selectedItem);
//            }
//        });
//
//        this.add(jList);
    }

    public void addList(String unitName){
        jList.add(unitName, new JPanel());
    }

    private JScrollPane createTreePanel() {
        JPanel treePanel = new JPanel(new BorderLayout());
        treePanel.add(tree, BorderLayout.CENTER);
        JScrollPane scrollPane = new JScrollPane(treePanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        return scrollPane;
    }

    public void addNode(String parent, String child) {
        DefaultMutableTreeNode parentNode = findNode(parent);
        if (parentNode != null) {
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
            treeModel.insertNodeInto(childNode, parentNode, parentNode.getChildCount());
            tree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
    }

    private DefaultMutableTreeNode findNode(String nodeValue) {
        Enumeration<TreeNode> e = root.breadthFirstEnumeration();
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
            if (nodeValue.equals(node.getUserObject().toString())) {
                return node;
            }
        }
        return null;
    }

    private class CustomTreeCellRenderer extends DefaultTreeCellRenderer{
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            setIcon(null);
            return this;
            }
        }
}

