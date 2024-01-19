import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TableExample {
    public static void main(String[] args) {
        // 创建 JFrame 实例
        JFrame frame = new JFrame("Table Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 创建表格模型
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 1 ? Integer.class : Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };

        model.addColumn("类型");
        model.addColumn("强度");

        // 创建表格并设置模型
        JTable table = new JTable(model);

        // 添加鼠标监听器以处理右键点击
        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopup(e);
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopup(e);
                }
            }

            private void showPopup(MouseEvent e) {
                JPopupMenu popup = new JPopupMenu();
                JMenuItem addItem = new JMenuItem("添加新行");
                addItem.addActionListener(ae -> {
                    model.addRow(new Object[]{null, null});
                    ensureRows(model);
                });
                popup.add(addItem);
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        });

        // 确保表格始终有10行
        ensureRows(model);

        // 将表格添加到滚动面板，并将滚动面板添加到框架
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);

        // 设置框架的大小
        frame.setSize(500, 200);
        // 显示框架
        frame.setVisible(true);
    }

    // 确保表格始终至少有10行
    private static void ensureRows(DefaultTableModel model) {
        while (model.getRowCount() < 10) {
            model.addRow(new Object[]{null, null});
        }
    }
}
