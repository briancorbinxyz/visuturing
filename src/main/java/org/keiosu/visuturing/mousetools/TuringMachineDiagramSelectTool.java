package org.keiosu.visuturing.mousetools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.keiosu.visuturing.core.State;
import org.keiosu.visuturing.core.Transition;
import org.keiosu.visuturing.core.TuringMachine;
import org.keiosu.visuturing.diagram.DiagramEditor;
import org.keiosu.visuturing.gui.TransitionFrame;

public class TuringMachineDiagramSelectTool extends TuringMachineDiagramTool {
    private boolean overHandle = false;
    private Point lastPoint;
    public static final Dimension HANDLE_DIMENSION = new Dimension(10, 10);

    public TuringMachineDiagramSelectTool(DiagramEditor diagramEditor) {
        super(diagramEditor);
        this.setCursor(Cursor.DEFAULT_CURSOR);
        this.setOverCursor(Cursor.MOVE_CURSOR);
    }

    public void mousePressed(MouseEvent event) {
        Point2D eventPoint =
                this.diagramEditor.toUser(
                        new Double(event.getPoint().getX(), event.getPoint().getY()));
        List<State> states = this.diagramEditor.getCurrentMachine().getStates();
        Transition selectedTransition = this.diagramEditor.getSelectedTransition();

        for (State state : states) {
            if (state.contains(eventPoint)) {
                this.diagramEditor.setSelectedState(state);
                this.diagramEditor.setSelectedTransition(null);
                this.diagramEditor.repaint();
                this.overHandle = false;
                break;
            }
        }

        if (this.diagramEditor.getSelectedState() == null) {
            if (selectedTransition != null) {
                Rectangle controlHandleArea = new Rectangle(HANDLE_DIMENSION);
                controlHandleArea.setLocation(
                        (int)
                                (selectedTransition.getControlPoint().getX()
                                        - HANDLE_DIMENSION.getWidth() / 2.0D),
                        (int)
                                (selectedTransition.getControlPoint().getY()
                                        - HANDLE_DIMENSION.getHeight() / 2.0D));
                if (event.getClickCount() > 1) {
                    TransitionFrame transitionFrame =
                            new TransitionFrame(
                                    this.diagramEditor.getCurrentMachine(), selectedTransition);
                    Point currentPoint = new Point(event.getPoint());
                    SwingUtilities.convertPointToScreen(
                            currentPoint, (Component) event.getSource());
                    transitionFrame.setLocation(currentPoint);
                    transitionFrame.setVisible(true);
                    if (transitionFrame.getTransition() != null) {
                        this.diagramEditor.getCurrentMachine().removeTransition(selectedTransition);
                        selectedTransition = transitionFrame.getTransition();
                        this.diagramEditor.getCurrentMachine().addTransition(selectedTransition);
                        this.diagramEditor.repaint();
                    } else {
                        JOptionPane.showMessageDialog(
                                this.diagramEditor,
                                "Transitions reading the left end marker can only\ngo right.",
                                "Invalid Transition",
                                JOptionPane.ERROR_MESSAGE,
                                null);
                    }
                }

                this.overHandle =
                        controlHandleArea.contains(this.diagramEditor.toUser(this.lastPoint));
            }

            if (!this.overHandle) {
                List<Transition> transitions =
                        this.diagramEditor.getCurrentMachine().getTransitions();

                for (Transition transition : transitions) {
                    if (this.isOver(transition, eventPoint)
                            && transition != this.diagramEditor.getSelectedTransition()) {
                        this.diagramEditor.setSelectedTransition(transition);
                        this.diagramEditor.repaint();
                        break;
                    }
                }
            }
        }

        this.lastPoint = event.getPoint();
    }

    public void mouseReleased(MouseEvent event) {
        Point eventPoint = event.getPoint();
        State selectedState = this.diagramEditor.getSelectedState();
        Transition selectedTransition = this.diagramEditor.getSelectedTransition();
        if (selectedState != null) {
            double ex =
                    this.diagramEditor.toUser(eventPoint).getX()
                            - this.diagramEditor.toUser(this.lastPoint).getX();
            double ey =
                    this.diagramEditor.toUser(eventPoint).getY()
                            - this.diagramEditor.toUser(this.lastPoint).getY();
            moveStateBy(selectedState, ex, ey);
            TuringMachine machine = this.diagramEditor.getCurrentMachine();
            List<Transition> transitions = machine.getTransitions();

            for (Transition transition : transitions) {
                if (transition.getCurrentState().equals(selectedState.getName())
                        && transition.getCurrentState().equals(transition.getNextState())) {
                    Point2D cp = transition.getControlPoint();
                    Double newControlPoint = new Double(cp.getX() + ex, cp.getY() + ey);
                    transition.setControlPoint(newControlPoint);
                }
            }
        } else if (selectedTransition != null) {
            this.moveHandle(eventPoint);
            this.diagramEditor.setSelectedState(null);
            this.diagramEditor.repaint();
        }

        if (event.getButton() != 1) {
            this.diagramEditor.setSelectedTransition(null);
        }
    }

    private void moveStateBy(State state, double ex, double ey) {
        Point2D stateLocation = state.getLocation();
        stateLocation.setLocation(stateLocation.getX() + ex, stateLocation.getY() + ey);
        state.setLocation(stateLocation);
        this.diagramEditor.getCurrentMachine().setChanged(true);
        this.diagramEditor.setSelectedState(null);
        this.diagramEditor.repaint();
    }

    private void moveHandle(Point point) {
        Transition transition = this.diagramEditor.getSelectedTransition();
        double ex =
                this.diagramEditor.toUser(point).getX()
                        - this.diagramEditor.toUser(this.lastPoint).getX();
        double ey =
                this.diagramEditor.toUser(point).getY()
                        - this.diagramEditor.toUser(this.lastPoint).getY();
        Rectangle boundingBox = new Rectangle(HANDLE_DIMENSION);
        boundingBox.setLocation(
                (int) (transition.getControlPoint().getX() - HANDLE_DIMENSION.getWidth() / 2.0D),
                (int) (transition.getControlPoint().getY() - HANDLE_DIMENSION.getHeight() / 2.0D));
        if (boundingBox.contains(this.diagramEditor.toUser(this.lastPoint))) {
            Point2D cp = transition.getControlPoint();
            cp.setLocation(cp.getX() + ex, cp.getY() + ey);
            transition.setControlPoint(cp);
            this.diagramEditor.getCurrentMachine().setChanged(true);
        }
    }

    public void mouseDragged(MouseEvent event) {
        Point eventPoint = event.getPoint();
        State selectedState = this.diagramEditor.getSelectedState();
        Transition selectedTransition = this.diagramEditor.getSelectedTransition();
        if (selectedState != null) {
            double ex =
                    this.diagramEditor.toUser(eventPoint).getX()
                            - this.diagramEditor.toUser(this.lastPoint).getX();
            double ey =
                    this.diagramEditor.toUser(eventPoint).getY()
                            - this.diagramEditor.toUser(this.lastPoint).getY();
            moveStateBy(selectedState, ex, ey);
            List<Transition> transitions = this.diagramEditor.getCurrentMachine().getTransitions();

            for (Transition transition : transitions) {
                if (transition.getCurrentState().equals(selectedState.getName())) {
                    Point2D transitionLocation = transition.getP1();
                    transitionLocation.setLocation(
                            transitionLocation.getX() + ex, transitionLocation.getY() + ey);
                    transition.setP1(transitionLocation);
                }

                if (transition.getNextState().equals(selectedState.getName())) {
                    Point2D transitionLocation = transition.getP2();
                    transitionLocation.setLocation(
                            transitionLocation.getX() + ex, transitionLocation.getY() + ey);
                    transition.setP2(transitionLocation);
                }

                if (transition.getCurrentState().equals(selectedState.getName())
                        && transition.getCurrentState().equals(transition.getNextState())) {
                    Point2D cp = transition.getControlPoint();
                    Double newControlPoint = new Double(cp.getX() + ex, cp.getY() + ey);
                    transition.setControlPoint(newControlPoint);
                }
            }
        } else if (selectedTransition != null) {
            this.moveHandle(eventPoint);
            this.diagramEditor.repaint();
        }

        this.lastPoint = eventPoint;
    }

    public void mouseMoved(MouseEvent event) {
        Point eventPoint = event.getPoint();
        Point2D userPoint =
                this.diagramEditor.toUser(new Double(eventPoint.getX(), eventPoint.getY()));
        List<State> states = this.diagramEditor.getCurrentMachine().getStates();

        for (State state : states) {
            if (state.contains(userPoint)) {
                this.diagramEditor.setCursor(this.getOverCursor());
                return;
            }
        }

        this.diagramEditor.setCursor(this.getCursor());
    }

    public void preDraw(Graphics2D canvas) {}

    public void postDraw(Graphics2D canvas) {
        Transition transition = this.diagramEditor.getSelectedTransition();
        if (transition != null) {
            drawControlHandle(canvas, transition);
        }
    }

    private void drawControlHandle(Graphics2D canvas, Transition transition) {
        canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        canvas.setStroke(new BasicStroke(2.0F));
        Point2D cp = transition.getControlPoint();
        java.awt.geom.Ellipse2D.Double controlHandle =
                new java.awt.geom.Ellipse2D.Double(
                        (double) ((int) cp.getX()) - HANDLE_DIMENSION.getWidth() / 2.0D,
                        (double) ((int) cp.getY()) - HANDLE_DIMENSION.getHeight() / 2.0D,
                        HANDLE_DIMENSION.getWidth(),
                        HANDLE_DIMENSION.getHeight());
        canvas.setColor(Color.LIGHT_GRAY);
        canvas.fill(controlHandle);
        canvas.setColor(Color.black);
        canvas.draw(controlHandle);
    }
}
