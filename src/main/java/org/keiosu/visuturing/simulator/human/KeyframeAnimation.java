package org.keiosu.visuturing.simulator.human;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

class KeyframeAnimation {
  private Point2D[] keyframePos;
  private int[] keyframes;
  private int totalFrames;
  private int currentFrame;
  private int currentIndex;
  private boolean finished;

  KeyframeAnimation(Point2D[] keyframePosition, int[] keyframes) {
    this.keyframePos = keyframePosition;
    this.keyframes = keyframes;
    this.finished = keyframes.length < 2;
    this.currentFrame = 0;
    this.currentIndex = 1;
    this.totalFrames = keyframes[this.currentIndex] - keyframes[this.currentIndex - 1];
  }

  Point2D position() {
    if (!this.finished) {
      ++this.currentFrame;
      double xPrev = this.keyframePos[this.currentIndex - 1].getX();
      double yPrev = this.keyframePos[this.currentIndex - 1].getY();
      double xCurr = this.keyframePos[this.currentIndex].getX();
      double yCurr = this.keyframePos[this.currentIndex].getY();
      int newX =
          (int) (xPrev + (xCurr - xPrev) / (double) this.totalFrames * (double) this.currentFrame);
      int newY =
          (int) (yPrev + (yCurr - yPrev) / (double) this.totalFrames * (double) this.currentFrame);
      if (this.currentFrame >= this.totalFrames) {
        ++this.currentIndex;
        this.currentFrame = 0;
        if (this.currentIndex == this.keyframes.length) {
          this.finished = true;
          this.totalFrames = 0;
        } else {
          this.totalFrames =
              this.keyframes[this.currentIndex] - this.keyframes[this.currentIndex - 1];
        }
      }

      return new Double((double) newX, (double) newY);
    } else {
      return this.keyframePos[this.keyframes.length - 1];
    }
  }

  boolean isFinished() {
    return this.finished;
  }
}
