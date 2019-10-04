package VisuTuring.simulator;

import VisuTuring.core.TuringMachine;
import javax.swing.JPanel;

public abstract class Simulator extends JPanel {
  private static final double MAX_SPEED = 12.0D;
  private static final double MIN_SPEED = 0.1D;
  protected String inputWord;
  protected double speed;
  protected TuringMachine machine;

  public Simulator() {
  }

  public abstract void play();

  public abstract void stop();

  public abstract void pause();

  public void increaseSpeed() {
    this.speed = this.speed < 12.0D ? this.speed + 0.5D : 12.0D;
  }

  public void decreaseSpeed() {
    this.speed = this.speed > 0.1D ? this.speed - 0.5D : 0.1D;
  }

  public void setInputWord(String var1) {
    this.inputWord = var1;
  }

  public TuringMachine getMachine() {
    return this.machine;
  }

  public abstract void refresh();
}
