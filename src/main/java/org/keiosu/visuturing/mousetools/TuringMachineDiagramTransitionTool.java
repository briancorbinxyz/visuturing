package org.keiosu.visuturing.mousetools;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.lang.invoke.MethodHandles;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.keiosu.visuturing.core.State;
import org.keiosu.visuturing.core.Symbols;
import org.keiosu.visuturing.core.Transition;
import org.keiosu.visuturing.diagram.DiagramEditor;
import org.keiosu.visuturing.gui.TransitionFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TuringMachineDiagramTransitionTool extends TuringMachineDiagramTool {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    Transition newTrans;
    private int mouseClicks = 0;
    private TransitionFrame transitionEditor;
    Point currentPoint;

    public TuringMachineDiagramTransitionTool(DiagramEditor diagramEditor) {
        super(diagramEditor);

        try {
            Toolkit tk = Toolkit.getDefaultToolkit();
            this.setCursor(
                    tk.createCustomCursor(
                            this.createImageIcon("cursors/add-transition.gif").getImage(),
                            new Point(0, 0),
                            "AddTransition"));
            this.setOverCursor(this.cursor);
        } catch (Exception e) {
            LOG.atError().setCause(e.getCause()).log("Failed whilst creating transition tool.");
        }
    }

    public void mousePressed(MouseEvent event) {
        Point2D eventPoint = getEventPoint(event);
        List<State> states = this.diagramEditor.getCurrentMachine().getStates();

        for (State state : states) {
            if (state.contains(eventPoint)) {
                if (state.getName().equals(Symbols.STATE_HALTING_STATE) && this.mouseClicks == 0) {
                    JOptionPane.showMessageDialog(
                            null,
                            "You cannot create transitions out of the halting state.",
                            "VisuTuring",
                            JOptionPane.ERROR_MESSAGE,
                            null);
                    return;
                }

                this.diagramEditor.setSelectedState(state);
                this.diagramEditor.setSelectedTransition(null);
                break;
            }
        }
    }

    Point2D getEventPoint(MouseEvent event) {
        if (SwingUtilities.isRightMouseButton(event)) {
            this.diagramEditor.revertToSelect();
            this.diagramEditor.repaint();
        }

        this.currentPoint = event.getPoint();
        return this.diagramEditor.toUser(
                new Double(this.currentPoint.getX(), this.currentPoint.getY()));
    }

    public void mouseReleased(MouseEvent event) {
        this.currentPoint = event.getPoint();
        State state = this.diagramEditor.getSelectedState();
        if (state != null && this.mouseClicks == 0) {
            double x = state.getLocation().getX();
            double y = state.getLocation().getY();
            this.newTrans = new Transition(state.getName(), '0', state.getName(), '0');
            this.newTrans.setP1(new Double(x, y));
            ++this.mouseClicks;
            this.diagramEditor.setSelectedTransition(null);
        } else if (state != null && this.mouseClicks == 1) {
            double x = state.getLocation().getX();
            double y = state.getLocation().getY();
            if (this.newTrans != null) {
                this.newTrans.setP2(new Double(x, y));
                Point2D p1 = this.newTrans.getP1();
                Point2D p2 = this.newTrans.getP2();
                this.newTrans.setNextState(state.getName());
                this.newTrans.setTask(Symbols.SPACE);
                this.newTrans.setCurrentSymbol(Symbols.SPACE);
                this.transitionEditor =
                        new TransitionFrame(this.diagramEditor.getCurrentMachine(), this.newTrans);
                this.newTrans.setControlPoint(
                        new Double(
                                p1.getX() + (p2.getX() - p1.getX()) / 2.0D,
                                p1.getY() + (p2.getY() - p1.getY()) / 2.0D));
                if (this.newTrans.getNextState().equals(this.newTrans.getCurrentState())) {
                    this.newTrans.setControlPoint(new Double(p1.getX(), p1.getY() - 60.0D));
                }

                repaintOnEvent(event);
            }

            this.mouseClicks = 0;
            this.diagramEditor.setSelectedTransition(null);
        }
    }

    protected void repaintOnEvent(MouseEvent event) {
        Point cp = new Point(this.currentPoint);
        SwingUtilities.convertPointToScreen(cp, (Component) event.getSource());
        this.transitionEditor.setLocation(cp);
        this.transitionEditor.setVisible(true);
        this.newTrans = this.transitionEditor.getTransition();
        this.diagramEditor.getCurrentMachine().addTransition(this.newTrans);
        this.newTrans = null;
        this.diagramEditor.repaint();
    }

    public void mouseDragged(MouseEvent event) {
        mouseEvent(event);
    }

    private void mouseEvent(MouseEvent event) {
        this.currentPoint = event.getPoint();
        Point2D eventPoint =
                this.diagramEditor.toUser(
                        new Double(this.currentPoint.getX(), this.currentPoint.getY()));
        List<State> states = this.diagramEditor.getCurrentMachine().getStates();

        for (State state : states) {
            if (state.contains(eventPoint)) {
                eventPoint = this.diagramEditor.toWorld(state.getLocation());
                this.currentPoint = new Point((int) eventPoint.getX(), (int) eventPoint.getY());
                break;
            }
        }
        this.diagramEditor.repaint();
    }

    public void mouseMoved(MouseEvent event) {
        mouseEvent(event);
    }

    public void postDraw(Graphics2D canvas) {}

    public void preDraw(Graphics2D canvas) {
        Point2D eventPoint = new Double();
        if (this.currentPoint != null) {
            eventPoint =
                    this.diagramEditor.toUser(
                            new Double(this.currentPoint.getX(), this.currentPoint.getY()));
        }

        if (this.newTrans != null) {
            canvas.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            canvas.setColor(Color.BLACK);
            Point2D p1 = this.newTrans.getP1();
            java.awt.geom.Line2D.Double eventLine = new java.awt.geom.Line2D.Double(p1, eventPoint);
            canvas.draw(eventLine);
            double ex = p1.getX() - eventPoint.getX();
            double ey = p1.getY() - eventPoint.getY();
            double ec = Math.sqrt(ex * ex + ey * ey);
            ex = ex / ec * 7.0D;
            ey = ey / ec * 7.0D;
            diagram.drawArrowHead(canvas, eventPoint, ex, ey);
        }
    }
}
