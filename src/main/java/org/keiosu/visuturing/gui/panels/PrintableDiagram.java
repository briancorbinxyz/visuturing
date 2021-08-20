package org.keiosu.visuturing.gui.panels;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import org.keiosu.visuturing.diagram.DiagramEditor;

class PrintableDiagram implements Printable {
  private final DiagramEditor diagram;
  private Dimension printSize;
  private boolean fitToPage;

  public PrintableDiagram(DiagramEditor diagramEditor) {
    this.diagram = new DiagramEditor(diagramEditor.getCurrentMachine());
    this.printSize = diagramEditor.getExtents();
    this.fitToPage = false;
  }

  public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
    Graphics2D g = (Graphics2D) graphics;
    g.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
    if (fitToPage) {
      printToFitPage(pageFormat, g);
    } else {
      printDefault(g);
    }
    diagram.setSelectedState(null);
    diagram.setSelectedTransition(null);
    diagram.render(g);
    return 0;
  }

  private void printDefault(Graphics2D g) {
    g.scale(
        printSize.getWidth() / diagram.getExtents().getWidth(),
        printSize.getHeight() / diagram.getExtents().getHeight());
  }

  private void printToFitPage(PageFormat pageFormat, Graphics2D g) {
    double printWidth = pageFormat.getImageableWidth() / diagram.getExtents().getWidth();
    if (pageFormat.getImageableHeight() / diagram.getExtents().getHeight() < printWidth) {
      printWidth = pageFormat.getImageableHeight() / diagram.getExtents().getHeight();
    }
    g.scale(printWidth, printWidth);
  }

  public void setPrintSize(Dimension printSize) {
    this.printSize = printSize;
  }

  public void setFitToPage(boolean fitToPage) {
    this.fitToPage = fitToPage;
  }
}
