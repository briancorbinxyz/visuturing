package org.keiosu.visuturing.mousetools;

import org.keiosu.visuturing.core.State;
import org.keiosu.visuturing.core.Symbols;
import org.keiosu.visuturing.core.Transition;
import org.keiosu.visuturing.diagram.DiagramEditor;
import org.keiosu.visuturing.gui.TransitionFrame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class TransitionTool extends MouseTool {
  private Transition newTrans;
  private int mouseClicks = 0;
  private TransitionFrame transitionEditor;
  private Point currentPoint;

  public TransitionTool(DiagramEditor var1) {
    super(var1);

    try {
      Toolkit var2 = Toolkit.getDefaultToolkit();
      this.setCursor(var2.createCustomCursor(this.createImageIcon("cursors/addtrans.gif").getImage(), new Point(0, 0), "AddTransition"));
      this.setOverCursor(this.cursor);
    } catch (Exception var3) {
      System.out.println("messed up.");
      var3.printStackTrace();
    }

  }

  public void mousePressed(MouseEvent var1) {
    if (SwingUtilities.isRightMouseButton(var1)) {
      this.diagram.revertToSelect();
      this.diagram.repaint();
    }

    this.currentPoint = var1.getPoint();
    Point2D var2 = this.diagram.toUser(new Double(this.currentPoint.getX(), this.currentPoint.getY()));
    Vector var3 = this.diagram.getCurrentMachine().getStates();

    for(int var4 = 0; var4 < var3.size(); ++var4) {
      State var5 = (State)var3.get(var4);
      if (var5.contains(var2)) {
        if (var5.getName().equals("h") && this.mouseClicks == 0) {
          JOptionPane.showMessageDialog((Component)null, "You cannot create transitions out of the halting state.", "TBIT VisuTuring", 0, (Icon)null);
          return;
        }

        this.diagram.setSelectedState(var5);
        this.diagram.setSelectedTransition((Transition)null);
        break;
      }
    }

  }

  public void mouseReleased(MouseEvent var1) {
    this.currentPoint = var1.getPoint();
    Point2D var2 = this.diagram.toUser(new Double(this.currentPoint.getX(), this.currentPoint.getY()));
    State var3 = this.diagram.getSelectedState();
    Transition var4 = this.diagram.getSelectedTransition();
    double var5;
    double var7;
    if (var3 != null && this.mouseClicks == 0) {
      var5 = Math.toRadians(45.0D);
      var7 = var3.getLocation().getX();
      double var12 = var3.getLocation().getY();
      this.newTrans = new Transition(var3.getName(), '0', var3.getName(), '0');
      this.newTrans.setP1(new Double(var7, var12));
      ++this.mouseClicks;
      this.diagram.setSelectedTransition((Transition)null);
    } else if (var3 != null && this.mouseClicks == 1) {
      var5 = var3.getLocation().getX();
      var7 = var3.getLocation().getY();
      if (this.newTrans != null) {
        this.newTrans.setP2(new Double(var5, var7));
        Point2D var9 = this.newTrans.getP1();
        Point2D var10 = this.newTrans.getP2();
        this.newTrans.setNextState(var3.getName());
        this.newTrans.setTask(Symbols.SPACE);
        this.newTrans.setCurrentSymbol(Symbols.SPACE);
        this.transitionEditor = new TransitionFrame(this.diagram.getCurrentMachine(), this.newTrans);
        this.newTrans.setControlPoint(new Double(var9.getX() + (var10.getX() - var9.getX()) / 2.0D, var9.getY() + (var10.getY() - var9.getY()) / 2.0D));
        if (this.newTrans.getNextState().equals(this.newTrans.getCurrentState())) {
          this.newTrans.setControlPoint(new Double(var9.getX(), var9.getY() - 60.0D));
        }

        Point var11 = new Point(this.currentPoint);
        SwingUtilities.convertPointToScreen(var11, (Component)var1.getSource());
        this.transitionEditor.setLocation(var11);
        this.transitionEditor.setVisible(true);
        this.newTrans = this.transitionEditor.getTransition();
        this.diagram.getCurrentMachine().addTransition(this.newTrans);
        this.newTrans = null;
        this.diagram.repaint();
      }

      this.mouseClicks = 0;
      this.diagram.setSelectedTransition((Transition)null);
    }

  }

  public void mouseDragged(MouseEvent var1) {
    this.currentPoint = var1.getPoint();
    Point2D var2 = this.diagram.toUser(new Double(this.currentPoint.getX(), this.currentPoint.getY()));
    Vector var3 = this.diagram.getCurrentMachine().getStates();

    for(int var4 = 0; var4 < var3.size(); ++var4) {
      State var5 = (State)var3.get(var4);
      if (var5.contains(var2)) {
        var2 = this.diagram.toWorld(var5.getLocation());
        this.currentPoint = new Point((int)var2.getX(), (int)var2.getY());
        break;
      }
    }

    this.diagram.repaint();
  }

  public void mouseMoved(MouseEvent var1) {
    this.currentPoint = var1.getPoint();
    Point2D var2 = this.diagram.toUser(new Double(this.currentPoint.getX(), this.currentPoint.getY()));
    Vector var3 = this.diagram.getCurrentMachine().getStates();

    for(int var4 = 0; var4 < var3.size(); ++var4) {
      State var5 = (State)var3.get(var4);
      if (var5.contains(var2)) {
        var2 = this.diagram.toWorld(var5.getLocation());
        this.currentPoint = new Point((int)var2.getX(), (int)var2.getY());
        break;
      }
    }

    this.diagram.repaint();
  }

  public void postDraw(Graphics2D var1) {
  }

  public void preDraw(Graphics2D var1) {
    Object var2 = new Double();
    if (this.currentPoint != null) {
      var2 = this.diagram.toUser(new Double(this.currentPoint.getX(), this.currentPoint.getY()));
    }

    if (this.newTrans != null) {
      var1.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      var1.setColor(Color.BLACK);
      Point2D var3 = this.newTrans.getP1();
      java.awt.geom.Line2D.Double var4 = new java.awt.geom.Line2D.Double(var3, (Point2D)var2);
      var1.draw(var4);
      double var5 = var3.getX() - ((Point2D)var2).getX();
      double var7 = var3.getY() - ((Point2D)var2).getY();
      double var9 = Math.sqrt(var5 * var5 + var7 * var7);
      var5 = var5 / var9 * 7.0D;
      var7 = var7 / var9 * 7.0D;
      java.awt.geom.Line2D.Double var11 = new java.awt.geom.Line2D.Double();
      var11.setLine(((Point2D)var2).getX(), ((Point2D)var2).getY(), ((Point2D)var2).getX() - (-var5 - var7), ((Point2D)var2).getY() - (-var7 + var5));
      var1.draw(var11);
      var11.setLine(((Point2D)var2).getX(), ((Point2D)var2).getY(), ((Point2D)var2).getX() - (-var5 + var7), ((Point2D)var2).getY() - (-var7 - var5));
      var1.draw(var11);
    }

  }
}