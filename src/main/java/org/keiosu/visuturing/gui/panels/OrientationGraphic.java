package org.keiosu.visuturing.gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JPanel;

class OrientationGraphic extends JPanel {
  private boolean isPortrait;

  public OrientationGraphic(boolean var1) {
    this.isPortrait = var1;
    this.setPreferredSize(new Dimension(35, 45));
    this.setOpaque(true);
  }

  public void paintComponent(Graphics var1) {
    super.paintComponent(var1);
    Graphics2D var2 = (Graphics2D)var1;
    Rectangle var3;
    if (this.isPortrait) {
      var3 = new Rectangle(5, 10, 25, 32);
      var2.setColor(Color.WHITE);
      var2.fill(var3);
      var2.setColor(Color.BLACK);
      var2.draw(var3);
    } else {
      var3 = new Rectangle(2, 15, 32, 25);
      var2.setColor(Color.WHITE);
      var2.fill(var3);
      var2.setColor(Color.BLACK);
      var2.draw(var3);
    }

  }

  public void setPortrait(boolean var1) {
    this.isPortrait = var1;
  }
}
