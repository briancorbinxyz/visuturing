package org.keiosu.visuturing.simulator.human;

import org.keiosu.visuturing.core.Configuration;
import org.keiosu.visuturing.core.Symbols;
import org.keiosu.visuturing.core.TuringMachine;
import org.keiosu.visuturing.simulator.AbstractSimulatorPanel;

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
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class HumanSimulatorPanel extends AbstractSimulatorPanel implements Runnable, ImageObserver {
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
  private ReentrantLock simulationLock = new ReentrantLock();

  public void refresh() {
    // do nothing
  }

  public void play() {
    if (!message.equals("")) {
      simulationLock.lock();
      try {
        reset();
        paused = false;
        runner = new Thread(this);
        runner.start();
      } finally {
        simulationLock.unlock();
      }
    } else if (runner != null) {
      simulationLock.lock();
      try {
        paused = false;
        stopped = false;
      } finally {
        simulationLock.unlock();
      }
    } else {
      paused = false;
      runner = new Thread(this);
      runner.start();
    }

    message = "";
  }

  public void stop() {
    pause();
    simulationLock.lock();
    try {
      stopped = true;
      runner = null;
      reset();
    } finally {
      simulationLock.unlock();
    }
  }

  public void pause() {
    simulationLock.lock();
    try {
      paused = true;
    } finally {
      simulationLock.unlock();
    }
  }

  private void reset() {
    fps = 24;
    speed = 1.0D;
    Point2D tapeLocation = new Double(300.0D, 115.0D);
    tape = new Tape(tapeLocation);
    currentState = Symbols.STATE_BEGINNING_STATE;
    config = new Configuration(currentState, inputWord, 0);
    Symbols.trimToHead(config);
    charWaiting = Symbols.LEFT_ARROW;
    message = "";
    lastPaused = false;
    initializingTape = false;
    inputIndex = 0;
    textOn = false;
    stopped = false;
    repaint();
  }

  public HumanSimulatorPanel(TuringMachine turingMachine) {
    super(turingMachine);
    setBackground(Color.WHITE);
    reset();
    Toolkit var2 = Toolkit.getDefaultToolkit();
    stateImage = var2.getImage(pathToURL("bitmaps/simulator/human/state.gif"));
    instrImage = var2.getImage(pathToURL("bitmaps/simulator/human/instr.gif"));
    titleImage = var2.getImage(pathToURL("bitmaps/simulator/human/title.gif"));
    hand = new Hand(pathToURL("bitmaps/simulator/hand.gif"), new Double(0.0D, 16.0D), new Double(450.0D, 400.0D), this);
    runner = null;
  }

  public void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);
    Graphics2D graphics2D = (Graphics2D)graphics;
    tape.draw(graphics2D);
    hand.draw(graphics2D);
    graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    graphics2D.drawImage(stateImage, 15, 15, this);
    graphics2D.drawImage(instrImage, 15, 420, this);
    graphics2D.drawImage(titleImage, 260, 30, this);
    graphics2D.setFont(new Font("serif", Font.PLAIN, 30));
    graphics2D.setColor(Color.BLACK);
    graphics2D.drawString(currentState, 80, 100);
    graphics2D.setColor(new Color(255, 204, 0));
    graphics2D.drawString(currentState, 81, 99);
    graphics2D.setColor(Color.BLACK);
    graphics2D.drawString(message, 20, 340);
    graphics2D.setColor(new Color(255, 204, 0));
    graphics2D.drawString(message, 21, 339);
  }

  private URL pathToURL(String path) {
    return getClass().getClassLoader().getResource(path);
  }

  public void run() {
    config = new Configuration(currentState, inputWord, 0);
    Symbols.trimToHead(config);
    Thread currentThread = Thread.currentThread();
    hand.moveTo(
      (int)tape.getPosition().getX() + tape.getCellWidth() + 2,
      (int)tape.getPosition().getY() + 2,
      25
    );
    if (config.getWord().length() > 0) {
      initializingTape = true;
    }

    while(true) {
      try {
        synchronized(currentThread) {
          while(true) {
            if (!paused) {
              if (stopped) {
                return;
              }
              break;
            }

            currentThread.wait();
          }
        }

        if (hand.finishedAnimation()) {
          if (charWaiting != Symbols.LEFT_ARROW && charWaiting != Symbols.LEFT_END_MARKER) {
            tape.writeSymbol(charWaiting, tape.findCellAtPoint(hand.getLocation()));
            repaint();
            charWaiting = Symbols.LEFT_ARROW;
          }

          nextAction();
        }

        repaint();
        Thread.sleep((long)((1000.0 / (double)fps) / speed));
      } catch (InterruptedException ie) {
        Thread.currentThread().interrupt();
      }
    }
  }

  private void nextAction() {
    if (initializingTape) {
      if (inputIndex < config.getWord().length()) {
        if (!textOn) {
          hand.squiggle(8, tape.getCellBounds());
          charWaiting = inputWord.charAt(inputIndex++);
        } else if (tape.findCellAtPoint(hand.getLocation()) < tape.visibleCells() / 2) {
          hand.moveTo((int)hand.getLocation().getX() + tape.getCellWidth(), (int)hand.getLocation().getY(), 5);
        } else {
          tape.forward();
        }

        textOn = !textOn;
      } else {
        tape.reset();
        hand.moveTo((int)tape.getPosition().getX() + tape.getCellWidth() + 1, (int)tape.getPosition().getY() + 1, 20);
        initializingTape = false;
      }

    } else {
      if (config.getState().equals(Symbols.STATE_HALTING_STATE)) {
        message = "[halted]";
        pause();
        repaint();
      }

      if (!lastPaused) {
        hand.pause(10);
        lastPaused = true;
      } else {
        lastPaused = false;
        List var1 = machine.getNextConfig(config);
        Configuration var2 = null;
        if (var1.size() > 0) {
          Random var3 = new Random();
          var2 = (Configuration)var1.get(var3.nextInt(var1.size()));
        }

        if (var2 != null) {
          if (var2.getIndex() < config.getIndex()) {
            if (tape.findCellAtPoint(hand.getLocation()) <= tape.visibleCells() / 2) {
              hand.moveTo((int)hand.getLocation().getX() - tape.getCellWidth(), (int)hand.getLocation().getY(), 5);
            } else {
              tape.reverse();
            }
          } else if (var2.getIndex() > config.getIndex()) {
            if (tape.findCellAtPoint(hand.getLocation()) < tape.visibleCells() / 2) {
              hand.moveTo((int)hand.getLocation().getX() + tape.getCellWidth(), (int)hand.getLocation().getY(), 5);
            } else {
              tape.forward();
            }
          } else {
            charWaiting = var2.getWord().charAt(var2.getIndex());
            if (charWaiting != Symbols.LEFT_END_MARKER) {
              hand.squiggle(10, tape.getCellBounds());
            }
          }

          config = var2;
        } else {
          message = "[stuck]";
          pause();
        }

        currentState = config.getState();
      }
    }
  }
}
