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
  private final PageFormat pageFormat;
  private final DiagramPrinter diagramPrinter;
  private boolean portraitOrientation;

  public PreviewGraphic(boolean portraitOrientation, DiagramPrinter printer) {
    this.diagramPrinter = printer;
    this.portraitOrientation = portraitOrientation;
    setPreferredSize(new Dimension(160, 160));
    setOpaque(true);
    PrinterJob job = PrinterJob.getPrinterJob();
    pageFormat = job.defaultPage();
  }

  @Override
  public void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);
    Graphics2D g = (Graphics2D) graphics;
    double xScale = 0.15119047610696146D;
    double yScale = 0.15441451547051574D;
    Rectangle printRect = new Rectangle(
            new Dimension(
              (int)(this.diagramPrinter.getPrintSize().getWidth() * xScale),
              (int)(this.diagramPrinter.getPrintSize().getHeight() * yScale)));
    Rectangle var8;
    Rectangle var9;
    if (this.portraitOrientation) {
      var9 = new Rectangle(50, 15, 90, 130);
      var8 = getRectangle(g, xScale, yScale, var9, 1, 45, 10);
      printRect.translate(45, 10);
    } else {
      var9 = new Rectangle(30, 35, 130, 90);
      var8 = getRectangle(g, xScale, yScale, var9, 2, 25, 30);
      printRect.translate(25, 30);
    }

    if (this.diagramPrinter.isFitToPage()) {
      double var13 = var8.getWidth() / printRect.getWidth();
      double var11 = var8.getHeight() / printRect.getHeight();
      printRect.setSize((int)(printRect.getWidth() * (var13 < var11 ? var13 : var11)), (int)(printRect.getHeight() * (var13 < var11 ? var13 : var11)));
    }

    printRect.translate((int)(this.pageFormat.getImageableX() * xScale), (int)(this.pageFormat.getImageableY() * yScale));
    var8.setSize((int)var8.getWidth() + 1, (int)var8.getHeight() + 1);
    g.setClip(var8);
    g.setColor(Color.red);
    g.draw(printRect);
    g.draw(new Double(printRect.getX(), printRect.getY(), printRect.getWidth() + printRect.getX(), printRect.getHeight() + printRect.getY()));
    g.draw(new Double(printRect.getWidth() + printRect.getX(), printRect.getY(), printRect.getX(), printRect.getHeight() + printRect.getY()));
  }

  private Rectangle getRectangle(Graphics2D g, double xScale, double yScale, Rectangle previewRect, int orientation, int locationX, int locationY) {
    Rectangle diagramPreview;
    g.setColor(Color.DARK_GRAY);
    g.fill(previewRect);
    previewRect.translate(-5, -5);
    g.setColor(Color.WHITE);
    g.fill(previewRect);
    g.setColor(Color.BLACK);
    g.draw(previewRect);
    pageFormat.setOrientation(orientation);
    diagramPreview = new Rectangle(
            (int)(this.pageFormat.getImageableX() * xScale),
            (int)(this.pageFormat.getImageableY() * yScale),
            (int)(this.pageFormat.getImageableWidth() * xScale),
            (int)(this.pageFormat.getImageableHeight() * yScale));
    diagramPreview.translate(locationX, locationY);
    g.setColor(new Color(240, 240, 240));
    g.draw(diagramPreview);
    return diagramPreview;
  }

  void setPortraitOrientation(boolean portraitOrientation) {
    this.portraitOrientation = portraitOrientation;
  }
}
