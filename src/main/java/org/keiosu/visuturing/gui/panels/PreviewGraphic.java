package org.keiosu.visuturing.gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D.Double;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import javax.swing.JPanel;

class PreviewGraphic extends JPanel {
  private boolean isPortrait;
  private PageFormat pf;
  private DiagramPrinter dp;

  public PreviewGraphic(boolean portraitOrientation, DiagramPrinter printer) {
    isPortrait = portraitOrientation;
    setPreferredSize(new Dimension(160, 160));
    setOpaque(true);
    PrinterJob job = PrinterJob.getPrinterJob();
    dp = printer;
    pf = job.defaultPage();
  }

  public void paintComponent(Graphics var1) {
    super.paintComponent(var1);
    Graphics2D var2 = (Graphics2D)var1;
    double var3 = 0.15119047610696146D;
    double var5 = 0.15441451547051574D;
    Rectangle var7 = new Rectangle(new Dimension((int)(this.dp.getPrintSize().getWidth() * var3), (int)(this.dp.getPrintSize().getHeight() * var5)));
    Rectangle var8;
    Rectangle var9;
    if (this.isPortrait) {
      var9 = new Rectangle(50, 15, 90, 130);
      var8 = getRectangle(var2, var3, var5, var9, 1, 45, 10);
      var7.translate(45, 10);
    } else {
      var9 = new Rectangle(30, 35, 130, 90);
      var8 = getRectangle(var2, var3, var5, var9, 2, 25, 30);
      var7.translate(25, 30);
    }

    if (this.dp.isFitToPage()) {
      double var13 = var8.getWidth() / var7.getWidth();
      double var11 = var8.getHeight() / var7.getHeight();
      var7.setSize((int)(var7.getWidth() * (var13 < var11 ? var13 : var11)), (int)(var7.getHeight() * (var13 < var11 ? var13 : var11)));
    }

    var7.translate((int)(this.pf.getImageableX() * var3), (int)(this.pf.getImageableY() * var5));
    var8.setSize((int)var8.getWidth() + 1, (int)var8.getHeight() + 1);
    var2.setClip(var8);
    var2.setColor(Color.red);
    var2.draw(var7);
    var2.draw(new Double(var7.getX(), var7.getY(), var7.getWidth() + var7.getX(), var7.getHeight() + var7.getY()));
    var2.draw(new Double(var7.getWidth() + var7.getX(), var7.getY(), var7.getX(), var7.getHeight() + var7.getY()));
  }

  private Rectangle getRectangle(Graphics2D g, double var3, double var5, Rectangle var9, int i, int i2, int i3) {
    Rectangle var8;
    g.setColor(Color.DARK_GRAY);
    g.fill(var9);
    var9.translate(-5, -5);
    g.setColor(Color.WHITE);
    g.fill(var9);
    g.setColor(Color.BLACK);
    g.draw(var9);
    pf.setOrientation(i);
    var8 = new Rectangle((int)(this.pf.getImageableX() * var3), (int)(this.pf.getImageableY() * var5), (int)(this.pf.getImageableWidth() * var3), (int)(this.pf.getImageableHeight() * var5));
    var8.translate(i2, i3);
    g.setColor(new Color(240, 240, 240));
    g.draw(var8);
    return var8;
  }

  void setPortrait(boolean portrait) {
    isPortrait = portrait;
  }
}
