package org.keiosu.visuturing.simulator.human;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

public class KeyframeAnimation {
  private Point2D[] keyframePos;
  private int[] keyframes;
  private int totalFrames;
  private int currentFrame;
  private int currentIndex;
  private boolean finished;

  public KeyframeAnimation(Point2D[] var1, int[] var2) {
    this.keyframePos = var1;
    this.keyframes = var2;
    this.finished = false;
    if (var2.length < 2) {
      this.finished = true;
    }

    this.currentFrame = 0;
    this.currentIndex = 1;
    this.totalFrames = var2[this.currentIndex] - var2[this.currentIndex - 1];
  }

  public Point2D position() {
    if (!this.finished) {
      ++this.currentFrame;
      double var3 = this.keyframePos[this.currentIndex - 1].getX();
      double var5 = this.keyframePos[this.currentIndex - 1].getY();
      double var7 = this.keyframePos[this.currentIndex].getX();
      double var9 = this.keyframePos[this.currentIndex].getY();
      int var1 = (int)(var3 + (var7 - var3) / (double)this.totalFrames * (double)this.currentFrame);
      int var2 = (int)(var5 + (var9 - var5) / (double)this.totalFrames * (double)this.currentFrame);
      if (this.currentFrame >= this.totalFrames) {
        ++this.currentIndex;
        this.currentFrame = 0;
        if (this.currentIndex == this.keyframes.length) {
          this.finished = true;
          this.totalFrames = 0;
        } else {
          this.totalFrames = this.keyframes[this.currentIndex] - this.keyframes[this.currentIndex - 1];
        }
      }

      return new Double((double)var1, (double)var2);
    } else {
      return this.keyframePos[this.keyframes.length - 1];
    }
  }

  boolean isFinished() {
    return this.finished;
  }
}
