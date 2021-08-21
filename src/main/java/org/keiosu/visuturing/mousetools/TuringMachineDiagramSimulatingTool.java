package org.keiosu.visuturing.mousetools;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.util.Optional.ofNullable;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D.Double;
import java.awt.geom.Point2D;
import javax.annotation.Nullable;
import org.keiosu.visuturing.core.State;
import org.keiosu.visuturing.core.Transition;
import org.keiosu.visuturing.core.TuringMachine;
import org.keiosu.visuturing.diagram.Diagram;
import org.keiosu.visuturing.diagram.DiagramEditor;

public class TuringMachineDiagramSimulatingTool extends TuringMachineDiagramTool {

    private final TuringMachine machine;
    private final Diagram diagram;
    private State currentState;
    private Transition transition;
    private int frame;

    private static final double MAX_FRAMES = 10.0D;
    private static final int INITIAL_FRAME = 1;

    public TuringMachineDiagramSimulatingTool(DiagramEditor editor) {
        super(editor);
        machine = editor.getCurrentMachine();
        diagram = editor.diagram;
        frame = INITIAL_FRAME;
    }

    public void preDraw(Graphics2D canvas) {
        if (transition == null) {
            return;
        }
        canvas.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        canvas.setColor(Color.RED);
        State state = machine.stateFor(transition.getCurrentState());
        canvas.fill(diagram.transitionPoint(transition, state, frame / MAX_FRAMES));
    }

    public void increaseFrame() {
        ++frame;
    }

    public int getFrame() {
        return frame;
    }

    public void postDraw(Graphics2D canvas) {
        canvas.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        canvas.setColor(new Color(255, 40, 40, 200));
        canvas.setStroke(new BasicStroke(3.0F));
        if (currentState != null) {
            Point2D p = currentState.getLocation();
            var highlight = new Double(p.getX() - 20.0D, p.getY() - 20.0D, 40.0D, 40.0D);
            canvas.fill(highlight);
            canvas.setColor(new Color(255, 40, 40));
            var border = new Double(p.getX() - 30.0D, p.getY() - 30.0D, 60.0D, 60.0D);
            canvas.draw(border);
        }
    }

    public void setStateFromAlphabet(@Nullable String state) {
        currentState = ofNullable(state).map(machine::stateFor).orElse(null);
    }

    public void setTransition(Transition transition) {
        this.transition = transition;
    }

    public void reset() {
        this.frame = INITIAL_FRAME;
    }
}
