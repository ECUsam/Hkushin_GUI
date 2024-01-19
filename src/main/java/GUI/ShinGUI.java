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
    private JMenuBar jMenuBar;

    public static void main(String[] args) {
        //实验
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // 如果 Nimbus 不可用，则设置默认的 Look and Feel
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        SwingUtilities.invokeLater(() -> application = new ShinGUI());
    }
    private ShinGUI(){
        UIManager.put("ToolTip.background", Color.BLUE); // 设置背景色
        UIManager.put("ToolTip.foreground", Color.WHITE); // 设置前景色（字体颜色）
        UIManager.put("ToolTip.font", new Font("Microsoft YaHei", Font.BOLD, 14)); // 设置字体
        UIManager.put("MenuItem.selectionBackground", Color.LIGHT_GRAY);
        UIManager.put("Menu.selectionBackground", Color.LIGHT_GRAY);

        initUI();
    }
    private void initUI(){
        frame = new JFrame(Constants_GUI.get("app_title"));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle(Constants_GUI.get("app_title"));
        frame.setSize(Constants.GUI_width, Constants.GUI_height);
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(new Dimension(800, 600));

        TabbedPane = new TabbedPane();
        frame.add(TabbedPane, BorderLayout.CENTER);

        jMenuBar = new ShinToolBar(frame);
        frame.setJMenuBar(jMenuBar);
        //frame.add(jMenuBar, BorderLayout.NORTH);

        frame.setVisible(true);
    }

    public JFrame getFrame(){
        return frame;
    }
}

