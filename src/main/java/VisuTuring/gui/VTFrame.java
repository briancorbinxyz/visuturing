package VisuTuring.gui;

import VisuTuring.core.TuringMachine;
import VisuTuring.gui.panels.DiagramPanel;
import VisuTuring.gui.panels.SimulationPanel;
import VisuTuring.simulator.diagram.DiagramSimulator;
import VisuTuring.simulator.human.HumanSimulator;
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
  SimulationPanel simulator;
  SimulationPanel humanSimulator;
  JTabbedPane tp;
  DescriptionPanel descPanel;

  public VTFrame(InternalFrameListener var1, ActionListener var2, TuringMachine var3, int var4) {
    super(var3.getName(), true, true, true, true);
    this.listener = var2;
    this.machine = var3;
    var3.setChanged(false);
    this.fileName = "-untitled-";
    this.setSize(DEFAULT_FRAME_SIZE);
    this.setDefaultCloseOperation(0);
    this.setFrameIcon(this.createImageIcon("bitmaps/vticons.jpg"));
    this.descPanel = new DescriptionPanel(var3);
    this.diagramEditor = new DiagramPanel(var2, var3);
    this.simulator = new SimulationPanel(var2, new DiagramSimulator(var3));
    this.humanSimulator = new SimulationPanel(var2, new HumanSimulator(var3));
    Container var5 = this.getContentPane();
    var5.setLayout(new BorderLayout());
    this.tp = new JTabbedPane(1, 1);
    this.tp.addTab("Description", this.descPanel);
    this.tp.addTab("Diagram", this.diagramEditor);
    this.tp.addTab("Simulator", this.simulator);
    this.tp.addTab("Human Simulator", this.humanSimulator);
    this.tp.setSelectedIndex(var4);
    this.tp.addChangeListener(this);
    var5.add(this.tp, "Center");
    this.addInternalFrameListener(var1);
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
