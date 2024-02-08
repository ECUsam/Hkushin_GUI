package GUI;

import postfix.LogManager;

import javax.swing.*;
import javax.swing.plaf.InsetsUIResource;
import java.awt.*;

public class UIPreProcessor {
    // 全局字体抗锯齿，必须在初始化 UIManager 之前调用！
    private static void enableAntiAliasing() {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
    }

    // 统一 Laf 为 Nimbus，避免界面元素混乱
    private static void initLaf() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Flatlaf".equals(info.getName())) {
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
    }

    // 初始化 UI 管理器配置
    private static void initUIManager() {
        UIManager.put("ToolTip.background", Color.BLUE); // 设置背景色
        UIManager.put("ToolTip.foreground", Color.WHITE); // 设置前景色（字体颜色）
        UIManager.put("ToolTip.font", new Font("Microsoft YaHei", Font.BOLD, 14)); // 设置字体
        UIManager.put("MenuItem.selectionBackground", Color.LIGHT_GRAY);
        UIManager.put("Menu.selectionBackground", Color.LIGHT_GRAY);
        // 列表不按照行块为单位滚动，提升动画流畅性
        UIManager.put("List.lockToPositionOnScroll", false);
        UIManager.put("TabbedPane.tabInsets", new InsetsUIResource(0, 4, 0, 4));
    }

    public static void process() {
        enableAntiAliasing();
        initLaf();
        initUIManager();
    }
}
