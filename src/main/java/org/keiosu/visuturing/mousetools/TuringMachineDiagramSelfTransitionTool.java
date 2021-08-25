package org.keiosu.visuturing.mousetools;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.lang.invoke.MethodHandles;
import java.util.List;
import javax.swing.JOptionPane;
import org.keiosu.visuturing.core.State;
import org.keiosu.visuturing.core.Symbols;
import org.keiosu.visuturing.core.Transition;
import org.keiosu.visuturing.diagram.DiagramEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TuringMachineDiagramSelfTransitionTool extends TuringMachineDiagramTransitionTool {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public TuringMachineDiagramSelfTransitionTool(DiagramEditor diagramEditor) {
        super(diagramEditor);

        try {
            Toolkit tk = Toolkit.getDefaultToolkit();
            this.setCursor(
                    tk.createCustomCursor(
                            this.createImageIcon("cursors/addstran.gif").getImage(),
                            new Point(0, 0),
                            "AddSelfTransition"));
            this.setOverCursor(this.cursor);
        } catch (Exception e) {
            LOG.atError().setCause(e.getCause()).log("Failure during construction");
        }
    }

    public void mouseReleased(MouseEvent event) {
        Point2D eventPoint = getEventPoint(event);
        List<State> states = this.diagramEditor.getCurrentMachine().getStates();

        for (State state : states) {
            if (state.contains(eventPoint)) {
                if (state.getName().equals(Symbols.STATE_HALTING_STATE)) {
                    JOptionPane.showMessageDialog(
                            null,
                            "You cannot create transitions out of the halting state.",
                            "VisuTuring",
                            JOptionPane.ERROR_MESSAGE,
                            null);
                    return;
                }

                double x = state.getLocation().getX();
                double y = state.getLocation().getY();
                this.diagramEditor.setSelectedTransition(null);
                this.newTrans =
                        new Transition(
                                state.getName(), Symbols.SPACE, state.getName(), Symbols.SPACE);
                this.newTrans.setP1(new Double(x, y));
                this.newTrans.setP2(new Double(x, y));
                Point2D p1 = this.newTrans.getP1();
                this.newTrans.setControlPoint(new Double(p1.getX(), p1.getY() - 60.0D));
                repaintOnEvent(event);
                return;
            }
        }
    }

    public void postDraw(Graphics2D canvas) {}
}
