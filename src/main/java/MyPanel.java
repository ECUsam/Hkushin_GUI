import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

class CharacterPanel extends JPanel {

    public CharacterPanel() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // 通用设置
        gbc.insets = new Insets(2, 2, 2, 2); // 组件之间的间隔
        gbc.anchor = GridBagConstraints.NORTHWEST; // 对齐方式

        // 名称和数值部分
        addLabelAndField("名称:", 0, 0, gbc);
        addLabelAndField("等级:", 1, 0, gbc);
        addLabelAndField("经验值:", 2, 0, gbc);
        // ... 添加更多的标签和字段

        // 图片部分
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 3; // 占据三行的空间
        gbc.fill = GridBagConstraints.BOTH;
        JLabel imageLabel = new JLabel(new ImageIcon("path/to/your/image.png")); // 替换为图片的路径
        this.add(imageLabel, gbc);

        // 战斗能力部分
        addLabelAndField("最大 HP:", 0, 2, gbc);
        addLabelAndField("最大 MP:", 1, 2, gbc);
        // ... 添加更多的标签和字段

        // 设置边框
        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    }

    private void addLabelAndField(String labelText, int y, int x, GridBagConstraints gbc) {
        // 设置约束条件
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 添加标签
        JLabel label = new JLabel(labelText);
        this.add(label, gbc);

        // 添加字段，可以是JTextField或者JComboBox等
        gbc.gridx = x + 1;
        JTextField field = new JTextField(10);
        this.add(field, gbc);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("角色信息");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new CharacterPanel());
        frame.pack();
        frame.setLocationRelativeTo(null); // 居中显示
        frame.setVisible(true);
    }
}
