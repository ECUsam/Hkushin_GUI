package GUI;

import Constants.Constants;
import Constants.Constants_GUI;
import FileManager.PathManager;
import FileManager.ScriptReader;
import postfix.DataManger;
import postfix.LogManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Path;

public class ShinToolBar extends JMenuBar implements INTERFACE{

    public JMenu fileButton;
    public JMenuItem newProject;
    public JMenuItem readProject;
    public JMenuItem closeProject;
    public JMenuItem saveProject;
    public JMenuItem exit;
    private final Font font;
    public JFrame Father;

    public ShinToolBar(JFrame F){
        font = new Font("Microsoft YaHei", Font.BOLD, 12);
        initToolBar();
        Father = F;
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
        readProject.addActionListener(e -> openFolderAndInit());

        fileButton.add(newProject);
        fileButton.add(readProject);
        fileButton.add(closeProject);
        fileButton.add(saveProject);
        fileButton.addSeparator();
        fileButton.add(exit);
        this.add(fileButton);
    }

    private static void openFolderAndInit() {
        new Thread(() -> {
            try {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                String lastSelectedPath = getLastSelectedPath();
                if (lastSelectedPath != null) {
                    fileChooser.setCurrentDirectory(new File(lastSelectedPath));
                }

                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                    saveLastSelectedPath(filePath);

                    if(!(PathManager.checkIsDataFolder(Path.of(filePath)) || Path.of(filePath).getFileName().toString().equals("script")))
                        JOptionPane.showMessageDialog(null, Constants_GUI.getDescription("load_file_error"));

                    DataManger.pathManager = new PathManager(filePath);
                    ScriptReader reader = new ScriptReader(DataManger.pathManager);
                    reader.readAll();

                    INFORMATION information = INFORMATION.getInstance();
                    information.sendMessage(new INFOMessage(INFORMATION_TYPE.NEW_PROJECT_CREATED, filePath));
                }
            }catch (Exception e) {
                JOptionPane.showMessageDialog(null, Constants_GUI.getDescription("load_error"));
                LogManager.addLog(e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    private static void saveLastSelectedPath(String path) {
        Constants_GUI.config.setProperty(Constants.LAST_SELECTED_PATH_KEY, path);
        Constants_GUI.saveConfigToFile();
    }
    private static String getLastSelectedPath() {
        return Constants_GUI.config.getProperty(Constants.LAST_SELECTED_PATH_KEY, null);
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

    @Override
    public void update(INFORMATION_TYPE informationType, Object message) {

    }
}
