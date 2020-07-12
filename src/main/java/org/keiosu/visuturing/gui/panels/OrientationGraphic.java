package org.keiosu.visuturing.gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JPanel;

class OrientationGraphic extends JPanel {
  private boolean isPortrait;

  public OrientationGraphic(boolean portraitOrientation) {
    this.isPortrait = portraitOrientation;
    this.setPreferredSize(new Dimension(35, 45));
    this.setOpaque(true);
  }

  public void paintComponent(Graphics canvas) {
    super.paintComponent(canvas);
    Graphics2D c = (Graphics2D) canvas;
    Rectangle rect;
    if (isPortrait) {
      rect = new Rectangle(5, 10, 25, 32);
      c.setColor(Color.WHITE);
      c.fill(rect);
      c.setColor(Color.BLACK);
      c.draw(rect);
    } else {
      rect = new Rectangle(2, 15, 32, 25);
      c.setColor(Color.WHITE);
      c.fill(rect);
      c.setColor(Color.BLACK);
      c.draw(rect);
    }

  }

  void setPortrait(boolean portrait) {
    this.isPortrait = portrait;
  }
}
