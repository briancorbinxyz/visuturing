package org.keiosu.visuturing.mousetools;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
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

    public ImageIcon createImageIcon(String var1) {
        return new ImageIcon(this.getClass().getClassLoader().getResource(var1));
    }

    public Cursor getCursor() {
        return this.cursor;
    }

    public Cursor getOverCursor() {
        return this.overCursor;
    }

    public void setCursor(Cursor var1) {
        this.cursor = var1;
    }

    public void setOverCursor(Cursor var1) {
        this.overCursor = var1;
    }

    public void setCursor(int var1) {
        this.cursor = Cursor.getPredefinedCursor(var1);
    }

    public void setOverCursor(int var1) {
        this.overCursor = Cursor.getPredefinedCursor(var1);
    }

    public boolean isOver(Transition var1, Point2D var2) {
        BasicStroke var3 = new BasicStroke(10.0F);
        return var3.createStrokedShape(
                        this.diagramEditor.diagram.transitionCurve(
                                var1, machine.stateFor(var1.getCurrentState())))
                .contains(var2);
    }

    public abstract void preDraw(Graphics2D var1);

    public abstract void postDraw(Graphics2D var1);
}
