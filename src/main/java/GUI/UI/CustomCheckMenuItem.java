package GUI.UI;

import Constants.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomCheckMenuItem extends JCheckBoxMenuItem {
    private boolean drawBg;
    private Timer drawBgTimer;
    private float alpha;
    private final float destAlpha = 0.1f;

    public CustomCheckMenuItem(String text) {
        this(text, false);
    }

    public CustomCheckMenuItem(String text, boolean selected) {
        super(text, selected);
        init();
    }

    private void init() {
        setOpaque(false);
        setFont(Constants.font);
        initResponse();
    }

    private void initResponse() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setDrawBg(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setDrawBg(false);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setDrawBg(false);
            }
        });

        addMouseWheelListener(e -> setDrawBg(false));

        drawBgTimer = new Timer(2, e -> {
            if (drawBg) alpha = Math.min(destAlpha, alpha + 0.005f);
            else alpha = Math.max(0, alpha - 0.005f);
            if (alpha <= 0 || alpha >= destAlpha) drawBgTimer.stop();
            repaint();
        });
    }

    private void setDrawBg(boolean drawBg) {
        if (this.drawBg == drawBg) return;
        this.drawBg = drawBg;
        if (drawBgTimer.isRunning()) return;
        drawBgTimer.start();
    }

    private void createBorder() {
        setBorder(BorderFactory.createEmptyBorder(4, 5, 4, 0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        // 画背景
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getForeground());
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        super.paintComponent(g);
    }
}
