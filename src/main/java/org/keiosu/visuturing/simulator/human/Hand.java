package org.keiosu.visuturing.simulator.human;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.ImageObserver;
import java.net.URL;

public class Hand {
  public static final int NO_ANIMATION = -1;
  private Image hand;
  private Point2D location;
  private Point2D hotSpot;
  private ImageObserver observer;
  private Font tapeFont;
  private KeyframeAnimation anim;

  public Hand(URL var1, Point2D var2, Point2D var3, ImageObserver var4) {
    Toolkit var5 = Toolkit.getDefaultToolkit();
    this.hand = var5.getImage(var1);
    this.hotSpot = var2;
    this.location = new Double(0.0D, 0.0D);
    this.moveTo(var3);
    this.observer = var4;
    this.tapeFont = new Font("Helvetica", 0, 20);
    this.anim = null;
  }

  public void moveTo(Point2D var1) {
    this.location.setLocation(var1.getX() - this.hotSpot.getX(), var1.getY() - this.hotSpot.getY());
  }

  public void moveTo(int var1, int var2) {
    this.location.setLocation((double)var1 - this.hotSpot.getX(), (double)var2 - this.hotSpot.getY());
  }

  public Point2D getLocation() {
    return new Double(this.location.getX() + this.hotSpot.getX(), this.location.getY() + this.hotSpot.getY());
  }

  public void moveTo(int var1, int var2, int var3) {
    Double var4 = new Double(this.location.getX() + this.hotSpot.getX(), this.location.getY() + this.hotSpot.getY());
    Double var5 = new Double((double)var1, (double)var2);
    Point2D[] var6 = new Point2D[]{var4, var5};
    int[] var7 = new int[]{0, var3};
    this.anim = new KeyframeAnimation(var6, var7);
  }

  public void moveTo(Point2D var1, int var2) {
    this.moveTo((int)var1.getX(), (int)var1.getY(), var2);
  }

  public void pause(int var1) {
    Double var2 = new Double(this.location.getX() + this.hotSpot.getX(), this.location.getY() + this.hotSpot.getY());
    Double var3 = new Double(this.location.getX() + this.hotSpot.getX(), this.location.getY() + this.hotSpot.getY());
    Point2D[] var4 = new Point2D[]{var2, var3};
    int[] var5 = new int[]{0, var1};
    this.anim = new KeyframeAnimation(var4, var5);
  }

  public void squiggle(int var1) {
    Double var2 = new Double(this.location.getX() + this.hotSpot.getX(), this.location.getY() + this.hotSpot.getY());
    Double var3 = new Double(var2.getX() + 10.0D, var2.getY());
    Double var4 = new Double(var2.getX(), var2.getY() + 10.0D);
    Double var5 = new Double(var2.getX() + 10.0D, var2.getY() + 10.0D);
    Point2D[] var6 = new Point2D[]{var2, var3, var4, var5};
    int[] var7 = new int[]{0, var1 / 3 * 1, var1 / 3 * 2, var1};
    this.anim = new KeyframeAnimation(var6, var7);
  }

  public void squiggle(int var1, Rectangle var2) {
    Double var3 = new Double(this.location.getX() + this.hotSpot.getX(), this.location.getY() + this.hotSpot.getY() - var2.getHeight());
    Double var4 = new Double(var3.getX() + var2.getWidth(), var3.getY());
    Double var5 = new Double(var3.getX(), var3.getY() + var2.getHeight());
    Double var6 = new Double(var3.getX() + var2.getWidth(), var3.getY() + var2.getHeight());
    Double var7 = new Double(this.location.getX() + this.hotSpot.getX(), this.location.getY() + this.hotSpot.getY());
    Point2D[] var8 = new Point2D[]{var3, var4, var5, var6, var7};
    int[] var9 = new int[]{0, var1 / 4 * 1, var1 / 4 * 2, var1 / 4 * 3, var1};
    this.anim = new KeyframeAnimation(var8, var9);
  }

  public void draw(Graphics2D var1) {
    this.update();
    var1.drawImage(this.hand, (int)this.location.getX(), (int)this.location.getY(), this.observer);
  }

  public void update() {
    if (this.anim != null && !this.anim.isFinished()) {
      Point2D var1 = this.anim.position();
      this.moveTo(var1);
    }

  }

  public boolean finishedAnimation() {
    return this.anim == null ? true : this.anim.isFinished();
  }
}
