package org.keiosu.visuturing.simulator;

import org.keiosu.visuturing.core.TuringMachine;

import javax.swing.JPanel;

public abstract class AbstractSimulatorPanel extends JPanel {

  private static final double MAX_SPEED = 12.0D;
  private static final double MIN_SPEED = 0.1D;
  public static final double SPEED_DELTA = 0.5D;

  protected String inputWord;
  protected double speed;

  protected TuringMachine machine;

  public AbstractSimulatorPanel(TuringMachine machine) {
    this.machine = machine;
  }

  public abstract void play();

  public abstract void stop();

  public abstract void pause();

  public void increaseSpeed() {
    this.speed = this.speed < MAX_SPEED ? this.speed + 0.5D : MAX_SPEED;
  }

  public void decreaseSpeed() {
    this.speed = this.speed > MIN_SPEED ? this.speed - SPEED_DELTA : MIN_SPEED;
  }

  public void setInputWord(String inputWord) {
    this.inputWord = inputWord;
  }

  public TuringMachine getMachine() {
    return this.machine;
  }

  public abstract void refresh();
}
