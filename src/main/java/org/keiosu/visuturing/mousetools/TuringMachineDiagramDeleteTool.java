package org.keiosu.visuturing.mousetools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.keiosu.visuturing.core.Transition;
import org.keiosu.visuturing.diagram.DiagramEditor;

public class TuringMachineDiagramDeleteTool extends TuringMachineDiagramTool {
    private Transition currentTransition = null;

    public void preDraw(Graphics2D canvas) {}

    public void postDraw(Graphics2D canvas) {
        if (this.currentTransition != null) {
            canvas.setColor(Color.red);
            canvas.setStroke(new BasicStroke(3.0F));
            Shape transitionCurve =
                    this.diagram.transitionCurve(
                            this.currentTransition,
                            this.machine.stateFor(this.currentTransition.getCurrentState()));
            if (transitionCurve instanceof QuadCurve2D) {
                QuadCurve2D curve = (QuadCurve2D) transitionCurve;
                drawCurveCross(canvas, curve);
            } else {
                CubicCurve2D curve = (CubicCurve2D) transitionCurve;
                drawCurveCross(canvas, curve);
            }
        }
    }

    private void drawCurveCross(Graphics2D canvas, QuadCurve2D curve) {
        QuadCurve2D.Double lhs = new QuadCurve2D.Double();
        curve.subdivide(lhs, null);
        drawCurveCross(canvas, lhs.getP2());
    }

    private void drawCurveCross(Graphics2D canvas, CubicCurve2D curve) {
        CubicCurve2D.Double lhs = new CubicCurve2D.Double();
        curve.subdivide(lhs, null);
        drawCurveCross(canvas, lhs.getP2());
    }

    private void drawCurveCross(Graphics2D canvas, Point2D p2) {
        canvas.draw(
                new java.awt.geom.Line2D.Double(
                        p2.getX() - 10.0D,
                        p2.getY() - 10.0D,
                        p2.getX() + 10.0D,
                        p2.getY() + 10.0D));
        canvas.draw(
                new java.awt.geom.Line2D.Double(
                        p2.getX() - 10.0D,
                        p2.getY() + 10.0D,
                        p2.getX() + 10.0D,
                        p2.getY() - 10.0D));
    }

    public TuringMachineDiagramDeleteTool(DiagramEditor editor) {
        super(editor);

        try {
            this.setCursor(
                    Toolkit.getDefaultToolkit().createCustomCursor(
                            this.createImageIcon("cursors/delete.gif").getImage(),
                            new Point(9, 9),
                            "Remove"));
            this.setOverCursor(this.cursor);
        } catch (Exception ignored) {
            // do nothing
        }
    }

    public void mousePressed(MouseEvent event) {
        if (SwingUtilities.isRightMouseButton(event)) {
            this.diagramEditor.revertToSelect();
            this.diagramEditor.repaint();
        }

        if (this.currentTransition != null) {
            if (JOptionPane.showConfirmDialog(
                null,
                    "Are you sure you want to remove the transition: "
                            + this.currentTransition
                            + " ?",
                    "VisuTuring - Transition Removal",
                JOptionPane.YES_NO_CANCEL_OPTION) == 0) {
                this.diagramEditor.getCurrentMachine().removeTransition(this.currentTransition);
                this.diagramEditor.repaint();
            }
        }
    }

    public void mouseMoved(MouseEvent event) {
        Point eventPoint = event.getPoint();
        Point2D userPoint =
                this.diagramEditor.toUser(
                        new java.awt.geom.Point2D.Double(eventPoint.getX(), eventPoint.getY()));
        List<Transition> transitions = this.diagramEditor.getCurrentMachine().getTransitions();
        for (Transition transition : transitions) {
            if (this.isOver(transition, userPoint)) {
                this.currentTransition = transition;
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
