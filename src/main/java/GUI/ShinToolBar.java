package GUI;

import Constants.Constants;
import Constants.Constants_GUI;
import FileManager.PathManager;
import FileManager.ScriptReader;
import GUI.INFOR.INFOMessage;
import GUI.INFOR.INFORMATION;
import GUI.INFOR.INFORMATION_TYPE;
import GUI.INFOR.INTERFACE;
import GUI.UI.CustomCheckMenuItem;
import GUI.UI.CustomMenu;
import GUI.UI.CustomMenuItem;
import postfix.DataManger;
import postfix.LogManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Path;

public class ShinToolBar extends JMenuBar implements INTERFACE {

    public CustomMenu fileButton;
    public CustomMenuItem newProject;
    public CustomMenuItem readProject;
    public CustomMenuItem closeProject;
    public CustomMenuItem saveProject;
    public CustomMenuItem exit;

    public CustomMenu showWays;
    public CustomCheckMenuItem showOfFiles;
    public CustomCheckMenuItem showOfRaw;
    public CustomCheckMenuItem showOfScenario;
    public ButtonGroup showBoxGroup;

    public CustomMenu showButton;
    public CustomCheckMenuItem originNameShow;
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
        closeProject.setEnabled(false);
        closeProject.addActionListener(e -> clearData());

        fileButton.add(newProject);
        fileButton.add(readProject);
        fileButton.add(closeProject);
        fileButton.add(saveProject);
        fileButton.addSeparator();
        fileButton.add(exit);
        this.add(fileButton);

        showButton = buttonMaker(Constants_GUI.get("show_button"));
        showButton.setMnemonic(KeyEvent.VK_S);

        originNameShow = JCMaker(Constants_GUI.get("origin_show"));
        originNameShow.setSelected(false);
        originNameShow.addActionListener(e -> {
            // 处理复选框状态变化的逻辑
            boolean isSelected = originNameShow.isSelected();
            INFORMATION information = INFORMATION.getInstance();
            if(isSelected){
                information.sendMessage(INFORMATION_TYPE.ORIGIN_SHOW);
            }else {
                information.sendMessage(INFORMATION_TYPE.NOT_ORIGIN_SHOW);
            }
        });
        originNameShow.setHorizontalAlignment(SwingConstants.LEFT);
        showButton.add(originNameShow);
        showButton.addSeparator();


        showWays = buttonMaker(Constants_GUI.get("show_ways"));
        showWays.setForeground(Color.black);
        showOfFiles = JCMaker(Constants_GUI.get("show_of_files"));
        showOfRaw = JCMaker(Constants_GUI.get("show_of_raw"));
        showOfFiles.setHorizontalAlignment(SwingConstants.LEFT);
        showOfRaw.setHorizontalAlignment(SwingConstants.LEFT);
        showWays.setHorizontalAlignment(SwingConstants.LEFT);
        showWays.add(showOfFiles);
        showWays.add(showOfRaw);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(showOfFiles);
        buttonGroup.add(showOfRaw);
        showOfFiles.setSelected(true); // 设置showOfFiles为默认选中项

        showOfRaw.addActionListener(e -> {
            boolean isSelected = showOfRaw.isSelected();
            INFORMATION information = INFORMATION.getInstance();
            if(isSelected){
                information.sendMessage(INFORMATION_TYPE.LIST_SHOW_WAY_RAW);
            }
        });

        showButton.add(showWays);
        this.add(showButton);
    }

    private void openFolderAndInit() {
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
                    closeProject.setForeground(Color.BLACK);
                    closeProject.setEnabled(true);
                }
            }catch (Exception e) {
                JOptionPane.showMessageDialog(null, Constants_GUI.getDescription("load_error"));
                LogManager.addLog(e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    public void clearData(){
        INFORMATION information = INFORMATION.getInstance();
        information.sendMessage(new INFOMessage(INFORMATION_TYPE.CLEAR_DATA, null));
        closeProject.setForeground(Color.gray);
    }

    private static void saveLastSelectedPath(String path) {
        Constants_GUI.config.setProperty(Constants.LAST_SELECTED_PATH_KEY, path);
        Constants_GUI.saveConfigToFile();
    }
    private static String getLastSelectedPath() {
        return Constants_GUI.config.getProperty(Constants.LAST_SELECTED_PATH_KEY, null);
    }

    private CustomCheckMenuItem JCMaker(String name){
        var item = new CustomCheckMenuItem(name);
        item.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        item.setFont(font);
        item.setForeground(Color.black);
        return item;
    }

    private CustomMenuItem itemMaker(String name){
        var item = new CustomMenuItem(name);
        item.setFont(font);
        item.setForeground(Color.GRAY);
        return item;
    }

    private CustomMenuItem itemMaker(String name, boolean BLACK){
        var item = new CustomMenuItem(name);
        item.setFont(font);
        if(BLACK)item.setForeground(Color.BLACK);
        else item.setForeground(Color.GRAY);
        return item;
    }

    private CustomMenu buttonMaker(String name){
        var button = new CustomMenu(name);
        button.setFont(font);
        button.setForeground(Color.BLACK);

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
