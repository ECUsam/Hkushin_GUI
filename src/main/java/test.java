import javax.swing.*;

public class test {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String value = "要缩进的部分文本";

        // 创建一个带有HTML格式的JLabel
        JLabel label = new JLabel();
        label.setText("<html><div style='margin-left: 30px;'>" + value + "</div></html>");

        frame.getContentPane().add(label);
        frame.pack();
        frame.setVisible(true);
    }
}
