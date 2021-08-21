package org.keiosu.visuturing.mousetools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.QuadCurve2D.Double;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.keiosu.visuturing.core.Transition;
import org.keiosu.visuturing.diagram.DiagramEditor;

public class TuringMachineDiagramDeleteTool extends TuringMachineDiagramTool {
  private Transition currentTransition = null;

  public void preDraw(Graphics2D g) {}

  public void postDraw(Graphics2D canvas) {
    if (this.currentTransition != null) {
      canvas.setColor(Color.red);
      canvas.setStroke(new BasicStroke(3.0F));
      Shape transitionCurve =
          this.diagram.transitionCurve(
              this.currentTransition,
              this.machine.stateFor(this.currentTransition.getCurrentState()));
      if (transitionCurve instanceof QuadCurve2D) {
        QuadCurve2D var3 = (QuadCurve2D) transitionCurve;
        Double var4 = new Double();
        Double var5 = new Double();
        var3.subdivide(var4, var5);
        canvas.draw(
            new java.awt.geom.Line2D.Double(
                var4.getP2().getX() - 10.0D,
                var4.getP2().getY() - 10.0D,
                var4.getP2().getX() + 10.0D,
                var4.getP2().getY() + 10.0D));
        canvas.draw(
            new java.awt.geom.Line2D.Double(
                var4.getP2().getX() - 10.0D,
                var4.getP2().getY() + 10.0D,
                var4.getP2().getX() + 10.0D,
                var4.getP2().getY() - 10.0D));
      } else {
        CubicCurve2D var7 = (CubicCurve2D) transitionCurve;
        java.awt.geom.CubicCurve2D.Double var8 = new java.awt.geom.CubicCurve2D.Double();
        java.awt.geom.CubicCurve2D.Double var6 = new java.awt.geom.CubicCurve2D.Double();
        var7.subdivide(var8, var6);
        canvas.draw(
            new java.awt.geom.Line2D.Double(
                var8.getP2().getX() - 10.0D,
                var8.getP2().getY() - 10.0D,
                var8.getP2().getX() + 10.0D,
                var8.getP2().getY() + 10.0D));
        canvas.draw(
            new java.awt.geom.Line2D.Double(
                var8.getP2().getX() - 10.0D,
                var8.getP2().getY() + 10.0D,
                var8.getP2().getX() + 10.0D,
                var8.getP2().getY() - 10.0D));
      }
    }
  }

  public TuringMachineDiagramDeleteTool(DiagramEditor var1) {
    super(var1);

    try {
      Toolkit var2 = Toolkit.getDefaultToolkit();
      this.setCursor(
          var2.createCustomCursor(
              this.createImageIcon("cursors/delete.gif").getImage(), new Point(9, 9), "Remove"));
      this.setOverCursor(this.cursor);
    } catch (Exception var3) {
    }
  }

  public void mousePressed(MouseEvent var1) {
    if (SwingUtilities.isRightMouseButton(var1)) {
      this.diagramEditor.revertToSelect();
      this.diagramEditor.repaint();
    }

    if (this.currentTransition != null) {
      JOptionPane var2 = new JOptionPane();
      int var3 =
          JOptionPane.showConfirmDialog(
              (Component) null,
              "Are you sure you want to remove the transition: "
                  + this.currentTransition.toString()
                  + " ?",
              "VisuTuring - Transition Removal",
              1);
      if (var3 == 0) {
        this.diagramEditor.getCurrentMachine().removeTransition(this.currentTransition);
        this.diagramEditor.repaint();
      }
    }
  }

  public void mouseMoved(MouseEvent var1) {
    Point var2 = var1.getPoint();
    Point2D var3 =
        this.diagramEditor.toUser(new java.awt.geom.Point2D.Double(var2.getX(), var2.getY()));
    List var4 = this.diagramEditor.getCurrentMachine().getTransitions();

    for (int var5 = 0; var5 < var4.size(); ++var5) {
      Transition var6 = (Transition) var4.get(var5);
      if (this.isOver(var6, var3)) {
        this.currentTransition = var6;
        this.diagramEditor.repaint();
        return;
      }
    }

    if (this.currentTransition != null) {
      this.currentTransition = null;
      this.diagramEditor.repaint();
    }
  }
}
