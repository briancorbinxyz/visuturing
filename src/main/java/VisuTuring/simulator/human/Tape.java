package VisuTuring.simulator.human;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.HashMap;

public class Tape {
  public static final int NO_ANIMATION = -1;
  public static final Color TAPE_COLOUR = new Color(255, 255, 204);
  private Point2D location;
  private Font tapeFont;
  private HashMap tapeContents;
  private Rectangle cell;
  private int visibleCells;
  private int firstVisibleCell;

  public Tape() {
    this.location = new Double(0.0D, 0.0D);
    this.moveTo(this.location);
    this.tapeFont = new Font("monospaced", 0, 17);
    this.tapeContents = new HashMap();
    this.cell = new Rectangle(20, 22);
    this.visibleCells = 20;
    this.firstVisibleCell = -1;
  }

  public Tape(Point2D var1) {
    this();
    this.moveTo(var1);
  }

  public void moveTo(Point2D var1) {
    this.location.setLocation(var1.getX(), var1.getY());
  }

  public void moveTo(int var1, int var2) {
    this.location.setLocation((double)var1, (double)var2);
  }

  public void draw(Graphics2D var1) {
    var1.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    var1.setFont(this.tapeFont);
    double var2 = this.location.getX();
    double var4 = this.location.getY();
    String var6 = null;

    double var7;
    double var9;
    int var11;
    for(var11 = this.firstVisibleCell; var11 < this.firstVisibleCell + this.visibleCells; ++var11) {
      var1.setColor(TAPE_COLOUR);
      var1.fill(new Rectangle((int)var2, (int)(var4 - this.cell.getBounds().getHeight()), (int)this.cell.getBounds().getWidth(), (int)this.cell.getBounds().getHeight()));
      var1.setColor(Color.BLACK);
      var1.draw(new Rectangle((int)var2, (int)(var4 - this.cell.getBounds().getHeight()), (int)this.cell.getBounds().getWidth(), (int)this.cell.getBounds().getHeight()));
      var6 = (String)this.tapeContents.get(new Integer(var11));
      var7 = var2 + (double)((this.getCellWidth() - var1.getFontMetrics().charWidth('⊔')) / 2);
      var9 = var4 - (this.cell.getHeight() - (double)var1.getFontMetrics().getAscent()) / 2.0D;
      if (var11 == -1) {
        var1.drawString(String.valueOf('⊳'), (float)var7, (float)var9);
      } else if (var6 == null) {
        var1.drawString(String.valueOf('⊔'), (float)var7, (float)var9);
      } else {
        var1.drawString(var6, (float)var7, (float)var9);
      }

      var2 += this.cell.getBounds().getWidth();
    }

    for(var11 = 1; var11 < 6; ++var11) {
      var7 = var2 + (double)((this.getCellWidth() - var1.getFontMetrics().charWidth('⊔')) / 2);
      var9 = var4 - (this.cell.getHeight() - (double)var1.getFontMetrics().getAscent()) / 2.0D;
      var1.setColor(new Color(TAPE_COLOUR.getRed(), TAPE_COLOUR.getGreen(), TAPE_COLOUR.getBlue(), 255 / var11));
      var1.fill(new Rectangle((int)var2, (int)(var4 - this.cell.getBounds().getHeight()), (int)this.cell.getBounds().getWidth(), (int)this.cell.getBounds().getHeight()));
      var1.setColor(new Color(255 / (6 - var11), 255 / (6 - var11), 255 / (6 - var11)));
      var1.draw(new Rectangle((int)var2, (int)(var4 - this.cell.getBounds().getHeight()), (int)this.cell.getBounds().getWidth(), (int)this.cell.getBounds().getHeight()));
      var1.drawString(String.valueOf('⊔'), (float)var7, (float)var9);
      var2 += this.cell.getBounds().getWidth();
    }

    var2 -= 5.0D * this.cell.getBounds().getWidth();
    this.drawInfinite(var2, var4, var1);
    if (this.firstVisibleCell > -1) {
      this.drawInfinite(this.getPosition().getX(), this.getPosition().getY(), var1);
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
