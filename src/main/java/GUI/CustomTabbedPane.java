package GUI;

import javax.swing.*;
import java.awt.*;

public class CustomTabbedPane extends JFrame {
    private JTabbedPane tabbedPane;

    public CustomTabbedPane() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);

        tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        addTab("Tab 啊啊", "Content of Tab 1");
        addTab("Tab 2", "Content of Tab 2");
        addTab("Tab 3", "Content of Tab 3");

        add(tabbedPane, BorderLayout.CENTER);
    }

    private void addTab(String title, String content) {
        // 创建标签内容
        JPanel panel = new JPanel();
        panel.add(new JLabel(content));

        // 创建一个自定义的标签组件
        JLabel label = new JLabel(title);
        label.setPreferredSize(new Dimension(100, 50)); // 设置标签大小
        label.setHorizontalAlignment(JLabel.CENTER); // 水平居中对齐
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // 添加边距

        // 设置标签的字体大小
        label.setFont(new Font("Dialog", Font.BOLD, 14));

        // 将自定义标签组件添加到 JTabbedPane
        tabbedPane.addTab(null, panel); // 使用 null 作为标题，因为我们用自定义组件代替
        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, label);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CustomTabbedPane frame = new CustomTabbedPane();
            frame.setVisible(true);
        });
    }
}
