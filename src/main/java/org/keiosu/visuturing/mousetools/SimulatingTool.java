package org.keiosu.visuturing.mousetools;

import org.keiosu.visuturing.core.Configuration;
import org.keiosu.visuturing.core.State;
import org.keiosu.visuturing.core.Transition;
import org.keiosu.visuturing.core.TuringMachine;
import org.keiosu.visuturing.diagram.DiagramEditor;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Ellipse2D.Double;

public class SimulatingTool extends AbstractMouseTool {
  private TuringMachine machine;
  private State currentState;
  private Transition currentTransition;
  private int frame;
  public static final double MAX_FRAMES = 10.0D;

  public SimulatingTool(DiagramEditor var1) {
    super(var1);
    this.machine = var1.getCurrentMachine();
    this.currentState = null;
    this.frame = 1;
  }

  public void preDraw(Graphics2D var1) {
    if (this.currentTransition != null) {
      var1.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      var1.setColor(Color.RED);
      double var6 = (double)this.frame / 10.0D;
      double var2;
      double var4;
      if (this.currentTransition.getCurrentState().equals(this.currentTransition.getNextState())) {
        CubicCurve2D var8 = (CubicCurve2D)this.diagram.getGraphicEdge(this.currentTransition);
        var2 = (1.0D - var6) * (1.0D - var6) * (1.0D - var6) * var8.getX1() + 3.0D * (1.0D - var6) * (1.0D - var6) * var6 * var8.getCtrlX1() + 3.0D * (1.0D - var6) * var6 * var6 * var8.getCtrlX2() + var6 * var6 * var6 * var8.getX2();
        var4 = (1.0D - var6) * (1.0D - var6) * (1.0D - var6) * var8.getY1() + 3.0D * (1.0D - var6) * (1.0D - var6) * var6 * var8.getCtrlY1() + 3.0D * (1.0D - var6) * var6 * var6 * var8.getCtrlY2() + var6 * var6 * var6 * var8.getY2();
      } else {
        QuadCurve2D var9 = (QuadCurve2D)this.diagram.getGraphicEdge(this.currentTransition);
        var2 = (1.0D - var6) * (1.0D - var6) * var9.getX1() + 2.0D * (1.0D - var6) * var6 * var9.getCtrlX() + var6 * var6 * var9.getX2();
        var4 = (1.0D - var6) * (1.0D - var6) * var9.getY1() + 2.0D * (1.0D - var6) * var6 * var9.getCtrlY() + var6 * var6 * var9.getY2();
      }

      Double var10 = new Double(var2 - 5.0D, var4 - 5.0D, 10.0D, 10.0D);
      var1.fill(var10);
    }

  }

  public void increaseFrame() {
    ++this.frame;
  }

  public int getFrame() {
    return this.frame;
  }

  public void postDraw(Graphics2D var1) {
    var1.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    var1.setColor(new Color(255, 40, 40, 200));
    var1.setStroke(new BasicStroke(3.0F));
    if (this.currentState != null) {
      Point2D var2 = this.currentState.getLocation();
      Double var3 = new Double(var2.getX() - 20.0D, var2.getY() - 20.0D, 40.0D, 40.0D);
      var1.fill(var3);
      var1.setColor(new Color(255, 40, 40));
      var3 = new Double(var2.getX() - 30.0D, var2.getY() - 30.0D, 60.0D, 60.0D);
      var1.draw(var3);
    }

  }

  public void setConfig(Configuration var1) {
    if (var1 != null) {
      State var2 = this.machine.stateFor(var1.getState());
      this.currentState = var2;
    } else {
      this.currentState = null;
    }

  }

  public void setTransition(Transition var1) {
    this.currentTransition = var1;
  }

  public void setFrame(int var1) {
    this.frame = var1;
  }
}
