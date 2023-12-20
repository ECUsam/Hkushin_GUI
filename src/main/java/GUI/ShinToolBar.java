package GUI;

import Constants.Constants_GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ShinToolBar extends JMenuBar {

    public JMenu fileButton;
    public JMenuItem newProject;
    public JMenuItem readProject;
    public JMenuItem closeProject;
    public JMenuItem saveProject;
    public JMenuItem exit;
    private final Font font;

    public ShinToolBar(){
        font = new Font("Microsoft YaHei", Font.BOLD, 12);
        initToolBar();
    }

    public void initToolBar(){


        fileButton = buttonMaker(Constants_GUI.get("file"));
        newProject = itemMaker(Constants_GUI.get("new_project"));
        readProject = itemMaker(Constants_GUI.get("read_project"), true);
        closeProject = itemMaker(Constants_GUI.get("close_project"));
        saveProject = itemMaker(Constants_GUI.get("save_project"));
        exit = itemMaker(Constants_GUI.get("exit"));

        fileButton.setMnemonic(KeyEvent.VK_F);

        readProject.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        saveProject.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
        exit.addActionListener(e -> {
            System.exit(0);
        });
        readProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFolderAndInit();
            }
        });


        fileButton.add(newProject);
        fileButton.add(readProject);
        fileButton.add(closeProject);
        fileButton.add(saveProject);
        fileButton.addSeparator();
        fileButton.add(exit);
        this.add(fileButton);
    }

    private static void openFolderAndInit(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(null);

        if(result == JFileChooser.APPROVE_OPTION){
            // TODO:加载逻辑
        }else {
            JOptionPane.showMessageDialog(null, Constants_GUI.getDescription("load_error"));
        }

    }

    private JMenuItem itemMaker(String name){
        var item = new JMenuItem(name);
        item.setFont(font);
        item.setForeground(Color.GRAY);
        return item;
    }

    private JMenuItem itemMaker(String name, boolean BLACK){
        var item = new JMenuItem(name);
        item.setFont(font);
        if(BLACK)item.setForeground(Color.BLACK);
        else item.setForeground(Color.GRAY);
        return item;
    }

    private JMenu buttonMaker(String name){
        var button = new JMenu(name);
        button.setFont(font);
        button.setForeground(Color.GRAY);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setOpaque(true);
                button.setBackground(Color.LIGHT_GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setOpaque(false);
                button.setBackground(null);
            }
        });
        return button;
    }
}
