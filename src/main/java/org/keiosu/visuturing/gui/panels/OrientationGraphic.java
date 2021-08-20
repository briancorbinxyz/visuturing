package org.keiosu.visuturing.gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JPanel;

class OrientationGraphic extends JPanel {
    private boolean portraitOrientation;

    public OrientationGraphic(boolean portraitOrientation) {
        this.portraitOrientation = portraitOrientation;
        this.setPreferredSize(new Dimension(35, 45));
        this.setOpaque(true);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        Rectangle rect;
        if (portraitOrientation) {
            rect = new Rectangle(5, 10, 25, 32);
        } else {
            rect = new Rectangle(2, 15, 32, 25);
        }
        g.setColor(Color.WHITE);
        g.fill(rect);
        g.setColor(Color.BLACK);
        g.draw(rect);
    }

    void setPortraitOrientation(boolean portraitOrientation) {
        this.portraitOrientation = portraitOrientation;
    }
}
