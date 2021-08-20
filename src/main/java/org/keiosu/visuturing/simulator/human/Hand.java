package org.keiosu.visuturing.simulator.human;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.ImageObserver;
import java.net.URL;

public class Hand {
    private Image image;
    private Point2D location;
    private Point2D hotSpot;
    private ImageObserver observer;
    private KeyframeAnimation animation;

    public Hand(
            URL imageUrl,
            Point2D pointerOrigin,
            Point2D handLocation,
            ImageObserver imageObserver) {
        this.image = Toolkit.getDefaultToolkit().getImage(imageUrl);
        this.hotSpot = pointerOrigin;
        this.location = new Double(0.0D, 0.0D);
        this.moveTo(handLocation);
        this.observer = imageObserver;
        this.animation = null;
    }

    private void moveTo(Point2D location) {
        this.location.setLocation(
                location.getX() - this.hotSpot.getX(), location.getY() - this.hotSpot.getY());
    }

    public Point2D getLocation() {
        return new Double(
                this.location.getX() + this.hotSpot.getX(),
                this.location.getY() + this.hotSpot.getY());
    }

    void moveTo(int keyframes, double x, double y) {
        Double startLocation =
                new Double(
                        this.location.getX() + this.hotSpot.getX(),
                        this.location.getY() + this.hotSpot.getY());
        Double finalLocation = new Double(x, y);
        Point2D[] path = new Point2D[] {startLocation, finalLocation};
        int[] frames = new int[] {0, keyframes};
        this.animation = new KeyframeAnimation(path, frames);
    }

    void pause() {
        moveTo(
                10,
                this.location.getX() + this.hotSpot.getX(),
                this.location.getY() + this.hotSpot.getY());
    }

    void squiggle(int noKeyframes, Rectangle cell) {
        Double step1 =
                new Double(
                        this.location.getX() + this.hotSpot.getX(),
                        this.location.getY() + this.hotSpot.getY() - cell.getHeight());
        Double step2 = new Double(step1.getX() + cell.getWidth(), step1.getY());
        Double step3 = new Double(step1.getX(), step1.getY() + cell.getHeight());
        Double step4 = new Double(step1.getX() + cell.getWidth(), step1.getY() + cell.getHeight());
        Double step5 =
                new Double(
                        this.location.getX() + this.hotSpot.getX(),
                        this.location.getY() + this.hotSpot.getY());
        Point2D[] path = new Point2D[] {step1, step2, step3, step4, step5};
        int[] frames =
                new int[] {
                    0, noKeyframes / 4, noKeyframes / 4 * 2, noKeyframes / 4 * 3, noKeyframes
                };
        this.animation = new KeyframeAnimation(path, frames);
    }

    public void draw(Graphics2D graphics2D) {
        this.update();
        graphics2D.drawImage(
                this.image, (int) this.location.getX(), (int) this.location.getY(), this.observer);
    }

    private void update() {
        if (this.animation != null && !this.animation.isFinished()) {
            this.moveTo(this.animation.position());
        }
    }

    boolean finishedAnimation() {
        return this.animation == null || this.animation.isFinished();
    }
}
