package GUI;

import Constants.Constants;
import FileManager.PathManager;
import postfix.DataManger;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.HashMap;


class shared{
    static HashMap<String, JComponent> componentHashMap;
}
@SuppressWarnings("unused")
public class UnitPanel extends JPanel implements INTERFACE{
    private JList jList = new JList<>();
    private JTree tree;
    private DefaultMutableTreeNode root;
    private DefaultTreeModel treeModel;
    private JPanel cards;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private basicSetting basicSetting;
    // 包私有
    public UnitPanel(){
        super();
        shared.componentHashMap = new HashMap<>();
        initUI();
    }
    private void initUI(){
        INFORMATION information = INFORMATION.getInstance();
        information.addObserver(this::update);
        root = new DefaultMutableTreeNode("I am root");
        treeModel = new DefaultTreeModel(root);
        tree = new JTree(treeModel);
        tree.setRowHeight(25); // 设置行高
        Font font = new Font("Microsoft YaHei", Font.PLAIN, 12);
        tree.setFont(font);
        tree.setRootVisible(false);
        tree.setCellRenderer(new CustomTreeCellRenderer());


        this.setLayout(new BorderLayout());

        contentPanel = new JPanel();
        getContentPanelSetting();


        // 使用JSplitPane分隔左右两边的组件
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createTreePanel(), contentPanel);
        splitPane.setDividerLocation(200); // 设置初始分隔条位置
        this.add(splitPane, BorderLayout.CENTER);


        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                TreePath path = tree.getPathForLocation(e.getX(), e.getY());
                if(path!=null){
                    if(e.getClickCount() == 2){
                        String unitName = path.getLastPathComponent().toString();
                        OPcode.TreeNode treeNode = DataManger.dataMap.get(unitName);
                        basicSetting.update();
                    }
                }
            }
        });
    }

    private void getContentPanelSetting(){
        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        contentPanel.setLayout(new BorderLayout());
        basicSetting = new basicSetting();
        var downPanel = new JPanel();

        JScrollPane basicSettingScrollPane = new JScrollPane(basicSetting);
        basicSettingScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        basicSettingScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, basicSettingScrollPane, downPanel);
        splitPane.setDividerLocation(400);
        contentPanel.add(splitPane, BorderLayout.CENTER);
    }

    public void addList(String unitName){
        jList.add(unitName, new JPanel());
    }

    private JScrollPane createTreePanel() {
        JScrollPane scrollPane = new JScrollPane(tree);

        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        return scrollPane;
    }

    public void addNode(String parent, String child) {
        DefaultMutableTreeNode parentNode = findNode(parent);
        if (parentNode != null) {
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
            treeModel.insertNodeInto(childNode, parentNode, parentNode.getChildCount());
            tree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
    }

    private DefaultMutableTreeNode findNode(String nodeValue) {
        Enumeration<TreeNode> e = root.breadthFirstEnumeration();
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
            if (nodeValue.equals(node.getUserObject().toString())) {
                return node;
            }
        }
        return null;
    }

    @Override
    public void update(INFORMATION_TYPE informationType, Object message) {
        if(informationType == INFORMATION_TYPE.NEW_PROJECT_CREATED){
            for(var data : DataManger.classHashMap.entrySet()){
                if("unit".equals(data.getValue().get(1))){
                    String rePath = PathManager.rePathString(data.getValue().get(0), (String) message);
//                    System.out.print(rePath+"\n");
                    putRePath2Root(rePath, data.getKey());
                }
            }
            treeModel.reload();
        }
    }

    public void putRePath2Root(String rePath, String name){
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
                tempNode.add(new DefaultMutableTreeNode(name));
            }
        }
    }

    private class CustomTreeCellRenderer extends DefaultTreeCellRenderer{
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                String nodeType = node.getUserObject().toString();
                if(nodeType.endsWith(".dat")){
                    setIcon(UIManager.getIcon("FileView.fileIcon"));
                }
                if ("Folder".equals(nodeType)) {
                    setIcon(UIManager.getIcon("FileView.directoryIcon"));
                }
                if(leaf)setIcon(null);
            return this;
            }
        }
}

class basicSetting extends JPanel {
    private JLabel headImage;
    private GridBagConstraints gbc;
    private NumberFormatter numberFormatter;
    private JPanel skillArea;


    public basicSetting() {
        NumberFormat integerFormat = NumberFormat.getIntegerInstance();
        numberFormatter = new NumberFormatter(integerFormat);
        numberFormatter.setValueClass(Integer.class);
        numberFormatter.setAllowsInvalid(false); // 不允许非法输入
        numberFormatter.setMinimum(0); // 最小值
        numberFormatter.setMaximum(Integer.MAX_VALUE); // 最大值

        init();
        alignTextFieldsRight(this);
    }

    public void init() {
        this.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(3, 4, 3, 4);
        JLabel headText = new JLabel("基本设置");
        headText.setFont(Constants.fontB);
        adder(headText, 0, 0);

        headImage = new JLabel(new ImageIcon(createPatternPlaceholderImage()));

        gbc.gridwidth = 2;
        adder(headImage, 0, 1);

        LabelAdder_string("名称");
        LabelAdder_string("称号");
        LabelAdder("最大HP");
        LabelAdder("最大MP");
        LabelAdder("攻击");
        LabelAdder("防御");
        LabelAdder("魔力");
        LabelAdder("魔抗");
        LabelAdder("速度");
        LabelAdder("技术");
        LabelAdder("HP回复");
        LabelAdder("移动");
        LabelAdder("召唤");
        LabelAdder("财政");
        LabelAdder("独立势力名");

        skillArea = new skillPanel();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 3;
        gbc.gridwidth = 10;
        adder(skillArea, 2, 0);
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        JComboBox<String> comboBox = comboBoxGetter(new String[]{"option1", "选项二"});
        JLabel race = new JLabel("种族");
        gbc.gridwidth = 2;
        adder(comboBox, 3, 3);
        gbc.gridwidth = 1;
        adder(race, 2, 3);

        DefaultTableModel tableModel = new AttrModel();
        JTable table = new AttrTable(tableModel);
        MouseAndTable(table);
        // test
        tableModel.addRow(new Object[]{"类型1", 1});
        tableModel.addRow(new Object[]{"类型2", 5});
        tableModel.addRow(new Object[]{"类型3", 10});
        JScrollPane AttrPane = new JScrollPane(table);
        AttrPane.setPreferredSize(new Dimension(250, 350));

        JLabel attr = new JLabel("抗性");
        adder(attr, 2, 6);
        gbc.gridheight = 3;
        gbc.gridwidth = 2;
        adder(AttrPane, 2, 7);

        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridy = 99;
        gbc.gridx = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.BOTH;
        this.add(Box.createGlue(), gbc);
    }

    private void MouseAndTable(JTable table){
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 检查点击事件是否在表格外部发生
                if (!table.contains(e.getPoint())) {
                    table.clearSelection(); // 清除表格的选择
                }
            }
        });
        for (Component c : this.getComponents()) {
            if (!(c instanceof JTable)) {
                c.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (!table.contains(e.getPoint())) {
                            table.clearSelection();
                        }
                    }
                });
            }
        }
    }

    private JComboBox<String> comboBoxGetter(String[] strings){
        JComboBox<String> comboBox = new JComboBox<>();
        for (String s:strings){
            comboBox.addItem(s);
        }
        return comboBox;
    }

    private void LabelAdder(String name){
        gbc.gridwidth = 1;
        JLabel label = new JLabel(name);
        var label_ = new JFormattedTextField(numberFormatter);
        label_.setColumns(10);
        label.setLabelFor(label_);
        shared.componentHashMap.put(name, label_);
        gbc.gridy++;
        gbc.gridx = 0;
        this.add(label, gbc);
        gbc.gridx = 1;
        this.add(label_, gbc);
    }

    private void LabelAdder_string(String name){
        gbc.gridwidth = 1;
        JLabel label = new JLabel(name);
        var label_ = new JFormattedTextField();
        label_.setColumns(10);
        gbc.gridy++;
        gbc.gridx = 0;
        this.add(label, gbc);
        gbc.gridx = 1;
        this.add(label_, gbc);
    }

    private void adder(JComponent component, int x, int y){
        gbc.gridx = x;
        gbc.gridy = y;
        this.add(component, gbc);
    }

    public void displayImage(File file) {
        ImageIcon imageIcon = new ImageIcon(file.getPath());
        Image scaledImage = imageIcon.getImage().getScaledInstance(
                100, // 指定宽度
                100, // 指定高度
                Image.SCALE_SMOOTH
        );
        headImage.setIcon(new ImageIcon(scaledImage));
    }

    private BufferedImage createPatternPlaceholderImage() {
        int width = 100;
        int height = 100;
        BufferedImage placeholderImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = placeholderImage.createGraphics();

        for (int i = 0; i < width; i += 20) {
            for (int j = 0; j < height; j += 20) {
                if ((i / 20 + j / 20) % 2 == 0) {
                    g2d.setColor(new Color(192, 192, 192, 128)); // 透明灰色
                } else {
                    g2d.setColor(Color.WHITE);
                }
                g2d.fillRect(i, j, 20, 20);
            }
        }
        g2d.dispose();
        return placeholderImage;
    }

    private void alignTextFieldsRight(Container container) {
        for (Component c : container.getComponents()) {
            if (c instanceof JTextField) {
                ((JTextField) c).setHorizontalAlignment(JTextField.RIGHT);
            }
        }
    }

    public void update(){

    }

}

class skillPanel extends JPanel{
    private GridBagConstraints gbc;
    public skillPanel(){
        init();
    }
    private void init(){
        this.setBorder(new ShadowBorder(10));
        this.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(3, 2, 3, 2);
        JLabel headText = new JLabel("技能");
        headText.setFont(Constants.fontB);
        adder(headText, 0, 0);

        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.BOTH;
        this.add(Box.createGlue(), gbc); // 添加一个透明的占位组件
    }

    private void adder(JComponent component, int x, int y){
        gbc.gridx = x;
        gbc.gridy = y;
        this.add(component, gbc);
    }
}

class AttrModel extends DefaultTableModel{
    public AttrModel(){
        super();
        this.addColumn("类型");
        this.addColumn("强度");
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (column == 1) {
            if(super.getValueAt(row,column)==null){
                return null;
            }
            int value = (int) super.getValueAt(row, column);
            if(value==0)return "即死";
            if (value == 1) return "激弱";
            else if (value<=3)return "弱";
            else if (value==4)return "微弱";
            else if (value==5)return "普通";
            else if (value==6)return "微强";
            else if (value<=8)return "强";
            else if (value==9)return "超强";
            else if (value == 10) return "无敌";
            else return "未知";
        }
        return super.getValueAt(row, column);
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        if (column == 1) {
            try {
                aValue = Integer.parseInt(aValue.toString());
            } catch (NumberFormatException e) {
                aValue = 0;
            }
        }
        super.setValueAt(aValue, row, column);
    }
}

class AttrTable extends JTable{
    private DefaultTableModel model;
    public AttrTable(DefaultTableModel model){
        super(model);
        init();
        this.model = model;
        ensureRows(model);
    }
    private void init(){
        // this.setDefaultEditor(Object.class, new DefaultCellEditor(new JComboBox<>(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"})));

        JComboBox<String> comboBox = new JComboBox<>(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"});
        DefaultCellEditor dce = new DefaultCellEditor(comboBox) {
            @Override
            public boolean isCellEditable(EventObject event) {
                if (event instanceof MouseEvent) {
                    return ((MouseEvent)event).getClickCount() == 2;
                }
                return true;
            }
        };
        this.setDefaultEditor(Object.class, dce);

        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopup(e);
                }
            }
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopup(e);
                }
            }
            private void showPopup(MouseEvent e) {
                JPopupMenu popup = new JPopupMenu();
                JMenuItem addItem = new JMenuItem("添加新行");
                addItem.addActionListener(ae -> {
                    model.addRow(new Object[]{null, null});
                    ensureRows(model);
                });
                popup.add(addItem);
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        });


    }

    private static void ensureRows(DefaultTableModel model) {
        while (model.getRowCount() < 10) {
            model.addRow(new Object[]{null, null});
        }
    }
}