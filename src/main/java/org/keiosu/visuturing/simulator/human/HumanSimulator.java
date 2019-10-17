package org.keiosu.visuturing.simulator.human;

import org.keiosu.visuturing.core.Configuration;
import org.keiosu.visuturing.core.Symbols;
import org.keiosu.visuturing.core.TuringMachine;
import org.keiosu.visuturing.simulator.Simulator;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.ImageObserver;
import java.net.URL;
import java.util.Random;
import java.util.Vector;

public class HumanSimulator extends Simulator implements Runnable, ImageObserver {
  private Point2D tapeLocation;
  private Hand hand;
  private Tape tape;
  private int fps;
  private Thread runner;
  private boolean paused;
  private boolean stopped;
  private Configuration config;
  private String currentState;
  private char charWaiting;
  private Image stateImage;
  private Image titleImage;
  private Image instrImage;
  private String message;
  private boolean lastPaused;
  private boolean initializingTape;
  private boolean textOn;
  private int inputIndex;

  public void refresh() {
  }

  public void play() {
    if (!this.message.equals("")) {
      synchronized(this.runner) {
        this.reset();
        this.paused = false;
        this.runner = new Thread(this);
        this.runner.start();
      }
    } else if (this.runner != null) {
      synchronized(this.runner) {
        this.paused = false;
        this.stopped = false;
        this.runner.notify();
      }
    } else {
      this.paused = false;
      this.runner = new Thread(this);
      this.runner.start();
    }

    this.message = "";
  }

  public void stop() {
    this.pause();
    synchronized(this.runner) {
      this.stopped = true;
    }

    this.runner = null;
    this.reset();
  }

  public void pause() {
    synchronized(this.runner) {
      this.paused = true;
    }
  }

  public void reset() {
    this.fps = 24;
    this.speed = 1.0D;
    this.tapeLocation = new Double(300.0D, 115.0D);
    this.tape = new Tape(this.tapeLocation);
    this.currentState = "s";
    this.config = new Configuration(this.currentState, this.inputWord, 0);
    Symbols.trimToHead(this.config);
    this.charWaiting = 8592;
    this.message = "";
    this.lastPaused = false;
    this.initializingTape = false;
    this.inputIndex = 0;
    this.textOn = false;
    this.stopped = false;
    this.repaint();
  }

  public HumanSimulator(TuringMachine var1) {
    this.machine = var1;
    this.setBackground(Color.WHITE);
    this.reset();
    Toolkit var2 = Toolkit.getDefaultToolkit();
    this.stateImage = var2.getImage(this.pathToURL("bitmaps/simulator/human/state.gif"));
    this.instrImage = var2.getImage(this.pathToURL("bitmaps/simulator/human/instr.gif"));
    this.titleImage = var2.getImage(this.pathToURL("bitmaps/simulator/human/title.gif"));
    this.hand = new Hand(this.pathToURL("bitmaps/simulator/hand.gif"), new Double(0.0D, 16.0D), new Double(450.0D, 400.0D), this);
    this.runner = null;
  }

  public void paintComponent(Graphics var1) {
    super.paintComponent(var1);
    Graphics2D var2 = (Graphics2D)var1;
    this.tape.draw(var2);
    this.hand.draw(var2);
    var2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    var2.drawImage(this.stateImage, 15, 15, this);
    var2.drawImage(this.instrImage, 15, 420, this);
    var2.drawImage(this.titleImage, 260, 30, this);
    var2.setFont(new Font("serif", 0, 30));
    var2.setColor(Color.BLACK);
    var2.drawString(this.currentState, 80, 100);
    var2.setColor(new Color(255, 204, 0));
    var2.drawString(this.currentState, 81, 99);
    var2.setColor(Color.BLACK);
    var2.drawString(this.message, 20, 340);
    var2.setColor(new Color(255, 204, 0));
    var2.drawString(this.message, 21, 339);
  }

  private URL pathToURL(String var1) {
    return this.getClass().getClassLoader().getResource(var1);
  }

  public void run() {
    this.config = new Configuration(this.currentState, this.inputWord, 0);
    Symbols.trimToHead(this.config);
    Thread var1 = Thread.currentThread();
    this.hand.moveTo((int)this.tape.getPosition().getX() + this.tape.getCellWidth() + 2, (int)this.tape.getPosition().getY() + 2, 25);
    if (this.config.getWord().length() > 0) {
      this.initializingTape = true;
    }

    while(true) {
      try {
        synchronized(var1) {
          while(true) {
            if (!this.paused) {
              if (this.stopped) {
                return;
              }
              break;
            }

            var1.wait();
          }
        }

        if (this.hand.finishedAnimation()) {
          if (this.charWaiting != 8592 && this.charWaiting != 8883) {
            this.tape.write(this.charWaiting, this.tape.cellAtPoint(this.hand.getLocation()));
            this.repaint();
            this.charWaiting = 8592;
          }

          this.nextAction();
        }

        this.repaint();
        Thread.sleep((long)((double)(1000 / this.fps) / this.speed));
      } catch (InterruptedException var5) {
      }
    }
  }

  private void nextAction() {
    if (this.initializingTape) {
      if (this.inputIndex < this.config.getWord().length()) {
        if (!this.textOn) {
          this.hand.squiggle(8, this.tape.getCellBounds());
          this.charWaiting = this.inputWord.charAt(this.inputIndex++);
        } else if (this.tape.cellAtPoint(this.hand.getLocation()) < this.tape.visibleCells() / 2) {
          this.hand.moveTo((int)this.hand.getLocation().getX() + this.tape.getCellWidth(), (int)this.hand.getLocation().getY(), 5);
        } else {
          this.tape.forward();
        }

        this.textOn = !this.textOn;
      } else {
        this.tape.reset();
        this.hand.moveTo((int)this.tape.getPosition().getX() + this.tape.getCellWidth() + 1, (int)this.tape.getPosition().getY() + 1, 20);
        this.initializingTape = false;
      }

    } else {
      if (this.config.getState().equals("h")) {
        this.message = "[halted]";
        this.pause();
        this.repaint();
      }

      if (!this.lastPaused) {
        this.hand.pause(10);
        this.lastPaused = true;
      } else {
        this.lastPaused = false;
        Vector var1 = this.machine.getNextConfig(this.config);
        Configuration var2 = null;
        if (var1.size() > 0) {
          Random var3 = new Random();
          var2 = (Configuration)var1.get(var3.nextInt(var1.size()));
        }

        if (var2 != null) {
          if (var2.getIndex() < this.config.getIndex()) {
            if (this.tape.cellAtPoint(this.hand.getLocation()) <= this.tape.visibleCells() / 2) {
              this.hand.moveTo((int)this.hand.getLocation().getX() - this.tape.getCellWidth(), (int)this.hand.getLocation().getY(), 5);
            } else {
              this.tape.reverse();
            }
          } else if (var2.getIndex() > this.config.getIndex()) {
            if (this.tape.cellAtPoint(this.hand.getLocation()) < this.tape.visibleCells() / 2) {
              this.hand.moveTo((int)this.hand.getLocation().getX() + this.tape.getCellWidth(), (int)this.hand.getLocation().getY(), 5);
            } else {
              this.tape.forward();
            }
          } else {
            this.charWaiting = var2.getWord().charAt(var2.getIndex());
            if (this.charWaiting != 8883) {
              this.hand.squiggle(10, this.tape.getCellBounds());
            }
          }

          this.config = var2;
        } else {
          this.message = "[stuck]";
          this.pause();
        }

        this.currentState = this.config.getState();
      }
    }
  }
}
