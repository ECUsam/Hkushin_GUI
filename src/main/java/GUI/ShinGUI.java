package GUI;

import Constants.Constants;
import Constants.Constants_GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@SuppressWarnings({"unused"})
public class ShinGUI {
    static ShinGUI application;
    private JFrame frame;
    private JTabbedPane TabbedPane;
    private JMenuBar jMenuBar;

    public static void main(String[] args) {
        //实验
        UIPreProcessor.process();
        SwingUtilities.invokeLater(() -> application = new ShinGUI());

    }
    private ShinGUI(){
        initUI();
    }
    private void initUI(){
        frame = new JFrame(Constants_GUI.get("app_title"));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle(Constants_GUI.get("app_title"));
        frame.setSize(Constants.GUI_width, Constants.GUI_height);
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(new Dimension(1000, 600));

        TabbedPane = new TabbedPane();
        frame.add(TabbedPane, BorderLayout.CENTER);

        jMenuBar = new ShinToolBar(frame);
        frame.setJMenuBar(jMenuBar);
        //frame.add(jMenuBar, BorderLayout.NORTH);

        setApplicationIcon(frame, "src/main/resources/image/biim.png");

        frame.setVisible(true);
    }

    public JFrame getFrame(){
        return frame;
    }

    private static void setApplicationIcon(JFrame frame, String iconPath) {
        try {
            BufferedImage iconImage = ImageIO.read(new File(iconPath));
            frame.setIconImage(iconImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

