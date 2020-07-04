package org.keiosu.visuturing.gui;

import org.keiosu.visuturing.core.TuringMachine;
import org.keiosu.visuturing.gui.panels.DiagramPanel;
import org.keiosu.visuturing.gui.panels.MainSimulationPanel;
import org.keiosu.visuturing.simulator.diagram.DiagramSimulatorPanel;
import org.keiosu.visuturing.simulator.human.HumanSimulatorPanel;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameListener;

public class VTFrame extends JInternalFrame implements ChangeListener {
  public static final String UNSAVED_FILENAME = "-untitled-";
  public static final Dimension DEFAULT_FRAME_SIZE = new Dimension(640, 480);
  public static final int DESCRIPTION_TAB = 0;
  public static final int DIAGRAM_TAB = 1;
  public static final int SIMULATION_TAB = 2;
  public static final int HUMAN_TAB = 3;
  String fileName;
  TuringMachine machine;
  ActionListener listener;
  DiagramPanel diagramEditor;
  MainSimulationPanel simulator;
  MainSimulationPanel humanSimulator;
  JTabbedPane tp;
  DescriptionPanel descPanel;

  public VTFrame(InternalFrameListener frameListener, ActionListener actionListener, TuringMachine turingMachine, int var4) {
    super(turingMachine.getName(), true, true, true, true);
    this.listener = actionListener;
    this.machine = turingMachine;
    turingMachine.setChanged(false);
    this.fileName = UNSAVED_FILENAME;
    this.setSize(DEFAULT_FRAME_SIZE);
    this.setDefaultCloseOperation(0);
    this.setFrameIcon(this.createImageIcon("bitmaps/vticons.jpg"));
    this.descPanel = new DescriptionPanel(turingMachine);
    this.diagramEditor = new DiagramPanel(actionListener, turingMachine);
    this.simulator = new MainSimulationPanel(actionListener, new DiagramSimulatorPanel(turingMachine));
    this.humanSimulator = new MainSimulationPanel(actionListener, new HumanSimulatorPanel(turingMachine));
    Container var5 = this.getContentPane();
    var5.setLayout(new BorderLayout());
    this.tp = new JTabbedPane(1, 1);
    this.tp.addTab("Description", this.descPanel);
    this.tp.addTab("Diagram Editor", this.diagramEditor);
    this.tp.addTab("State Simulator", this.simulator);
    this.tp.addTab("Human Simulator", this.humanSimulator);
    this.tp.setSelectedIndex(var4);
    this.tp.addChangeListener(this);
    var5.add(this.tp, "Center");
    this.addInternalFrameListener(frameListener);
  }

  ImageIcon createImageIcon(String var1) {
    return new ImageIcon(this.getClass().getClassLoader().getResource(var1));
  }

  public void setFileName(String var1) {
    this.fileName = var1;
  }

  public String getFileName() {
    return this.fileName;
  }

  public TuringMachine getTuringMachine() {
    return this.machine;
  }

  public void displayDiagram() {
    this.tp.setSelectedIndex(1);
  }

  public void displaySimulation() {
    this.tp.setSelectedIndex(2);
  }

  public void displayHuman() {
    this.tp.setSelectedIndex(3);
  }

  public void stateChanged(ChangeEvent var1) {
    this.simulator.refresh();
  }

  public void exportToJPEG(File var1) {
    this.diagramEditor.exportToJPEG(var1);
  }

  public void printDiagram() {
    this.diagramEditor.printDiagram();
  }
}
