package GUI;

import javax.swing.border.Border;
import java.awt.*;

public class ShadowBorder implements Border {
    private int shadowSize;

    public ShadowBorder(int shadowSize) {
        this.shadowSize = shadowSize;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();

        // 启用抗锯齿
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 阴影颜色
        Color shadowColor = new Color(0, 0, 0, 128);
        g2.setColor(shadowColor);


        // 绘制组件边框
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(x, y, width - shadowSize - 1, height - shadowSize - 1, shadowSize, shadowSize);

        g2.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(shadowSize, shadowSize, shadowSize, shadowSize);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}
