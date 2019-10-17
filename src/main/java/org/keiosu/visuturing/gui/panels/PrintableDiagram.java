package org.keiosu.visuturing.gui.panels;

import org.keiosu.visuturing.core.State;
import org.keiosu.visuturing.core.Transition;
import org.keiosu.visuturing.diagram.DiagramEditor;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;

class PrintableDiagram implements Printable {
  private DiagramEditor diagram;
  private Dimension printSize;
  private boolean fitToPage;
  private DiagramPrinter dp;

  public PrintableDiagram(DiagramEditor var1) {
    this.diagram = new DiagramEditor(var1.getCurrentMachine());
    this.printSize = var1.getExtents();
    this.fitToPage = false;
  }

  public int print(Graphics var1, PageFormat var2, int var3) {
    Graphics2D var4 = (Graphics2D)var1;
    this.dp = this.dp;
    var4.translate(var2.getImageableX(), var2.getImageableY());
    double var5;
    double var7;
    double var9;
    if (this.fitToPage) {
      Dimension var11 = this.diagram.getExtents();
      var7 = var2.getImageableWidth();
      var9 = var2.getImageableHeight();
      var5 = var7 / var11.getWidth();
      if (var9 / var11.getHeight() < var5) {
        var5 = var9 / var11.getHeight();
      }

      var4.scale(var5, var5);
    } else {
      Dimension var13 = this.diagram.getExtents();
      var9 = this.printSize.getWidth();
      double var14 = this.printSize.getHeight();
      var5 = var9 / var13.getWidth();
      var7 = var14 / var13.getHeight();
      var4.scale(var5, var7);
    }

    this.diagram.setSelectedState((State)null);
    this.diagram.setSelectedTransition((Transition)null);
    this.diagram.render(var4);
    return 0;
  }

  public void setPrintSize(Dimension var1) {
    this.printSize = var1;
  }

  public void setFitToPage(boolean var1) {
    this.fitToPage = var1;
  }
}
