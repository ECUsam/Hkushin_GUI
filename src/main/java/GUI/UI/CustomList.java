package GUI.UI;

import javax.swing.*;

public class CustomList<E> extends JList<E> {
    public CustomList() {
        setOpaque(false);
        // 横向滚动时自适应宽度
        setVisibleRowCount(0);
    }

    @Override
    public void setModel(ListModel<E> model) {
        if (getModel() == model) return;
        super.setModel(model);
    }

    @Override
    public void setFixedCellWidth(int width) {
        if (getFixedCellWidth() == width) return;
        super.setFixedCellWidth(width);
    }
}