package GUI;

import Constants.Constants;
import FileManager.PathManager;
import Constants.Constants_GUI;
import Token.TokenFeature;
import postfix.DataManger;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.text.NumberFormatter;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;


class shared{
    static HashMap<String, JComponent> componentHashMap;
}
@SuppressWarnings("unused")
public class UnitPanel extends JPanel implements INTERFACE{
    private JList<Object> jList = new JList<>();
    private JTree tree;
    private DefaultMutableTreeNode root;
    private DefaultTreeModel treeModel;
    private JPanel cards;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private basicSetting basicSetting;
    private JSplitPane splitPane;


    // 包私有
    public UnitPanel(){
        super();
        shared.componentHashMap = new HashMap<>();
        initUI();
    }
    private void initUI(){
        INFORMATION information = INFORMATION.getInstance();
        information.addObserver(this::update);
        treeTableInit();
        this.setLayout(new BorderLayout());
        contentPanel = new JPanel();
        getContentPanelSetting();


        // 使用JSplitPane分隔左右两边的组件
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createTreePanel(), contentPanel);
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
                        basicSetting.clearData(basicSetting);
                        basicSetting.update(treeNode);
                    }
                }
            }
        });
    }

    public void treeTableInit(){
        root = new DefaultMutableTreeNode("I am root");
        treeModel = new DefaultTreeModel(root);
        tree = new JTree(treeModel);
        tree.setRowHeight(25); // 设置行高
        Font font = new Font("Microsoft YaHei", Font.PLAIN, 12);
        tree.setFont(font);
        tree.setRootVisible(false);
        tree.setCellRenderer(new CustomTreeCellRenderer());
    }

    private void getContentPanelSetting(){
        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        contentPanel.setLayout(new BorderLayout());
        basicSetting = new basicSetting();
        var downPanel = new JPanel();

        JScrollPane basicSettingScrollPane = new JScrollPane(basicSetting);

        basicSettingScrollPane.getVerticalScrollBar().setUnitIncrement(20);

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
            basicSetting.getAttrSet();
        }else if(informationType == INFORMATION_TYPE.CLEAR_DATA){
            Enumeration<TreeNode> children = root.children();
            List<TreeNode> nodesToRemove = new ArrayList<>();
            while (children.hasMoreElements()) {
                nodesToRemove.add(children.nextElement());
            }
            for (TreeNode node : nodesToRemove) {
                root.remove((MutableTreeNode) node);
            }
            treeModel.reload();
            basicSetting.clearData(basicSetting);
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
    private skillPanel skillArea;
    private AttrTable table;
    private JButton fatherButton;
    private Boolean checkFather;
    private OPcode.TreeNode currentNode;

    public basicSetting() {
        NumberFormat integerFormat = NumberFormat.getIntegerInstance();
        numberFormatter = new NumberFormatter(integerFormat);
        numberFormatter.setValueClass(Integer.class);
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setMinimum(0);
        numberFormatter.setMaximum(Integer.MAX_VALUE);

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

        gbc.gridwidth = 1;
        fatherButton = new JButton("本类");
        checkFather = false;
        fatherButton.addActionListener(e -> {
            if(fatherButton.getText().equals("本类")){
                fatherButton.setText("溯源");
                checkFather = true;
            }else if(fatherButton.getText().equals("溯源")){
                fatherButton.setText("本类");
                checkFather = false;
            }
            clearData(this);
            update(currentNode);
        });

        adder(fatherButton, 1, 0);
        gbc.gridy++;

        LabelAdder_string("name");
        LabelAdder_string("help");
        LabelAdder("hp");
        LabelAdder("mp");
        LabelAdder("attack");
        LabelAdder("defense");
        LabelAdder("magic");
        LabelAdder("magdef");
        LabelAdder("speed");
        LabelAdder("dext");
        LabelAdder("heal_max");
        LabelAdder("move");
        LabelAdder("summon_max");
        LabelAdder_string("power_name");

        skillArea = new skillPanel();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 3;
        gbc.gridwidth = 10;
        adder(skillArea, 2, 0);
        gbc.gridheight = 1;
        gbc.gridwidth = 1;

        comboBoxAdder("race", null, 2, 3);
        var sexC = comboBoxAdder("sex", null, 2, 4);
        sexC.addItem(Constants_GUI.get("female"));
        sexC.addItem(Constants_GUI.get("male"));
        sexC.addItem(Constants_GUI.get("neuter"));
        sexC.setSelectedIndex(-1);

        DefaultTableModel tableModel = new AttrModel();
        table = new AttrTable(tableModel);

        MouseAndTable(table);
        // test
        // table.addNewData2Row(new Object[]{"类型1", 1});

        JScrollPane AttrPane = new JScrollPane(table);
        AttrPane.addMouseWheelListener(this::dispatchEvent);
        AttrPane.setPreferredSize(new Dimension(250, 350));

        JLabel attr = new JLabel(Constants_GUI.get("attribute"));
        adder(attr, 2, 6);
        gbc.gridheight = 15;
        gbc.gridwidth = 2;
        adder(AttrPane, 2, 7);

        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridy = 99;
        gbc.gridx = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.BOTH;
        this.add(Box.createGlue(), gbc);
        // setPanelComponentsDisabled(this);
    }

    public static void setPanelComponentsDisabled(JComponent panel) {
        Component[] components = panel.getComponents();

        for (Component component : components) {
            if (component instanceof JTextField) {
                ((JTextField) component).setEditable(false);
            } else if (component instanceof JButton) {
                component.setEnabled(false);
            } else if (component instanceof JComboBox) {
                component.setEnabled(false);
            } else if (component instanceof JCheckBox) {
                component.setEnabled(false);
            } else if (component instanceof JRadioButton) {
                component.setEnabled(false);
            } else if (component instanceof JSpinner) {
                component.setEnabled(false);
            } else if (component instanceof JTextArea) {
                ((JTextArea) component).setEditable(false);
            } else if (component instanceof JTable) {
                // setTableNonEditable((JTable) component);
            } else if (component instanceof Container) {
                setPanelComponentsDisabled((JComponent) component);
            }
            // 这里可以添加更多的组件类型
        }
    }

    private static void setTableNonEditable(JTable table) {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // 使所有单元格不可编辑
            }
        };
        // 将现有数据复制到新模型
        DefaultTableModel currentModel = (DefaultTableModel) table.getModel();
        for (int i = 0; i < currentModel.getRowCount(); i++) {
            model.addRow(currentModel.getDataVector().elementAt(i));
        }
        table.setModel(model);
    }


    private JComboBox<String> comboBoxAdder(String meta_label, String[] strings, int x, int y){
        String name = Constants_GUI.get(meta_label);
        JLabel label = new JLabel(name);
        var comboBox = comboBoxGetter(strings);
        shared.componentHashMap.put(meta_label, comboBox);
        comboBox.setSelectedIndex(-1);
        gbc.gridwidth = 1;
        adder(label, x, y);
        gbc.gridwidth = 2;
        adder(comboBox, x+1, y);
        return comboBox;
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
        if(strings == null)return comboBox;
        for (String s:strings){
            comboBox.addItem(s);
        }
        return comboBox;
    }

    private void LabelAdder(String meta_name){
        String name = Constants_GUI.get(meta_name);
        gbc.gridwidth = 1;
        JLabel label = new JLabel(name);
        var label_ = new JFormattedTextField(numberFormatter);
        label_.setColumns(10);
        label.setLabelFor(label_);
        shared.componentHashMap.put(meta_name, label_);
        gbc.gridy++;
        gbc.gridx = 0;
        this.add(label, gbc);
        gbc.gridx = 1;
        this.add(label_, gbc);
    }

    private void LabelAdder_string(String meta_name){
        String name = Constants_GUI.get(meta_name);
        gbc.gridwidth = 1;
        JLabel label = new JLabel(name);
        var label_ = new JFormattedTextField();
        label_.setColumns(10);
        shared.componentHashMap.put(meta_name, label_);
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
        // System.out.print(file.getPath());
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

    public void clearData(Container container) {
        var a = new ImageIcon(createPatternPlaceholderImage());
        headImage.setIcon(a);
        skillArea.clearData();
        table.cleaData();
        for (Component comp : container.getComponents()) {
            if (comp instanceof JTextField) {
                ((JTextField) comp).setText(""); // 重置文本字段
            } else if (comp instanceof JComboBox) {
                ((JComboBox<?>) comp).setSelectedIndex(-1); // 重置下拉菜单
                ((JComboBox<?>) comp).removeAllItems();
            } else if (comp instanceof Container) {
                clearData((Container) comp); // 递归重置子容器
            }
        }
    }

    public void getAttrSet(){
        var AttrMap = DataManger.getAttrMap();
        List<String> AttrList = Utils.getSortedList(AttrMap);
        for(String s : AttrList){
            table.addItem2first(s);
        }
    }

    public void update(OPcode.TreeNode treeNode){
        if(treeNode==null)return;
        // clearData(this);

        for(OPcode.TreeNode node : treeNode.getChildren()){
            if(Objects.equals(node.key, "className")){
                JTextField a = (JTextField) shared.componentHashMap.get(node.key);
                if(a!=null) a.setText((String) node.value);
            }
            if(Objects.equals(node.key, "Feature")){
                if(node.value instanceof TokenFeature value){
                    if(Objects.equals(value.FeatureName, "face")){
                        PathManager pathManager = PathManager.getInstance();
                        String faceName = value.strings_feature[0];
                        Path face = pathManager.getDataNamePath("face").resolve(faceName+".png");
                        File facefile = new File(face.toUri());
                        if(facefile.exists())
                            displayImage(facefile);
                    }
                    if(Objects.equals(value.FeatureName, "consti")){
                        for(String con : value.strings_feature){
                            String[] cons = Utils.getOutOfSpace(con).split("\\*");
                            var map = DataManger.getAttrMap();
                            table.addNewData2Row(new Object[]{map.get(cons[0]).getKey(), Integer.parseInt( cons[1] ) });
                        }
                    }
                    var a = shared.componentHashMap.get(value.FeatureName);
                    if(a instanceof JTextField textField){
                        if (value.strings_feature.length == 1) {
                            textField.setText(value.strings_feature[0]);
                        }
                        // 不知道怎么写了
                    }
                    if(a instanceof JComboBox comboBox){
                        comboBox.setSelectedIndex(-1);
                        if(Objects.equals(value.FeatureName, "race")) {
                            var raceMap = DataManger.getRaceMap();
                            for (Map.Entry<String, OPcode.TreeNode> raceSet : raceMap.entrySet()) {
                                String name = raceSet.getValue().getName();
                                if(name==null){
                                    comboBox.addItem(raceSet.getKey());
                                } else {
                                    comboBox.addItem(raceSet.getValue().getName());
                                }
                            }
                            var name = raceMap.get(value.strings_feature[0]).getName();
                            if(name!=null) comboBox.setSelectedItem(name);
                            else comboBox.setSelectedItem(value.strings_feature[0]);
                        }
                        if(Objects.equals(value.FeatureName, "sex")){
                            comboBox.addItem(Constants_GUI.get("female"));
                            comboBox.addItem(Constants_GUI.get("male"));
                            comboBox.addItem(Constants_GUI.get("neuter"));
                            comboBox.setSelectedItem(Constants_GUI.get(value.strings_feature[0]));
                        }
                    }
                }
            }
        }
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

    public void clearData(){}

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
    private DialogCellEditor first_dce;
    private DefaultCellEditor dce;

    public AttrTable(DefaultTableModel model){
        super(model);
        init();
        this.model = model;
        ensureRows(model);
    }

    public void addNewData2Row(Object[] objects){
        for(int i = 0; i < rowHeight-1; i++){
            var value = model.getValueAt(i, 1);
            if(value == null){
                if (i < model.getRowCount()) {
                    model.removeRow(i);
                    model.insertRow(i, objects);
                    break;
                }
            }
        }
    }

    private void init(){
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"});
        dce = new DefaultCellEditor(comboBox) {
            @Override
            public boolean isCellEditable(EventObject event) {
                if (event instanceof MouseEvent) {
                    return ((MouseEvent)event).getClickCount() == 2;
                }
                return true;
            }
        };

        first_dce = new DialogCellEditor();

        this.getColumnModel().getColumn(0).setCellEditor(first_dce);
        this.getColumnModel().getColumn(1).setCellEditor(dce);
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

    public void addItem2first(String item){
        first_dce.addListItem(item);
    }

    public void cleaData(){
        for(int i=0;i<model.getRowCount()-1;i++){
            model.removeRow(i);
        }
        ensureRows(model);
    }
    @Override
    public TableCellEditor getCellEditor(int row, int column) {
        // 第二列的编辑逻辑依赖于第一列的值
        if (column == 1) {
            Object value = getValueAt(row, 0);
            if (value == null || value.toString().trim().isEmpty()) {
                // 如果第一列为空，则第二列不可编辑
                return new DefaultCellEditor(new JTextField()) {
                    @Override
                    public boolean isCellEditable(EventObject anEvent) {
                        return false; // 使单元格不可编辑
                    }
                };
            }
        }
        return super.getCellEditor(row, column);
    }

    private static void ensureRows(DefaultTableModel model) {
        while (model.getRowCount() < 20) {
            model.addRow(new Object[]{null, null});
        }
    }
}


class DialogCellEditor extends AbstractCellEditor implements TableCellEditor {
    private JDialog dialog;
    private JList<String> list;
    private DefaultListModel<String> listModel;
    private String currentValue;
    private JButton editorComponent;

    public DialogCellEditor() {
        // 初始化列表和对话框
        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        dialog = new JDialog();
        dialog.setContentPane(new JScrollPane(list));
        dialog.pack();
        dialog.setModal(true);
        dialog.setLocationRelativeTo(null);

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    currentValue = list.getSelectedValue();
                    dialog.setVisible(false);
                    stopCellEditing();
                }
            }
        });

        editorComponent = new JButton();
        editorComponent.addActionListener(e -> dialog.setVisible(true));
        editorComponent.setContentAreaFilled(false);
        editorComponent.setBorderPainted(false);
        editorComponent.setFocusPainted(false);
    }
    public void removeAll(){
        list.removeAll();
    }

    @Override
    public Object getCellEditorValue() {
        return currentValue;
    }

    public void addListItem(String s){
        listModel.addElement(s);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        currentValue = (String) value;
        return editorComponent;
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        // 仅双击
        return e instanceof MouseEvent && ((MouseEvent) e).getClickCount() == 2;
    }
}