package GUI;

import javax.swing.*;

@SuppressWarnings("unused")
public class UnitPanel extends JPanel {
    private JList jList = new JList<Object>();
    public UnitPanel(){
        super();
        initUI();
    }
    private void initUI(){
        jList.add("aa", jList);
    }
}

class Unit{

}