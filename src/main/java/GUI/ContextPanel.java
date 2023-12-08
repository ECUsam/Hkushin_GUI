package GUI;

import Constants.Constants;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class ContextPanel extends JPanel {
    private BasicPanel basicPanel;
    public ContextPanel(){
        super();
        initUI();
    }

    private void initUI(){
        basicPanel = new BasicPanel();
        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addComponent(basicPanel)
                        .addGap(100, 200, 800 )
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(basicPanel)

        );

    }


}
class BasicPanel extends JPanel{
    public BasicPanel(){
        super();
        init();
    }
    private void init(){
        JPanel title = new JPanel();
        title.setBackground(Color.LIGHT_GRAY);
        JLabel title_label = new JLabel("基本设定");
        title_label.setFont(new Font(Constants.FontName, Font.BOLD, 20));
        title.add(title_label);

        this.setPreferredSize(new Dimension(200, 400));
        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        this.add(title);
        //this.add(labelText("游戏名称"));
    }

    private JPanel labelText(String name){
        var label_text = new JPanel();
        var name_label = new JLabel(name);
        var text_area = new JTextField();
        label_text.add(name_label);
        label_text.add(text_area);
        return label_text;
    }
}