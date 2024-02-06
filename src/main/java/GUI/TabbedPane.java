package GUI;

import Constants.Constants;
import Constants.Constants_GUI;
import GUI.Panel.EventPanel;
import GUI.Panel.UnitPanel;

import javax.swing.*;
import java.awt.*;

public class TabbedPane extends JTabbedPane {
    public TabbedPane(){
        super(JTabbedPane.LEFT);
        addTabPane(Constants_GUI.get("context"), new ContextPanel());
        addTabPane(Constants_GUI.get("scenario"), new JPanel());
        addTabPane(Constants_GUI.get("story"), new JPanel());
        addTabPane(Constants_GUI.get("class"), new JPanel());
        addTabPane(Constants_GUI.get("unit"), new UnitPanel());
        addTabPane(Constants_GUI.get("event"), new EventPanel());
        addTabPane(Constants_GUI.get("power"), new JPanel());
    }

    private void addTabPane(String title, JPanel panel){

        JLabel label = new JLabel(title);
        label.setPreferredSize(new Dimension(Constants.TabWidth, Constants.TabHeight));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        label.setFont(new Font("Dialog", Font.BOLD, 14));

        this.addTab(null, panel);
        this.setTabComponentAt(this.getTabCount() - 1, label);
    }
}
