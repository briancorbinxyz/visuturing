package VisuTuring.gui.panels;

import VisuTuring.core.TuringMachine;
import VisuTuring.diagram.DiagramEditor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GraphicsPanel extends DiagramEditor implements MouseListener {
  private Dimension area = this.getExtents();

  public GraphicsPanel(TuringMachine var1) {
    super(var1);
    this.addMouseListener(this);
  }

  public void mouseReleased(MouseEvent var1) {
    int var2 = (int)var1.getPoint().getX();
    int var3 = (int)var1.getPoint().getY();
    Rectangle var4 = new Rectangle(var1.getPoint(), new Dimension(40, 40));
    var4 = this.transformation.createTransformedShape(new Rectangle(var4)).getBounds();
    this.scrollRectToVisible(var4);
    boolean var5 = false;
    int var6 = (int)((double)var2 + var4.getWidth());
    if (var6 > this.area.width) {
      this.area.width = var6;
      var5 = true;
    }

    int var7 = (int)((double)var3 + var4.getWidth());
    if (var7 > this.area.height) {
      this.area.height = var7;
      var5 = true;
    }

    if (var5) {
      this.setPreferredSize(this.area);
      this.revalidate();
    }

    this.repaint();
  }

  public void zoomIn() {
    super.zoomIn();
    Dimension var1 = this.getExtents();
    Rectangle var2 = this.transformation.createTransformedShape(new Rectangle(var1)).getBounds();
    boolean var3 = false;
    if (var2.width > this.area.width) {
      this.area.width = var2.width;
      var3 = true;
    }

    if (var2.height > this.area.height) {
      this.area.height = var2.height;
      var3 = true;
    }

    if (var3) {
      this.setPreferredSize(this.area);
      this.revalidate();
    }

    this.repaint();
  }

  public void zoomOut() {
    super.zoomOut();
    Dimension var1 = this.getExtents();
    Rectangle var2 = this.transformation.createTransformedShape(new Rectangle(var1)).getBounds();
    boolean var3 = false;
    if (var2.width < this.area.width) {
      this.area.width = var2.width;
      var3 = true;
    }

    if (var2.height < this.area.height) {
      this.area.height = var2.height;
      var3 = true;
    }

    if (var3) {
      this.setPreferredSize(this.area);
      this.revalidate();
    }

    this.repaint();
  }

  public void mouseClicked(MouseEvent var1) {
  }

  public void mouseEntered(MouseEvent var1) {
  }

  public void mouseExited(MouseEvent var1) {
  }

  public void mousePressed(MouseEvent var1) {
  }
}
