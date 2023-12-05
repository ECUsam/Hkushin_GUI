package GUI;

import Constants.Constants;
import Constants.Constants_GUI;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings({"unused"})
public class ShinGUI {
    static ShinGUI application;
    private JFrame frame;
    private JTabbedPane TabbedPane;

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            application = new ShinGUI();
        });
    }
    private ShinGUI(){

        UIManager.put("ToolTip.background", Color.BLUE); // 设置背景色
        UIManager.put("ToolTip.foreground", Color.WHITE); // 设置前景色（字体颜色）
        UIManager.put("ToolTip.font", new Font("Arial", Font.BOLD, 14)); // 设置字体

        initUI();
    }
    private void initUI(){
        frame = new JFrame(Constants_GUI.get("app_title"));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle(Constants_GUI.get("app_title"));
        frame.setSize(Constants.GUI_width, Constants.GUI_height);
        frame.setLocationRelativeTo(null);
        frame.setMaximumSize(new Dimension(800, 600));

        TabbedPane = new TabbedPane();

        frame.add(TabbedPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    public JFrame getFrame(){
        return frame;
    }
}
