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
import java.util.Objects;
import javax.swing.*;
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
    this.tp = new JTabbedPane(SwingConstants.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
    this.tp.addTab("Description", this.descPanel);
    this.tp.addTab("Diagram Editor", this.diagramEditor);
    this.tp.addTab("State Simulator", this.simulator);
    this.tp.addTab("Human Simulator", this.humanSimulator);
    this.tp.setSelectedIndex(var4);
    this.tp.addChangeListener(this);
    var5.add(this.tp, "Center");
    this.addInternalFrameListener(frameListener);
  }

  ImageIcon createImageIcon(String imageUri) {
    return new ImageIcon(Objects.requireNonNull(this.getClass().getClassLoader().getResource(imageUri)));
  }

  void setFileName(String filename) {
    this.fileName = filename;
  }

  String getFileName() {
    return this.fileName;
  }

  TuringMachine getTuringMachine() {
    return this.machine;
  }

  void displayDiagram() {
    this.tp.setSelectedIndex(DIAGRAM_TAB);
  }

  void displaySimulation() {
    this.tp.setSelectedIndex(SIMULATION_TAB);
  }

  void displayHuman() {
    this.tp.setSelectedIndex(HUMAN_TAB);
  }

  public void stateChanged(ChangeEvent event) {
    this.simulator.refresh();
  }

  void exportToJPEG(File file) {
    this.diagramEditor.exportToJPEG(file);
  }

  void printDiagram() {
    this.diagramEditor.printDiagram();
  }
}
