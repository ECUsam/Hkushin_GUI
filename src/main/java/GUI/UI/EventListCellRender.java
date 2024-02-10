package GUI.UI;

import GUI.Panel.EventCellData;
import postfix.DataManger;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class EventListCellRender extends DefaultListCellRenderer {
    private int cellWidth;
    private int cellHeight;

    public EventListCellRender(){
        cellHeight = 25;
    }
    public EventListCellRender(int cellWidth, int cellHeight){
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
    }

    public String marginText(String value, int space){
        String style2 = "px;'>";
        String style = "<div style='margin-left: ";
        return style + space*8 + style2 +value+"</div>";
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        //label.setPreferredSize(new Dimension(cellWidth, cellHeight));
        String res = value.toString().replace("$", "<br>");
        if(value instanceof EventCellData e){
            res = marginText(res, e.level);
            if(Objects.equals(e.func, "msg") && e.args.length==2){
                // 没搜到 溯源，请
                String name = DataManger.searchUnitNameFormFuncName(e.args[0]);
                if(name!=null){
                    label.setText("<html>" + res.replace(e.args[0], name) + "</html>");
                    return label;
                }
            }
        }
        label.setText("<html>" + res + "</html>");
        return label;
    }
}

// 图片显示
//            if(Objects.equals(e.func, "msg") && e.args.length>=2){
//                PathManager p = PathManager.getInstance();
//                File face = p.getFaceFile(e.args[0]);
//                if(e.args.length==3){
//                    face = p.getFaceFile(e.args[1]);
//                }
//                String imagePath = "file:///" + face.getAbsolutePath().replace("\\", "/")+".png"; // 图片路径
//                String imageTag = "<img src=\"" + imagePath + "\">"; // 图片标签
//                String textWithImage = value.toString().replace("$", "<br>").replace(e.args[0], imageTag);
//                label.setText("<html>" + textWithImage + "</html>");
//            }
//            else {
//                label.setText("<html>" + value.toString().replace("$", "<br>") + "</html>");
//            }