import GUI.Panel.EventCellData;
import GUI.Panel.EventList;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class CustomRendererExample {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Custom Renderer Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            List<Object> dataList = new ArrayList<>();
            dataList.add("Text Data");
            dataList.add(123);
            dataList.add(true);

            EventList customList = new EventList();
            customList.model.addElement(new EventCellData());
            customList.setCellRenderer(new CustomListCellRenderer());

            frame.getContentPane().add(new JScrollPane(customList));
            frame.setSize(300, 200);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

class CustomListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        // 根据数据类型作出不同的响应
        if (value instanceof String) {
            label.setIcon(UIManager.getIcon("FileView.fileIcon"));
            label.setText((String) value);
        } else if (value instanceof Integer) {
            label.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
            label.setText("Number: " + value);
        } else if (value instanceof Boolean) {
            label.setIcon(UIManager.getIcon("OptionPane.questionIcon"));
            label.setText("Boolean: " + value);
        }

        return label;
    }
}
