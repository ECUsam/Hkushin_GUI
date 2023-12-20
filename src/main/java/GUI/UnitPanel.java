package GUI;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("unused")
public class UnitPanel extends JPanel {
    private JList jList = new JList<>();
    private JPanel cards;
    private CardLayout cardLayout;
    public UnitPanel(){
        super();
        initUI();
    }
    private void initUI(){
        cards = new JPanel();
        cardLayout = new CardLayout();
        cards.setLayout(cardLayout);

        jList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedItem = (String) jList.getSelectedValue();
                cardLayout.show(cards, selectedItem);
            }
        });

        this.add(jList);
    }

    public void addList(String unitName){
        jList.add(unitName, new JPanel());
    }
}

class Unit{

}