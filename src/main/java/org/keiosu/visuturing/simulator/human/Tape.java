package org.keiosu.visuturing.simulator.human;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import org.keiosu.visuturing.core.Symbols;

public class Tape {
    private static final Color TAPE_COLOUR = new Color(255, 255, 204);
    private Point2D location;
    private Font tapeFont;
    private Map<Integer, String> tapeContents;
    private Rectangle cell;
    private int visibleCells;
    private int firstVisibleCell;

    public Tape() {
        this.location = new Double(0.0D, 0.0D);
        this.moveTo(this.location);
        this.tapeFont = new Font("monospaced", Font.PLAIN, 17);
        this.tapeContents = new TreeMap<>();
        this.cell = new Rectangle(20, 22);
        this.visibleCells = 20;
        this.firstVisibleCell = -1;
    }

    public Tape(Point2D location) {
        this();
        this.moveTo(location);
    }

    private void moveTo(Point2D location) {
        this.location.setLocation(location.getX(), location.getY());
    }

    public void draw(Graphics2D canvas) {
        canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        canvas.setFont(this.tapeFont);
        double offsetX = this.location.getX();
        double offsetY = this.location.getY();
        offsetX = drawVisibleTapeCellsAndOffset(canvas, offsetX, offsetY);
        offsetX = drawVisibleTapeSpacesAndOffset(canvas, offsetX, offsetY);
        offsetX -= 5.0D * this.cell.getBounds().getWidth();
        this.drawInfinite(offsetX, offsetY, canvas);
        if (this.firstVisibleCell > -1) {
            this.drawInfinite(this.getPosition().getX(), this.getPosition().getY(), canvas);
        }
    }

    private double drawVisibleTapeSpacesAndOffset(
            Graphics2D canvas, double offsetX, double offsetY) {
        for (int i = 1; i < 6; ++i) {
            canvas.setColor(
                    new Color(
                            TAPE_COLOUR.getRed(),
                            TAPE_COLOUR.getGreen(),
                            TAPE_COLOUR.getBlue(),
                            255 / i));
            canvas.fill(
                    new Rectangle(
                            (int) offsetX,
                            (int) (offsetY - this.cell.getBounds().getHeight()),
                            (int) this.cell.getBounds().getWidth(),
                            (int) this.cell.getBounds().getHeight()));
            canvas.setColor(new Color(255 / (6 - i), 255 / (6 - i), 255 / (6 - i)));
            canvas.draw(
                    new Rectangle(
                            (int) offsetX,
                            (int) (offsetY - this.cell.getBounds().getHeight()),
                            (int) this.cell.getBounds().getWidth(),
                            (int) this.cell.getBounds().getHeight()));
            canvas.drawString(
                    String.valueOf(Symbols.SPACE),
                    (float)
                            (offsetX
                                    + (this.getCellWidth()
                                                    - canvas.getFontMetrics()
                                                            .charWidth(Symbols.SPACE))
                                            / 2.0D),
                    (float)
                            (offsetY
                                    - (this.cell.getHeight()
                                                    - (double) canvas.getFontMetrics().getAscent())
                                            / 2.0D));
            offsetX += this.cell.getBounds().getWidth();
        }
        return offsetX;
    }

    private double drawVisibleTapeCellsAndOffset(
            Graphics2D canvas, double locationX, double locationY) {
        for (int currentCell = this.firstVisibleCell;
                currentCell < this.firstVisibleCell + this.visibleCells;
                ++currentCell) {
            canvas.setColor(TAPE_COLOUR);
            drawTapeCell(canvas, (int) locationX, locationY);
            String symbol = this.tapeContents.get(currentCell);
            double symbolX =
                    locationX
                            + (this.getCellWidth()
                                            - canvas.getFontMetrics().charWidth(Symbols.SPACE))
                                    / 2.0D;
            double symbolY =
                    locationY
                            - (this.cell.getHeight() - (double) canvas.getFontMetrics().getAscent())
                                    / 2.0D;
            if (currentCell == -1) {
                canvas.drawString(
                        String.valueOf(Symbols.LEFT_END_MARKER), (float) symbolX, (float) symbolY);
            } else {
                canvas.drawString(
                        Objects.requireNonNullElseGet(symbol, () -> String.valueOf(Symbols.SPACE)),
                        (float) symbolX,
                        (float) symbolY);
            }
            locationX += this.cell.getBounds().getWidth();
        }
        return locationX;
    }

    private void drawTapeCell(Graphics2D canvas, int locationX, double locationY) {
        canvas.fill(
                new Rectangle(
                        locationX,
                        (int) (locationY - this.cell.getBounds().getHeight()),
                        (int) this.cell.getBounds().getWidth(),
                        (int) this.cell.getBounds().getHeight()));
        canvas.setColor(Color.BLACK);
        canvas.draw(
                new Rectangle(
                        locationX,
                        (int) (locationY - this.cell.getBounds().getHeight()),
                        (int) this.cell.getBounds().getWidth(),
                        (int) this.cell.getBounds().getHeight()));
    }

    private void drawInfinite(double originX, double originY, Graphics2D canvas) {
        int height = this.getHeight();
        int width = this.getCellWidth();
        var leftLine =
                new java.awt.geom.Line2D.Double(
                        originX - (width / 2.0d),
                        originY + (height / 2.0d),
                        originX + (width / 2.0d),
                        originY - (double) height - (height / 2.0d));
        originX += this.cell.getBounds().getWidth();
        var rightLine =
                new java.awt.geom.Line2D.Double(
                        originX - (width / 2.0d),
                        originY + (height / 2.0d),
                        originX + (width / 2.0d),
                        originY - (double) height - (height / 2.0d));
        Polygon gap = new Polygon();
        gap.addPoint((int) leftLine.getX1(), (int) leftLine.getY1());
        gap.addPoint((int) leftLine.getX2(), (int) leftLine.getY2());
        gap.addPoint((int) rightLine.getX2(), (int) rightLine.getY2());
        gap.addPoint((int) rightLine.getX1(), (int) rightLine.getY1());
        canvas.setColor(Color.white);
        canvas.fill(gap);
        canvas.setColor(Color.BLACK);
        canvas.draw(leftLine);
        canvas.draw(rightLine);
    }

    void writeSymbol(char symbol, int cellNumber) {
        if (cellNumber > -1) {
            if (tapeContents.get(cellNumber) != null) {
                tapeContents.remove(cellNumber);
            }
            tapeContents.put(cellNumber, String.valueOf(symbol));
        }
    }

    int findCellAtPoint(Point2D point) {
        return (int) ((point.getX() - this.location.getX()) / this.cell.getBounds().getWidth())
                + this.firstVisibleCell;
    }

    int getCellWidth() {
        return (int) this.cell.getBounds().getWidth();
    }

    public int getHeight() {
        return (int) this.cell.getBounds().getHeight();
    }

    Rectangle getCellBounds() {
        return this.cell.getBounds();
    }

    Point2D getPosition() {
        return this.location;
    }

    int visibleCells() {
        return this.visibleCells;
    }

    void forward() {
        ++this.firstVisibleCell;
    }

    void reverse() {
        if (this.firstVisibleCell > -1) {
            --this.firstVisibleCell;
        }
    }

    void reset() {
        this.firstVisibleCell = -1;
    }
}
