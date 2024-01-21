import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class MainPanel extends JPanel {
    private JButton openDialogButton;
    private JLabel selectionLabel;

    public MainPanel() {
        openDialogButton = new JButton("打开列表窗口");
        selectionLabel = new JLabel("选中的项目: 无");
        openDialogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new ListDialog(MainPanel.this);
                dialog.setSize(400, 300);
                dialog.setLocationRelativeTo(MainPanel.this);
                dialog.setVisible(true);
            }
        });

        setLayout(new FlowLayout());
        add(openDialogButton);
        add(selectionLabel);
    }

    public void setSelection(String selection) {
        selectionLabel.setText("选中的项目: " + selection);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Panel 示例");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MainPanel());
        frame.pack();
        frame.setVisible(true);
    }
}
class ListDialog extends JDialog {
    private JTable table;
    private JTextField filterText;
    private MainPanel mainPanel;

    public ListDialog(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        setTitle("选择一个项目");
        setLayout(new BorderLayout());

        filterText = new JTextField(15);
        JPanel filterPanel = new JPanel(new FlowLayout());
        filterPanel.add(new JLabel("过滤:"));
        filterPanel.add(filterText);

        table = new JTable(new DefaultTableModel(new Object[]{"项目"}, 0));
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.addRow(new Object[]{"项目 1"});
        model.addRow(new Object[]{"项目 2"});
        model.addRow(new Object[]{"项目 2"});
        model.addRow(new Object[]{"项目 2"});
        model.addRow(new Object[]{"项目 2"});
        model.addRow(new Object[]{"项目 2"});
        model.addRow(new Object[]{"项目 2"});
        model.addRow(new Object[]{"项目 2"});
        model.addRow(new Object[]{"项目 2"});
        model.addRow(new Object[]{"项目 2"});
        model.addRow(new Object[]{"项目 2"});
        model.addRow(new Object[]{"项目 2"});
        model.addRow(new Object[]{"项目 2"});
        model.addRow(new Object[]{"项目 2"});
        model.addRow(new Object[]{"项目 2"});
        model.addRow(new Object[]{"项目 2"});
        model.addRow(new Object[]{"项目 2"});
        model.addRow(new Object[]{"项目 2"});
        // 添加更多的行...

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        filterText.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                newFilter();
            }

            public void removeUpdate(DocumentEvent e) {
                newFilter();
            }

            public void changedUpdate(DocumentEvent e) {
                newFilter();
            }

            private void newFilter() {
                RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter(filterText.getText(), 0);
                sorter.setRowFilter(rf);
            }
        });

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                if (row >= 0 && evt.getClickCount() == 2) {
                    String selectedData = (String) table.getValueAt(row, 0);
                    mainPanel.setSelection(selectedData);
                    dispose();
                }
            }
        });

        add(filterPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}
