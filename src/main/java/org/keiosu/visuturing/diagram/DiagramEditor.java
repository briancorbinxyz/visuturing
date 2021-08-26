package org.keiosu.visuturing.diagram;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.invoke.MethodHandles;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiagramEditor extends JPanel {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static double BORDER = 20.0D;
    public static final Color STATE_COLOUR = new Color(255, 255, 255);
    public static final Color HALTING_STATE_COLOUR = new Color(255, 255, 255);
    public static final Color SELECTED_STATE_COLOUR = new Color(255, 255, 204);
    public static final Color SELECTED_TRANSITION_COLOUR;
    public static final int TRANSITION_MARGIN = 10;
    public boolean gridOn;
    private final TuringMachine currentMachine;
    private State selectedState;
    private Transition selectedTransition;
    public Diagram diagram;
    private TuringMachineDiagramTool tool;
    protected AffineTransform transformation;
    private final double[] zoomList;
    private int zoomIndex;

    public DiagramEditor(TuringMachine machine) {
        this.currentMachine = machine;
        this.gridOn = true;
        this.selectedState = null;
        this.tool = new TuringMachineDiagramSelectTool(this);
        this.transformation = new AffineTransform();
        this.diagram = new Diagram();
        MouseActionHandler mouse = new MouseActionHandler();
        this.addMouseListener(mouse);
        this.addMouseMotionListener(mouse);
        this.zoomList = new double[] {0.25D, 0.5D, 1.0D, 1.25D, 1.5D, 1.75D, 2.0D, 3.0D, 4.0D};
        this.zoomIndex = 2;
        this.setBackground(Color.white);
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

    public void drawTransition(Transition transition, Graphics2D canvas) {
        Point2D p2;
        if (transition == this.selectedTransition) {
            canvas.setColor(SELECTED_TRANSITION_COLOUR);
            canvas.setStroke(new BasicStroke(2.0F));
        } else {
            canvas.setColor(Color.BLACK);
            canvas.setStroke(new BasicStroke(1.0F));
        }

        Point2D cp;
        if (!transition.getCurrentState().equals(transition.getNextState())) {
            QuadCurve2D transitionCurve =
                    (QuadCurve2D)
                            this.diagram.transitionCurve(
                                    transition,
                                    this.currentMachine.stateFor(transition.getCurrentState()));
            canvas.draw(transitionCurve);
            cp = transitionCurve.getCtrlPt();
            p2 = transitionCurve.getP2();
            drawCurveLabel(transition, canvas, transitionCurve);
            diagram.drawArrowHead(canvas, p2, cp.getX() - p2.getX(), cp.getY() - p2.getY());
        } else {
            CubicCurve2D transitionCurve =
                    (CubicCurve2D)
                            this.diagram.transitionCurve(
                                    transition,
                                    this.currentMachine.stateFor(transition.getCurrentState()));
            canvas.draw(transitionCurve);
            cp = transitionCurve.getCtrlP2();
            p2 = transitionCurve.getP2();
            drawCurveLabel(transition, canvas, transitionCurve);
            diagram.drawArrowHead(canvas, p2, cp.getX() - p2.getX(), cp.getY() - p2.getY());
        }
    }

    private void drawCurveLabel(
            Transition transition, Graphics2D canvas, CubicCurve2D transitionCurve) {
        String actionOnSymbol;
        Point actionLabelPoint;
        int actionLabelBounds;
        int actionLabelMargin;
        actionOnSymbol = transition.getCurrentSymbol() + " / " + transition.getTask();
        CubicCurve2D.Double curveLhs = new CubicCurve2D.Double();
        transitionCurve.subdivide(curveLhs, null);
        actionLabelPoint = new Point((int) curveLhs.getP2().getX(), (int) curveLhs.getP2().getY());
        actionLabelBounds =
                (int) canvas.getFontMetrics().getStringBounds(actionOnSymbol, canvas).getWidth();
        actionLabelMargin = TRANSITION_MARGIN;
        if (curveLhs.getY1() < curveLhs.getY2()) {
            actionLabelMargin = -(actionLabelMargin + canvas.getFontMetrics().getAscent());
        }
        canvas.drawString(
                actionOnSymbol,
                (int) (actionLabelPoint.getX() - (double) (actionLabelBounds / 2)),
                (int) actionLabelPoint.getY() - actionLabelMargin);
    }

    private void drawCurveLabel(
            Transition transition, Graphics2D canvas, QuadCurve2D transitionCurve) {
        String actionOnSymbol;
        Point actionLabelPoint;
        int actionLabelBounds;
        int actionLabelMargin;
        actionOnSymbol = transition.getCurrentSymbol() + " / " + transition.getTask();
        QuadCurve2D curveLhs = new QuadCurve2D.Double();
        transitionCurve.subdivide(curveLhs, null);
        actionLabelPoint = new Point((int) curveLhs.getP2().getX(), (int) curveLhs.getP2().getY());
        actionLabelBounds =
                (int) canvas.getFontMetrics().getStringBounds(actionOnSymbol, canvas).getWidth();
        actionLabelMargin = TRANSITION_MARGIN;
        if (curveLhs.getY1() < curveLhs.getY2()) {
            actionLabelMargin = -(actionLabelMargin + canvas.getFontMetrics().getAscent());
        }
        canvas.drawString(
                actionOnSymbol,
                (int) (actionLabelPoint.getX() - (double) (actionLabelBounds / 2)),
                (int) actionLabelPoint.getY() - actionLabelMargin);
    }

    public void setGrid(boolean on) {
        this.gridOn = on;
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D canvas = (Graphics2D) graphics;
        if (this.gridOn) {
            canvas.setColor(new Color(230, 230, 230));
            int width = this.getWidth();
            int height = this.getHeight();

            for (int i = 0; i < width / 20; ++i) {
                canvas.drawLine(i * 20, 0, i * 20, height);
            }

            for (int i = 0; i < width / 20; ++i) {
                canvas.drawLine(0, i * 20, width, i * 20);
            }
        }

        canvas.scale(this.transformation.getScaleX(), this.transformation.getScaleY());
        this.tool.preDraw(canvas);
        this.render(canvas);
        this.tool.postDraw(canvas);
    }

    public void render(Graphics2D canvas) {
        canvas.setFont(new Font("Helvetica", Font.PLAIN, 14));
        canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        canvas.setColor(Color.BLACK);
        List<Transition> transitions = this.currentMachine.getTransitions();
        for (Transition transition : transitions) {
            this.drawTransition(transition, canvas);
        }
        canvas.setStroke(new BasicStroke(1.0F));
        List<State> states = this.currentMachine.getStates();
        for (State state : states) {
            this.drawState(state, canvas);
        }
    }

    public void setTool(TuringMachineDiagramTool tool) {
        this.tool = tool;
        this.setCursor(tool.getCursor());
    }

    public void setSelectedState(State state) {
        this.selectedState = state;
    }

    public void setSelectedTransition(Transition transition) {
        this.selectedTransition = transition;
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

    public void setZoom(double scale) {
        this.transformation = new AffineTransform();
        this.transformation.scale(scale, scale);
        this.repaint();
    }

    public Point2D toWorld(Point2D point) {
        java.awt.geom.Point2D.Double worldPoint = new java.awt.geom.Point2D.Double();
        this.transformation.transform(point, worldPoint);
        return worldPoint;
    }

    public Point2D toUser(Point2D point) {
        java.awt.geom.Point2D.Double userPoint = new java.awt.geom.Point2D.Double();
        try {
            this.transformation.inverseTransform(point, userPoint);
        } catch (Exception e) {
            LOG.atError().setCause(e.getCause());
        }

        return userPoint;
    }

    public BufferedImage makeBuffer(int width, int height) {
        BufferedImage bufferedImage = new BufferedImage(width, height, 1);
        Graphics2D canvas = bufferedImage.createGraphics();
        canvas.setColor(Color.WHITE);
        canvas.fillRect(0, 0, width, height);
        this.render(canvas);
        canvas.dispose();
        return bufferedImage;
    }

    public Dimension getExtents() {
        Dimension extents = new Dimension();
        double width = 200.0D;
        double height = 200.0D;
        List<State> states = this.currentMachine.getStates();

        for (State state : states) {
            double sx = state.getLocation().getX();
            double sy = state.getLocation().getY();
            if (width < sx + Diagram.STATE_RADIUS + BORDER) {
                width = sx + Diagram.STATE_RADIUS + BORDER;
            }

            if (height < sy + Diagram.STATE_RADIUS + BORDER) {
                height = sy + Diagram.STATE_RADIUS + BORDER;
            }
        }

        List<Transition> transitions = this.currentMachine.getTransitions();
        BasicStroke stroke = new BasicStroke(2.0F);

        for (Transition transition : transitions) {
            Shape transitionCurve =
                    this.diagram.transitionCurve(
                            transition, this.currentMachine.stateFor(transition.getCurrentState()));
            Rectangle curveBounds = stroke.createStrokedShape(transitionCurve).getBounds();
            if (width < curveBounds.getX() + curveBounds.getWidth() + BORDER) {
                width = curveBounds.getX() + curveBounds.getWidth() + BORDER;
            }

            if (height < curveBounds.getY() + curveBounds.getHeight() + BORDER) {
                height = curveBounds.getY() + curveBounds.getHeight() + BORDER;
            }
        }

        extents.setSize(width + 2.0D * BORDER, height + 2.0D * BORDER);
        return extents;
    }

    public void exportToJPEG(File file) {
        this.setSelectedState(null);
        this.setSelectedTransition(null);
        Dimension extents = this.getExtents();
        BufferedImage image = this.makeBuffer((int) extents.getWidth(), (int) extents.getHeight());
        Persistence.saveJPEG(image, file.toString());
    }

    static {
        SELECTED_TRANSITION_COLOUR = Color.BLACK;
    }

    public class MouseActionHandler extends MouseAdapter implements MouseMotionListener {
        public MouseActionHandler() {}

        public void mousePressed(MouseEvent event) {
            DiagramEditor.this.tool.mousePressed(event);
        }

        public void mouseReleased(MouseEvent event) {
            DiagramEditor.this.tool.mouseReleased(event);
        }

        public void mouseMoved(MouseEvent event) {
            DiagramEditor.this.tool.mouseMoved(event);
        }

        public void mouseDragged(MouseEvent event) {
            DiagramEditor.this.tool.mouseDragged(event);
        }
    }
}
