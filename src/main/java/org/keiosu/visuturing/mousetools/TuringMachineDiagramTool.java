package org.keiosu.visuturing.mousetools;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.event.MouseInputAdapter;
import org.keiosu.visuturing.core.Transition;
import org.keiosu.visuturing.core.TuringMachine;
import org.keiosu.visuturing.diagram.Diagram;
import org.keiosu.visuturing.diagram.DiagramEditor;

public abstract class TuringMachineDiagramTool extends MouseInputAdapter implements MouseListener {
    protected DiagramEditor diagramEditor;
    protected TuringMachine machine;
    protected Diagram diagram;
    protected Cursor cursor;
    protected Cursor overCursor;

    public TuringMachineDiagramTool(DiagramEditor diagramEditor) {
        this.diagramEditor = diagramEditor;
        this.diagram = diagramEditor.diagram;
        this.machine = diagramEditor.getCurrentMachine();
    }

    public ImageIcon createImageIcon(String imageResource) {
        return new ImageIcon(
                Objects.requireNonNull(
                        this.getClass().getClassLoader().getResource(imageResource)));
    }

    public Cursor getCursor() {
        return this.cursor;
    }

    public Cursor getOverCursor() {
        return this.overCursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public void setOverCursor(Cursor cursor) {
        this.overCursor = cursor;
    }

    public void setCursor(int cursor) {
        this.cursor = Cursor.getPredefinedCursor(cursor);
    }

    public void setOverCursor(int cursor) {
        this.overCursor = Cursor.getPredefinedCursor(cursor);
    }

    public boolean isOver(Transition transition, Point2D point) {
        return new BasicStroke(10.0F)
                .createStrokedShape(
                        this.diagramEditor.diagram.transitionCurve(
                                transition, machine.stateFor(transition.getCurrentState())))
                .contains(point);
    }

    public abstract void preDraw(Graphics2D canvas);

    public abstract void postDraw(Graphics2D canvas);
}
