package GUI.Panel;

import FileManager.PathManager;
import GUI.INFOR.INFORMATION;
import GUI.INFOR.INFORMATION_TYPE;
import GUI.INFOR.INTERFACE;
import GUI.NodeData;
import GUI.UI.RTree;
import GUI.Utils;
import OPcode.OPTreeNode;
import postfix.DataManger;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EventPanel extends JPanel implements INTERFACE {
    private JTree tree;
    private DefaultMutableTreeNode root;
    private DefaultTreeModel treeModel;
    private JPanel contentPanel;
    private JSplitPane splitPane;
    private EventList ejList;
    private INFORMATION_TYPE showWay = INFORMATION_TYPE.LIST_SHOW_WAY_FLIES;

    public EventPanel(){
        super();
        initUI();
    }

    private void initUI(){
        INFORMATION information = INFORMATION.getInstance();
        information.addObserver(this::update);

        treeTableInit();
        this.setLayout(new BorderLayout());
        contentPanel = new JPanel();
        getContentPanelSetting();

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createTreePanel(), contentPanel);
        splitPane.setDividerLocation(250);
        this.add(splitPane, BorderLayout.CENTER);

        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                TreePath path = tree.getPathForLocation(e.getX(), e.getY());
                if(path!=null){
                    tree.setSelectionPath(path);
                    if(e.getClickCount() == 2){
                        Object lastPathComponent = path.getLastPathComponent();
                        if (lastPathComponent instanceof DefaultMutableTreeNode node) {
                            Object userObject = node.getUserObject();
                            if (userObject instanceof NodeData nodeData) {
                                String className = nodeData.getKey();
                                OPTreeNode OPTreeNode = DataManger.dataMap.get(className);
                                ejList.model.clearData();
                                ejList.model.update(OPTreeNode);
                            }
                        }
                    }
                    if (SwingUtilities.isRightMouseButton(e)) {
                        if (e.getClickCount() == 1) {
                            // showPopupMenuTree(e.getX(),e.getY(), path);
                        }
                    }
                }
            }
        });
    }

    private void treeTableInit() {
        root = new DefaultMutableTreeNode("root");
        treeModel = new DefaultTreeModel(root);
        tree = new RTree(treeModel);

        tree.setRowHeight(25); // 设置行高
        Font font = new Font("Microsoft YaHei", Font.PLAIN, 12);
        tree.setFont(font);
        tree.setRootVisible(false);
        tree.setCellRenderer(new UnitPanel.CustomTreeCellRenderer());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        //tree.setBackground(new Color(240, 240, 240));
    }


    private void getContentPanelSetting(){
        ejList = new EventList();
        JScrollPane scrollPane = new JScrollPane(ejList);
        contentPanel.setLayout(new BorderLayout()); // 设置contentPanel的布局管理器为BorderLayout
        contentPanel.add(scrollPane, BorderLayout.CENTER); // 将ejList添加到contentPanel的中间位置
    }

    private JScrollPane createTreePanel() {
        JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.setBorder(new MatteBorder(3, 3, 3, 3, Color.lightGray)); // 设置边框宽度和颜色
        // 设置滚动面板的背景颜色
        scrollPane.setBackground(Color.LIGHT_GRAY);

        return scrollPane;
    }

    private void treeUpdateRaw(){
        root.removeAllChildren();
        treeModel.reload();
        for(var data : DataManger.classHashMap.entrySet()){
            if("event".equals(data.getValue().get(1))){
                OPTreeNode opTreeNode = DataManger.dataMap.get(data.getKey());
                String name = DataManger.searchFeatureFromClass_one(opTreeNode, "name");
                if(name!=null)root.add(new DefaultMutableTreeNode(new NodeData(data.getKey(), name)));
                else root.add(new DefaultMutableTreeNode(new NodeData(data.getKey(), data.getKey())));
            }
        }
        Utils.sortTree(root);
        treeModel.reload();
    }

    @Override
    public void update(INFORMATION_TYPE informationType, Object message) {
        if (informationType == INFORMATION_TYPE.NEW_PROJECT_CREATED){
            for(var data : DataManger.classHashMap.entrySet()){
                if("event".equals(data.getValue().get(1))){
                    String rePath = PathManager.rePathString(data.getValue().get(0), (String) message);
                    OPTreeNode opTreeNode = DataManger.dataMap.get(data.getKey());
                    String name = DataManger.searchFeatureFromClass_one(opTreeNode, "name");
                    if(name!=null)putRePath2Root(rePath, data.getKey(), name);
                    else putRePath2Root(rePath, data.getKey(), data.getKey());
                }
            }
            Utils.sortTree(root);
            treeModel.reload();
        }
        else if(informationType == INFORMATION_TYPE.LIST_SHOW_WAY_RAW){
            showWay = informationType;
            treeUpdateRaw();
        }else if(informationType == INFORMATION_TYPE.LIST_SHOW_WAY_FLIES){
            showWay = informationType;
        }
    }

    public void putRePath2Root(String rePath, String key, String value){
        String[] files = rePath.split("\\\\");
        int length = files.length;
        DefaultMutableTreeNode tempNode = root;
        for (int i = 0;i<length;i++) {
            String file = files[i];
            var node = Utils.findNode(tempNode, file);
            if (node == null) {
                node = new DefaultMutableTreeNode(file);
                tempNode.add(node);
            }
            tempNode = node;
            if(i==length-1){
                tempNode.add(new DefaultMutableTreeNode(new NodeData(key, value)));
            }
        }
    }
}
