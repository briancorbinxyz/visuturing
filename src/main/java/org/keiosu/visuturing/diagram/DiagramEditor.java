package org.keiosu.visuturing.diagram;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D.Double;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.swing.JPanel;
import org.keiosu.visuturing.core.State;
import org.keiosu.visuturing.core.Symbols;
import org.keiosu.visuturing.core.Transition;
import org.keiosu.visuturing.core.TuringMachine;
import org.keiosu.visuturing.gui.panels.DiagramPrinter;
import org.keiosu.visuturing.mousetools.TuringMachineDiagramSelectTool;
import org.keiosu.visuturing.mousetools.TuringMachineDiagramTool;
import org.keiosu.visuturing.persistence.Persistence;

public class DiagramEditor extends JPanel {
    public static double BORDER = 20.0D;
    public static final Color STATE_COLOUR = new Color(255, 255, 255);
    public static final Color HALTING_STATE_COLOUR = new Color(255, 255, 255);
    public static final Color SELECTED_STATE_COLOUR = new Color(255, 255, 204);
    public static final Color SELECTED_TRANSITION_COLOUR;
    public static final int TRANSITION_MARGIN = 10;
    public static final int ARROW_LENGTH = 7;
    public boolean gridOn;
    private final TuringMachine currentMachine;
    private State selectedState;
    private Transition selectedTransition;
    public Diagram diagram;
    private DiagramEditor.Mousey m;
    private TuringMachineDiagramTool tool;
    protected AffineTransform transformation;
    private double[] zoomList;
    private int zoomIndex;
    private boolean showDescription;

    public DiagramEditor(TuringMachine machine) {
        this.currentMachine = machine;
        this.gridOn = true;
        this.selectedState = null;
        this.tool = new TuringMachineDiagramSelectTool(this);
        this.transformation = new AffineTransform();
        this.diagram = new Diagram();
        this.m = new DiagramEditor.Mousey();
        this.addMouseListener(this.m);
        this.addMouseMotionListener(this.m);
        this.zoomList = new double[] {0.25D, 0.5D, 1.0D, 1.25D, 1.5D, 1.75D, 2.0D, 3.0D, 4.0D};
        this.zoomIndex = 2;
        this.setBackground(Color.white);
        this.showDescription = false;
    }

    public void revertToSelect() {
        this.setTool(new TuringMachineDiagramSelectTool(this));
    }

    public void print() {
        DiagramPrinter diagramPrinter = new DiagramPrinter(this);
        diagramPrinter.setVisible(true);
        if (diagramPrinter.didSucceed()) {
            diagramPrinter.printIt();
        }
    }

    public void drawState(State state, Graphics2D graphics2D) {
        double x = state.getLocation().getX();
        double y = state.getLocation().getY();
        Double stateShape =
                new Double(x - Diagram.STATE_RADIUS, y - Diagram.STATE_RADIUS, 40.0D, 40.0D);
        if (state.getName().equals(Symbols.STATE_HALTING_STATE)) {
            graphics2D.setColor(HALTING_STATE_COLOUR);
        } else if (this.selectedState == state) {
            graphics2D.setColor(SELECTED_STATE_COLOUR);
        } else {
            graphics2D.setColor(STATE_COLOUR);
        }

        graphics2D.fill(stateShape);
        graphics2D.setColor(Color.BLACK);
        graphics2D.draw(stateShape);
        if (state.getName().equals(Symbols.STATE_HALTING_STATE)) {
            graphics2D.draw(
                    new Double(
                            x - Diagram.STATE_RADIUS + 5.0D,
                            y - Diagram.STATE_RADIUS + 5.0D,
                            30.0D,
                            30.0D));
        }

        graphics2D.setColor(Color.BLACK);
        int ascent = graphics2D.getFontMetrics().getAscent();
        double width =
                graphics2D.getFontMetrics().getStringBounds(state.getName(), graphics2D).getWidth();
        if (width < 30.0D) {
            graphics2D.drawString(
                    state.getName(),
                    (float) (x - width / 2.0D),
                    (float) (y + (double) (ascent / 2)));
        } else {
            graphics2D.drawString(state.getName(), (float) (x - width / 2.0D), (float) (y + 40.0D));
        }

        if (state.getName().equals(Symbols.STATE_BEGINNING_STATE)) {
            graphics2D.draw(
                    new java.awt.geom.Line2D.Double(
                            x - Diagram.STATE_RADIUS - 12.0D,
                            y - 12.0D,
                            x - Diagram.STATE_RADIUS,
                            y));
            graphics2D.draw(
                    new java.awt.geom.Line2D.Double(
                            x - Diagram.STATE_RADIUS - 12.0D,
                            y + 12.0D,
                            x - Diagram.STATE_RADIUS,
                            y));
        }
    }

    public void drawTransition(Transition transition, Graphics2D graphics2D) {
        Point2D p1 = transition.getP1();
        Point2D p2 = transition.getP2();
        Point2D controlPoint = transition.getControlPoint();
        if (transition == this.selectedTransition) {
            graphics2D.setColor(SELECTED_TRANSITION_COLOUR);
            graphics2D.setStroke(new BasicStroke(2.0F));
        } else {
            graphics2D.setColor(Color.BLACK);
            graphics2D.setStroke(new BasicStroke(1.0F));
        }

        Point2D cp;
        String var9;
        Point var12;
        int var13;
        int var14;
        if (!transition.getCurrentState().equals(transition.getNextState())) {
            QuadCurve2D var8 =
                    (QuadCurve2D)
                            this.diagram.transitionCurve(
                                    transition,
                                    this.currentMachine.stateFor(transition.getCurrentState()));
            graphics2D.draw(var8);
            cp = var8.getCtrlPt();
            p2 = var8.getP2();
            var9 = transition.getCurrentSymbol() + " / " + transition.getTask();
            java.awt.geom.QuadCurve2D.Double var10 = new java.awt.geom.QuadCurve2D.Double();
            java.awt.geom.QuadCurve2D.Double var11 = new java.awt.geom.QuadCurve2D.Double();
            var8.subdivide(var10, var11);
            var12 = new Point((int) var10.getP2().getX(), (int) var10.getP2().getY());
            var13 = (int) graphics2D.getFontMetrics().getStringBounds(var9, graphics2D).getWidth();
            var14 = TRANSITION_MARGIN;
            if (var10.getY1() < var10.getY2()) {
                var14 = -(var14 + graphics2D.getFontMetrics().getAscent());
            }

            graphics2D.drawString(
                    var9, (int) (var12.getX() - (double) (var13 / 2)), (int) var12.getY() - var14);
        } else {
            CubicCurve2D var15 =
                    (CubicCurve2D)
                            this.diagram.transitionCurve(
                                    transition,
                                    this.currentMachine.stateFor(transition.getCurrentState()));
            graphics2D.draw(var15);
            cp = var15.getCtrlP2();
            p2 = var15.getP2();
            var9 = transition.getCurrentSymbol() + " / " + transition.getTask();
            java.awt.geom.CubicCurve2D.Double var17 = new java.awt.geom.CubicCurve2D.Double();
            java.awt.geom.CubicCurve2D.Double var19 = new java.awt.geom.CubicCurve2D.Double();
            var15.subdivide(var17, var19);
            var12 = new Point((int) var17.getP2().getX(), (int) var17.getP2().getY());
            var13 = (int) graphics2D.getFontMetrics().getStringBounds(var9, graphics2D).getWidth();
            var14 = TRANSITION_MARGIN;
            if (var17.getY1() < var17.getY2()) {
                var14 = -(var14 + graphics2D.getFontMetrics().getAscent());
            }

            graphics2D.drawString(
                    var9, (int) (var12.getX() - (double) (var13 / 2)), (int) var12.getY() - var14);
        }

        double var16 = cp.getX() - p2.getX();
        double var18 = cp.getY() - p2.getY();
        double var20 = Math.sqrt(var16 * var16 + var18 * var18);
        var16 = var16 / var20 * ARROW_LENGTH;
        var18 = var18 / var20 * ARROW_LENGTH;
        java.awt.geom.Line2D.Double var21 = new java.awt.geom.Line2D.Double();
        var21.setLine(
                p2.getX(), p2.getY(), p2.getX() - (-var16 - var18), p2.getY() - (-var18 + var16));
        graphics2D.draw(var21);
        var21.setLine(
                p2.getX(), p2.getY(), p2.getX() - (-var16 + var18), p2.getY() - (-var18 - var16));
        graphics2D.draw(var21);
    }

    public void setGrid(boolean var1) {
        this.gridOn = var1;
    }

    public void paintComponent(Graphics var1) {
        super.paintComponent(var1);
        Graphics2D var2 = (Graphics2D) var1;
        if (this.gridOn) {
            var2.setColor(new Color(230, 230, 230));
            int var3 = this.getWidth();
            int var4 = this.getHeight();

            int var5;
            for (var5 = 0; var5 < var3 / 20; ++var5) {
                var2.drawLine(var5 * 20, 0, var5 * 20, var4);
            }

            for (var5 = 0; var5 < var3 / 20; ++var5) {
                var2.drawLine(0, var5 * 20, var3, var5 * 20);
            }
        }

        var2.scale(this.transformation.getScaleX(), this.transformation.getScaleY());
        this.tool.preDraw(var2);
        this.render(var2);
        this.tool.postDraw(var2);
    }

    public void render(Graphics2D var1) {
        FontMetrics var2 = var1.getFontMetrics();
        var1.setFont(new Font("Helvetica", 0, 14));
        var1.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        var1.setColor(Color.BLACK);
        List var3 = this.currentMachine.getTransitions();

        for (int var4 = 0; var4 < var3.size(); ++var4) {
            Transition var5 = (Transition) var3.get(var4);
            this.drawTransition(var5, var1);
        }

        var1.setStroke(new BasicStroke(1.0F));
        List var7 = this.currentMachine.getStates();

        for (int var8 = 0; var8 < var7.size(); ++var8) {
            State var6 = (State) var7.get(var8);
            this.drawState(var6, var1);
        }
    }

    public void setTool(TuringMachineDiagramTool var1) {
        this.tool = var1;
        this.setCursor(var1.getCursor());
    }

    public void setSelectedState(State var1) {
        this.selectedState = var1;
    }

    public void setSelectedTransition(Transition var1) {
        this.selectedTransition = var1;
    }

    public TuringMachineDiagramTool getTool() {
        return this.tool;
    }

    public TuringMachine getCurrentMachine() {
        return this.currentMachine;
    }

    public State getSelectedState() {
        return this.selectedState;
    }

    public Transition getSelectedTransition() {
        return this.selectedTransition;
    }

    public void setTransformation(AffineTransform var1) {
        this.transformation = var1;
    }

    public void zoomIn() {
        ++this.zoomIndex;
        if (this.zoomIndex >= this.zoomList.length) {
            this.zoomIndex = this.zoomList.length - 1;
        }

        this.transformation = new AffineTransform();
        this.transformation.scale(this.zoomList[this.zoomIndex], this.zoomList[this.zoomIndex]);
        this.repaint();
    }

    public void zoomOut() {
        --this.zoomIndex;
        if (this.zoomIndex < 0) {
            this.zoomIndex = 0;
        }

        this.transformation = new AffineTransform();
        this.transformation.scale(this.zoomList[this.zoomIndex], this.zoomList[this.zoomIndex]);
        this.repaint();
    }

    public void setZoom(double var1) {
        this.transformation = new AffineTransform();
        this.transformation.scale(var1, var1);
        this.repaint();
    }

    public Point2D toWorld(Point2D var1) {
        java.awt.geom.Point2D.Double var2 = new java.awt.geom.Point2D.Double();
        this.transformation.transform(var1, var2);
        return var2;
    }

    public Point2D toUser(Point2D var1) {
        java.awt.geom.Point2D.Double var2 = new java.awt.geom.Point2D.Double();

        try {
            this.transformation.inverseTransform(var1, var2);
        } catch (NoninvertibleTransformException var4) {
        } catch (Exception var5) {
        }

        return var2;
    }

    public BufferedImage makeBuffer(int var1, int var2) {
        BufferedImage var3 = new BufferedImage(var1, var2, 1);
        Graphics2D var4 = var3.createGraphics();
        var4.setColor(Color.WHITE);
        var4.fillRect(0, 0, var1, var2);
        this.render(var4);
        var4.dispose();
        return var3;
    }

    public boolean showDescription() {
        return this.showDescription;
    }

    public void setDescriptionShown(boolean var1) {
        this.showDescription = var1;
    }

    public Dimension getExtents() {
        Dimension var1 = new Dimension();
        double var2 = 200.0D;
        double var4 = 200.0D;
        List var6 = this.currentMachine.getStates();

        for (int var7 = 0; var7 < var6.size(); ++var7) {
            State var8 = (State) var6.get(var7);
            double var9 = var8.getLocation().getX();
            double var11 = var8.getLocation().getY();
            if (var2 < var9 + Diagram.STATE_RADIUS + BORDER) {
                var2 = var9 + Diagram.STATE_RADIUS + BORDER;
            }

            if (var4 < var11 + Diagram.STATE_RADIUS + BORDER) {
                var4 = var11 + Diagram.STATE_RADIUS + BORDER;
            }
        }

        List var14 = this.currentMachine.getTransitions();
        BasicStroke var15 = new BasicStroke(2.0F);

        for (int var16 = 0; var16 < var14.size(); ++var16) {
            Transition var10 = (Transition) var14.get(var16);
            Shape var13 =
                    this.diagram.transitionCurve(
                            var10, this.currentMachine.stateFor(var10.getCurrentState()));
            Rectangle var12 = var15.createStrokedShape(var13).getBounds();
            if (var2 < var12.getX() + var12.getWidth() + BORDER) {
                var2 = var12.getX() + var12.getWidth() + BORDER;
            }

            if (var4 < var12.getY() + var12.getHeight() + BORDER) {
                var4 = var12.getY() + var12.getHeight() + BORDER;
            }
        }

        var1.setSize(var2 + 2.0D * BORDER, var4 + 2.0D * BORDER);
        return var1;
    }

    public void exportToJPEG(File var1) {
        this.setSelectedState((State) null);
        this.setSelectedTransition((Transition) null);
        Dimension var2 = this.getExtents();
        BufferedImage var3 = this.makeBuffer((int) var2.getWidth(), (int) var2.getHeight());
        Persistence.saveJPEG(var3, var1.toString());
    }

    static {
        SELECTED_TRANSITION_COLOUR = Color.BLACK;
    }

    public class Mousey extends MouseAdapter implements MouseMotionListener {
        public Mousey() {}

        public void mousePressed(MouseEvent var1) {
            DiagramEditor.this.tool.mousePressed(var1);
        }

        public void mouseReleased(MouseEvent var1) {
            DiagramEditor.this.tool.mouseReleased(var1);
        }

        public void mouseMoved(MouseEvent var1) {
            DiagramEditor.this.tool.mouseMoved(var1);
        }

        public void mouseDragged(MouseEvent var1) {
            DiagramEditor.this.tool.mouseDragged(var1);
        }
    }
}
