package GUI.UI;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class RTree extends JTree {
    private static final Color HFC = new Color(205, 232, 255); // 获取焦点时的颜色
    private static final Color LFC = new Color(213, 213, 213); // 失去焦点时的颜色
    private Handler handler;

    public RTree(TreeModel model){
        super(model);
    }
    public RTree(){

    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());

        // 根据焦点状态绘制树节点整行的颜色
        int selectionCount = getSelectionCount();
        if (selectionCount > 0) {
            if (hasFocus()) {
                g.setColor(HFC);
            } else {
                g.setColor(LFC);
            }

            int[] selectedRows = getSelectionRows();
            if (selectedRows != null) {
                for (int i : selectedRows) {
                    if (i >= 0 && i < getRowCount()) {  // 防止越界
                        Rectangle r = getRowBounds(i);
                        g.fillRect(0, r.y, getWidth(), r.height);
                    }
                }
            }
        }

        super.paintComponent(g);
    }

    @Override
    public void updateUI() {
        removeFocusListener(handler);
        super.updateUI();
        setUI(new BasicTreeUI() {
            @Override
            public Rectangle getPathBounds(JTree tree, TreePath path) {
                if (tree != null && treeState != null) {
                    return getPathBounds(path, tree.getInsets(), new Rectangle());
                }
                return null;
            }

            private Rectangle getPathBounds(
                    TreePath path, Insets insets, Rectangle bounds) {
                Rectangle rect = treeState.getBounds(path, bounds);
                if (rect != null) {
                    rect.width = tree.getWidth();
                    rect.y += insets.top;
                }
                return rect;
            }
        });
        handler = new Handler();
        addFocusListener(handler);
        setCellRenderer(handler);
        setOpaque(false);
    }

    static class Handler extends DefaultTreeCellRenderer implements FocusListener {
        @Override
        public Component getTreeCellRendererComponent(
                JTree tree, Object value, boolean selected, boolean expanded,
                boolean leaf, int row, boolean hasFocus) {
            JLabel label = (JLabel) super.getTreeCellRendererComponent(
                    tree, value, selected, expanded, leaf, row, hasFocus);
            // 根据焦点状态设置不同的背景色
            if (hasFocus) {
                label.setBackground(selected ? HFC : tree.getBackground());
            } else {
                label.setBackground(selected ? LFC : tree.getBackground());
            }
            label.setOpaque(true);
            return label;
        }

        @Override
        public void focusGained(FocusEvent e) {
            e.getComponent().repaint();
        }

        @Override
        public void focusLost(FocusEvent e) {
            e.getComponent().repaint();
        }

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("MyTree Test"); // 设置标题
        frame.setSize(600, 500); // 设置大小
        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(150); // 设置分隔线位置
        JScrollPane scrollPane = new JScrollPane();
        RTree tree = new RTree(); // 创建一个树
        scrollPane.setViewportView(tree);
        splitPane.setLeftComponent(scrollPane);
        splitPane.setRightComponent(new JScrollPane(new JTextArea()));
        frame.add(splitPane);
        frame.setLocationRelativeTo(null); // 居中
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置默认关闭操作
        frame.setVisible(true); // 设置可见
    }
}
