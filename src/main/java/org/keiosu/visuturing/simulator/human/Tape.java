package org.keiosu.visuturing.simulator.human;

import org.keiosu.visuturing.core.Symbols;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.Map;
import java.util.TreeMap;

public class Tape {
  public static final int NO_ANIMATION = -1;
  public static final Color TAPE_COLOUR = new Color(255, 255, 204);
  private Point2D location;
  private Font tapeFont;
  private Map<Integer, String> tapeContents;
  private Rectangle cell;
  private int visibleCells;
  private int firstVisibleCell;

  public Tape() {
    this.location = new Double(0.0D, 0.0D);
    this.moveTo(this.location);
    this.tapeFont = new Font("monospaced", 0, 17);
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
    double locationX = this.location.getX();
    double locationY = this.location.getY();
    String var6;

    double var7;
    double var9;
    for(int currentCell = this.firstVisibleCell; currentCell < this.firstVisibleCell + this.visibleCells; ++currentCell) {
      canvas.setColor(TAPE_COLOUR);
      canvas.fill(new Rectangle((int)locationX, (int)(locationY - this.cell.getBounds().getHeight()), (int)this.cell.getBounds().getWidth(), (int)this.cell.getBounds().getHeight()));
      canvas.setColor(Color.BLACK);
      canvas.draw(new Rectangle((int)locationX, (int)(locationY - this.cell.getBounds().getHeight()), (int)this.cell.getBounds().getWidth(), (int)this.cell.getBounds().getHeight()));
      var6 = (String)this.tapeContents.get(currentCell);
      var7 = locationX + (double)((this.getCellWidth() - canvas.getFontMetrics().charWidth(Symbols.SPACE)) / 2);
      var9 = locationY - (this.cell.getHeight() - (double) canvas.getFontMetrics().getAscent()) / 2.0D;
      if (currentCell == -1) {
        canvas.drawString(String.valueOf(Symbols.LEFT_END_MARKER), (float)var7, (float)var9);
      } else if (var6 == null) {
        canvas.drawString(String.valueOf(Symbols.SPACE), (float)var7, (float)var9);
      } else {
        canvas.drawString(var6, (float)var7, (float)var9);
      }

      locationX += this.cell.getBounds().getWidth();
    }

    for(int var11 = 1; var11 < 6; ++var11) {
      var7 = locationX + (double)((this.getCellWidth() - canvas.getFontMetrics().charWidth(Symbols.SPACE)) / 2);
      var9 = locationY - (this.cell.getHeight() - (double) canvas.getFontMetrics().getAscent()) / 2.0D;
      canvas.setColor(new Color(TAPE_COLOUR.getRed(), TAPE_COLOUR.getGreen(), TAPE_COLOUR.getBlue(), 255 / var11));
      canvas.fill(new Rectangle((int)locationX, (int)(locationY - this.cell.getBounds().getHeight()), (int)this.cell.getBounds().getWidth(), (int)this.cell.getBounds().getHeight()));
      canvas.setColor(new Color(255 / (6 - var11), 255 / (6 - var11), 255 / (6 - var11)));
      canvas.draw(new Rectangle((int)locationX, (int)(locationY - this.cell.getBounds().getHeight()), (int)this.cell.getBounds().getWidth(), (int)this.cell.getBounds().getHeight()));
      canvas.drawString(String.valueOf(Symbols.SPACE), (float)var7, (float)var9);
      locationX += this.cell.getBounds().getWidth();
    }

    locationX -= 5.0D * this.cell.getBounds().getWidth();
    this.drawInfinite(locationX, locationY, canvas);
    if (this.firstVisibleCell > -1) {
      this.drawInfinite(this.getPosition().getX(), this.getPosition().getY(), canvas);
    }

  }

  private void drawInfinite(double var1, double var3, Graphics2D var5) {
    int var6 = this.getHeight();
    int var7 = this.getCellWidth();
    java.awt.geom.Line2D.Double var8 = new java.awt.geom.Line2D.Double(var1 - (double)(var7 / 2), var3 + (double)(var6 / 2), var1 + (double)(var7 / 2), var3 - (double)var6 - (double)(var6 / 2));
    var1 += this.cell.getBounds().getWidth();
    java.awt.geom.Line2D.Double var9 = new java.awt.geom.Line2D.Double(var1 - (double)(var7 / 2), var3 + (double)(var6 / 2), var1 + (double)(var7 / 2), var3 - (double)var6 - (double)(var6 / 2));
    Polygon var10 = new Polygon();
    var10.addPoint((int)var8.getX1(), (int)var8.getY1());
    var10.addPoint((int)var8.getX2(), (int)var8.getY2());
    var10.addPoint((int)var9.getX2(), (int)var9.getY2());
    var10.addPoint((int)var9.getX1(), (int)var9.getY1());
    var5.setColor(Color.white);
    var5.fill(var10);
    var5.setColor(Color.BLACK);
    var5.draw(var8);
    var5.draw(var9);
  }

  public boolean isRolled() {
    return this.firstVisibleCell != -1;
  }

  public void write(char var1, int var2) {
    Integer var3 = new Integer(var2);
    if (var2 > -1) {
      if (this.tapeContents.get(var3) != null) {
        this.tapeContents.remove(var3);
      }

      this.tapeContents.put(var3, new String(String.valueOf(var1)));
    }

  }

  public int cellAtPoint(Point2D var1) {
    int var2 = (int)((var1.getX() - this.location.getX()) / this.cell.getBounds().getWidth()) + this.firstVisibleCell;
    return var2;
  }

  public int getCellWidth() {
    return (int)this.cell.getBounds().getWidth();
  }

  public int getHeight() {
    return (int)this.cell.getBounds().getHeight();
  }

  public Rectangle getCellBounds() {
    return this.cell.getBounds();
  }

  public Point2D getPosition() {
    return this.location;
  }

  public int visibleCells() {
    return this.visibleCells;
  }

  public void forward() {
    ++this.firstVisibleCell;
  }

  public void reverse() {
    if (this.firstVisibleCell > -1) {
      --this.firstVisibleCell;
    }

  }

  public void reset() {
    this.firstVisibleCell = -1;
  }

  public String symbolAt(int var1) {
    String var2 = null;
    if (this.tapeContents.get(new Integer(var1)) != null) {
      var2 = (String)this.tapeContents.get(new Integer(var1));
    }

    return var2;
  }
}
