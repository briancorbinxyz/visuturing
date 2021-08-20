package org.keiosu.visuturing.gui.panels;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import org.keiosu.visuturing.core.TuringMachine;
import org.keiosu.visuturing.diagram.DiagramEditor;

public class GraphicsPanel extends DiagramEditor implements MouseListener {

  private final Dimension area = getExtents();

  public GraphicsPanel(TuringMachine turingMachine) {
    super(turingMachine);
    addMouseListener(this);
  }

  public void mouseReleased(MouseEvent event) {
    int x = (int) event.getPoint().getX();
    int y = (int) event.getPoint().getY();
    Rectangle hotspot = new Rectangle(event.getPoint(), new Dimension(40, 40));
    hotspot = this.transformation.createTransformedShape(new Rectangle(hotspot)).getBounds();
    this.scrollRectToVisible(hotspot);
    boolean needsRepaint = false;
    int hotspotMaxX = (int) ((double) x + hotspot.getWidth());
    if (hotspotMaxX > this.area.width) {
      this.area.width = hotspotMaxX;
      needsRepaint = true;
    }

    int hotspotMaxY = (int) ((double) y + hotspot.getWidth());
    if (hotspotMaxY > this.area.height) {
      this.area.height = hotspotMaxY;
      needsRepaint = true;
    }

    if (needsRepaint) {
      this.setPreferredSize(this.area);
      this.revalidate();
    }

    this.repaint();
  }

  public void zoomIn() {
    super.zoomIn();
    Rectangle hotspot =
        this.transformation.createTransformedShape(new Rectangle(this.getExtents())).getBounds();
    boolean needsRepaint = false;
    if (hotspot.width > this.area.width) {
      this.area.width = hotspot.width;
      needsRepaint = true;
    }

    if (hotspot.height > this.area.height) {
      this.area.height = hotspot.height;
      needsRepaint = true;
    }

    if (needsRepaint) {
      this.setPreferredSize(this.area);
      this.revalidate();
    }

    this.repaint();
  }

  public void zoomOut() {
    super.zoomOut();
    Rectangle hotspot =
        this.transformation.createTransformedShape(new Rectangle(this.getExtents())).getBounds();
    boolean needsRepaint = false;
    if (hotspot.width < this.area.width) {
      this.area.width = hotspot.width;
      needsRepaint = true;
    }

    if (hotspot.height < this.area.height) {
      this.area.height = hotspot.height;
      needsRepaint = true;
    }

    if (needsRepaint) {
      this.setPreferredSize(this.area);
      this.revalidate();
    }

    this.repaint();
  }

  @Override
  public void mouseClicked(MouseEvent ignore) {
    // do nothing
  }

  @Override
  public void mouseEntered(MouseEvent ignore) {
    // do nothing
  }

  @Override
  public void mouseExited(MouseEvent ignore) {
    // do nothing
  }

  @Override
  public void mousePressed(MouseEvent ignore) {
    // do nothing
  }
}
