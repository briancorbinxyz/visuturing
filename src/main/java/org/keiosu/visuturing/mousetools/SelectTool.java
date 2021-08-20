package org.keiosu.visuturing.mousetools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.keiosu.visuturing.core.State;
import org.keiosu.visuturing.core.Transition;
import org.keiosu.visuturing.core.TuringMachine;
import org.keiosu.visuturing.diagram.DiagramEditor;
import org.keiosu.visuturing.gui.TransitionFrame;

public class SelectTool extends AbstractMouseTool {
    private boolean overHandle = false;
    private Point lastPoint;
    public static final Dimension HANDLE_DIMENSION = new Dimension(10, 10);
    public static final Color HANDLE_COLOUR = new Color(10, 10, 255);

    public SelectTool(DiagramEditor var1) {
        super(var1);
        this.setCursor(0);
        this.setOverCursor(13);
    }

    public void mousePressed(MouseEvent var1) {
        Point var2 = var1.getPoint();
        Point2D var3 = this.diagram.toUser(new Double(var2.getX(), var2.getY()));
        List var4 = this.diagram.getCurrentMachine().getStates();
        State var5 = this.diagram.getSelectedState();
        Transition var6 = this.diagram.getSelectedTransition();

        for (int var7 = 0; var7 < var4.size(); ++var7) {
            State var8 = (State) var4.get(var7);
            if (var8.contains(var3)) {
                this.diagram.setSelectedState(var8);
                this.diagram.setSelectedTransition((Transition) null);
                this.diagram.repaint();
                this.overHandle = false;
                break;
            }
        }

        if (this.diagram.getSelectedState() == null) {
            if (var6 != null) {
                Rectangle var10 = new Rectangle(HANDLE_DIMENSION);
                var10.setLocation(
                        (int) (var6.getControlPoint().getX() - HANDLE_DIMENSION.getWidth() / 2.0D),
                        (int)
                                (var6.getControlPoint().getY()
                                        - HANDLE_DIMENSION.getHeight() / 2.0D));
                if (var1.getClickCount() > 1) {
                    TransitionFrame var12 =
                            new TransitionFrame(this.diagram.getCurrentMachine(), var6);
                    Point var9 = new Point(var2);
                    SwingUtilities.convertPointToScreen(var9, (Component) var1.getSource());
                    var12.setLocation(var9);
                    var12.setVisible(true);
                    if (var12.getTransition() != null) {
                        this.diagram.getCurrentMachine().removeTransition(var6);
                        var6 = var12.getTransition();
                        this.diagram.getCurrentMachine().addTransition(var6);
                        this.diagram.repaint();
                    } else {
                        JOptionPane.showMessageDialog(
                                this.diagram,
                                "Transitions reading the left end marker can only\ngo right.",
                                "Invalid Transition",
                                0,
                                (Icon) null);
                    }
                }

                if (var10.contains(this.diagram.toUser(this.lastPoint))) {
                    this.overHandle = true;
                } else {
                    this.overHandle = false;
                }
            }

            if (!this.overHandle) {
                List var11 = this.diagram.getCurrentMachine().getTransitions();

                for (int var13 = 0; var13 < var11.size(); ++var13) {
                    Transition var14 = (Transition) var11.get(var13);
                    if (this.isOver(var14, var3) && var14 != this.diagram.getSelectedTransition()) {
                        this.diagram.setSelectedTransition(var14);
                        this.diagram.repaint();
                        break;
                    }
                }
            }
        }

        this.lastPoint = var2;
    }

    public void mouseReleased(MouseEvent var1) {
        Point var2 = var1.getPoint();
        State var3 = this.diagram.getSelectedState();
        Transition var4 = this.diagram.getSelectedTransition();
        if (var3 != null) {
            double var5 =
                    this.diagram.toUser(var2).getX() - this.diagram.toUser(this.lastPoint).getX();
            double var7 =
                    this.diagram.toUser(var2).getY() - this.diagram.toUser(this.lastPoint).getY();
            Point2D var9 = var3.getLocation();
            var9.setLocation(var9.getX() + var5, var9.getY() + var7);
            var3.setLocation(var9);
            this.diagram.getCurrentMachine().setChanged(true);
            this.diagram.setSelectedState((State) null);
            this.diagram.repaint();
            TuringMachine var10 = this.diagram.getCurrentMachine();
            List var11 = var10.getTransitions();

            for (int var12 = 0; var12 < var11.size(); ++var12) {
                Transition var13 = (Transition) var11.get(var12);
                if (var13.getCurrentState().equals(var3.getName())
                        && var13.getCurrentState().equals(var13.getNextState())) {
                    Point2D var14 = var13.getControlPoint();
                    Double var15 = new Double(var14.getX() + var5, var14.getY() + var7);
                    var13.setControlPoint(var15);
                }
            }
        } else if (var4 != null) {
            this.moveHandle(var2);
            this.diagram.setSelectedState((State) null);
            this.diagram.repaint();
        }

        if (var1.getButton() != 1) {
            this.diagram.setSelectedTransition((Transition) null);
        }
    }

    private void moveHandle(Point var1) {
        Transition var2 = this.diagram.getSelectedTransition();
        double var3 = this.diagram.toUser(var1).getX() - this.diagram.toUser(this.lastPoint).getX();
        double var5 = this.diagram.toUser(var1).getY() - this.diagram.toUser(this.lastPoint).getY();
        Rectangle var7 = new Rectangle(HANDLE_DIMENSION);
        var7.setLocation(
                (int) (var2.getControlPoint().getX() - HANDLE_DIMENSION.getWidth() / 2.0D),
                (int) (var2.getControlPoint().getY() - HANDLE_DIMENSION.getHeight() / 2.0D));
        if (var7.contains(this.diagram.toUser(this.lastPoint))) {
            Point2D var8 = var2.getControlPoint();
            var8.setLocation(var8.getX() + var3, var8.getY() + var5);
            var2.setControlPoint(var8);
            this.diagram.getCurrentMachine().setChanged(true);
        }
    }

    public void mouseDragged(MouseEvent var1) {
        Point var2 = var1.getPoint();
        State var3 = this.diagram.getSelectedState();
        Transition var4 = this.diagram.getSelectedTransition();
        if (var3 != null) {
            double var5 =
                    this.diagram.toUser(var2).getX() - this.diagram.toUser(this.lastPoint).getX();
            double var7 =
                    this.diagram.toUser(var2).getY() - this.diagram.toUser(this.lastPoint).getY();
            Point2D var9 = var3.getLocation();
            var9.setLocation(var9.getX() + var5, var9.getY() + var7);
            var3.setLocation(var9);
            this.diagram.repaint();
            List var10 = this.diagram.getCurrentMachine().getTransitions();

            for (int var11 = 0; var11 < var10.size(); ++var11) {
                Transition var12 = (Transition) var10.get(var11);
                if (var12.getCurrentState().equals(var3.getName())) {
                    var9 = var12.getP1();
                    var9.setLocation(var9.getX() + var5, var9.getY() + var7);
                    var12.setP1(var9);
                }

                if (var12.getNextState().equals(var3.getName())) {
                    var9 = var12.getP2();
                    var9.setLocation(var9.getX() + var5, var9.getY() + var7);
                    var12.setP2(var9);
                }

                if (var12.getCurrentState().equals(var3.getName())
                        && var12.getCurrentState().equals(var12.getNextState())) {
                    Point2D var13 = var12.getControlPoint();
                    Double var14 = new Double(var13.getX() + var5, var13.getY() + var7);
                    var12.setControlPoint(var14);
                }
            }
        } else if (var4 != null) {
            this.moveHandle(var2);
            this.diagram.repaint();
        }

        this.lastPoint = var2;
    }

    public void mouseMoved(MouseEvent var1) {
        Point var2 = var1.getPoint();
        Point2D var3 = this.diagram.toUser(new Double(var2.getX(), var2.getY()));
        List var4 = this.diagram.getCurrentMachine().getStates();

        for (int var5 = 0; var5 < var4.size(); ++var5) {
            State var6 = (State) var4.get(var5);
            if (var6.contains(var3)) {
                this.diagram.setCursor(this.getOverCursor());
                return;
            }
        }

        this.diagram.setCursor(this.getCursor());
    }

    public void preDraw(Graphics2D var1) {}

    public void postDraw(Graphics2D var1) {
        Transition var2 = this.diagram.getSelectedTransition();
        if (var2 != null) {
            var1.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            var1.setStroke(new BasicStroke(2.0F));
            Point2D var3 = var2.getControlPoint();
            java.awt.geom.Ellipse2D.Double var4 =
                    new java.awt.geom.Ellipse2D.Double(
                            (double) ((int) var3.getX()) - HANDLE_DIMENSION.getWidth() / 2.0D,
                            (double) ((int) var3.getY()) - HANDLE_DIMENSION.getHeight() / 2.0D,
                            HANDLE_DIMENSION.getWidth(),
                            HANDLE_DIMENSION.getHeight());
            var1.setColor(Color.LIGHT_GRAY);
            var1.fill(var4);
            var1.setColor(Color.black);
            var1.draw(var4);
        }
    }
}
