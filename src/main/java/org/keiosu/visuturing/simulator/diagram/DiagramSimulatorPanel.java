package org.keiosu.visuturing.simulator.diagram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import org.keiosu.visuturing.core.Configuration;
import org.keiosu.visuturing.core.Symbols;
import org.keiosu.visuturing.core.Transition;
import org.keiosu.visuturing.core.TuringMachine;
import org.keiosu.visuturing.diagram.DiagramEditor;
import org.keiosu.visuturing.mousetools.SimulatingTool;
import org.keiosu.visuturing.simulator.AbstractSimulatorPanel;

public class DiagramSimulatorPanel extends AbstractSimulatorPanel
        implements Runnable, ComponentListener, FocusListener {
    private JRadioButton nonDeterminismChooseFirstButton;
    private JRadioButton nonDeterminismChooseLastButton;
    private JRadioButton nonDeterminismRandomButtom;
    private JRadioButton nonDeterminismChoiceButton;
    private JTable resultsTable;
    private DiagramEditor diagram;
    private SimulatingTool simTool;
    private Configuration config;
    private Configuration prevConfig;
    private JPanel nonDeterminismPanel;
    private JTextArea computationText;
    private JScrollPane computationScroller;
    private Transition currentTransition;
    private String startWord;
    private boolean paused;
    private boolean stopped;
    private Thread runner;
    private JSplitPane sp;
    private int noConfigs;
    private final DefaultTableModel resultsTableModel;

    public DiagramSimulatorPanel(TuringMachine turingMachine) {
        super(turingMachine);
        this.addFocusListener(this);
        this.currentTransition = null;
        this.speed = 1.0D;
        this.noConfigs = 0;
        this.setLayout(new BorderLayout(0, 0));
        JPanel leftPane = new JPanel(new BorderLayout());
        JPanel rightPane = new JPanel(new BorderLayout());
        this.nonDeterminismPanel = new JPanel(new FlowLayout());
        this.nonDeterminismPanel.setBorder(BorderFactory.createTitledBorder("Non-Determinism"));
        this.nonDeterminismPanel.add(new JLabel("Always Choose: "));
        ButtonGroup nonDeterminismButtonGroup = new ButtonGroup();
        this.nonDeterminismChooseFirstButton = new JRadioButton("First Transition");
        nonDeterminismButtonGroup.add(this.nonDeterminismChooseFirstButton);
        this.nonDeterminismPanel.add(this.nonDeterminismChooseFirstButton);
        this.nonDeterminismChooseLastButton = new JRadioButton("Last Transition");
        nonDeterminismButtonGroup.add(this.nonDeterminismChooseLastButton);
        this.nonDeterminismPanel.add(this.nonDeterminismChooseLastButton);
        this.nonDeterminismRandomButtom = new JRadioButton("Random Transition");
        nonDeterminismButtonGroup.add(this.nonDeterminismRandomButtom);
        this.nonDeterminismPanel.add(this.nonDeterminismRandomButtom);
        this.nonDeterminismChoiceButton = new JRadioButton("Prompted Transition");
        nonDeterminismButtonGroup.add(this.nonDeterminismChoiceButton);
        this.nonDeterminismPanel.add(this.nonDeterminismChoiceButton);
        nonDeterminismButtonGroup.setSelected(this.nonDeterminismRandomButtom.getModel(), true);
        leftPane.add(this.nonDeterminismPanel, "North");
        if (turingMachine.isDeterministic()) {
            this.nonDeterminismPanel.setVisible(false);
        }

        if (!turingMachine.hasDiagram()) {
            turingMachine.generateDiagram();
        }

        this.diagram = new DiagramEditor(turingMachine);
        this.simTool = new SimulatingTool(this.diagram);
        this.diagram.setTool(this.simTool);
        this.diagram.setGrid(false);
        this.diagram.setPreferredSize(this.diagram.getExtents());
        this.diagram.addComponentListener(this);
        leftPane.add(this.diagram, "Center");
        List<String> headers = new ArrayList<String>(2);
        headers.add("Input");
        headers.add("Output");
        resultsTableModel = new DefaultTableModel(new Object[][] {}, headers.toArray());
        this.resultsTable = new JTable(resultsTableModel);
        this.resultsTable.setFont(new Font("Helvetica", 0, 14));
        JScrollPane var13 = new JScrollPane(this.resultsTable);
        rightPane.add(var13);
        this.computationText = new JTextArea(" ");
        this.computationText.setFont(new Font("Helvetica", 0, 14));
        this.computationText.setBackground(Color.white);
        this.computationScroller = new JScrollPane(this.computationText, 20, 32);
        this.computationScroller.setBorder(
                BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Computation"));
        rightPane.add(this.computationScroller, "South");
        this.sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPane, rightPane);
        this.add(this.sp);
        double var9 = Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 0.75D;
        double var11 = this.diagram.getPreferredSize().getWidth();
        this.sp.setDividerLocation((int) (var11 < var9 ? var11 : var9));
        this.diagram.setDescriptionShown(true);
    }

    private void addComputation(Configuration var1) {
        this.computationText.setText(
                this.computationText.getText().equals(" ")
                        ? var1.toString()
                        : this.computationText.getText()
                                + " "
                                + Symbols.ASSERTION
                                + " "
                                + var1.toString());
        this.computationText.setCaretPosition(this.computationText.getText().length());
        ++this.noConfigs;
    }

    public void setVisible(boolean var1) {
        super.setVisible(var1);
        if (var1) {
            int var2 =
                    (int)
                            ((this.sp.getBounds().getWidth() - (double) this.sp.getDividerSize())
                                    / 2.0D);
            this.sp.setDividerLocation(var2);
        }
    }

    public void play() {
        if (this.stopped) {
            this.runner = null;
        }

        if (this.runner == null) {
            this.noConfigs = 0;
            this.config = new Configuration(Symbols.STATE_BEGINNING_STATE, this.inputWord, 0);
            this.startWord = new String(this.inputWord);
            this.computationText.setText(" ");
            this.addComputation(this.config);
            this.simTool.setConfig(this.config);
            this.paused = false;
            this.stopped = false;
            this.runner = new Thread(this);
            this.runner.start();
        } else {
            synchronized (this.runner) {
                this.paused = false;
                this.runner.notify();
            }
        }
    }

    public void run() {
        Thread var1 = Thread.currentThread();

        while (true) {
            try {
                synchronized (var1) {
                    while (true) {
                        if (!this.paused) {
                            if (this.stopped) {
                                return;
                            }
                            break;
                        }

                        var1.wait();
                    }
                }

                Thread.sleep((long) (1000.0D / this.speed));
                this.simTool.setConfig(null);
                List var2 = this.machine.getNextConfig(this.config);
                int var3 = -1;
                this.prevConfig = this.config;
                List var8;
                if (var2.size() == 1) {
                    this.config = (Configuration) var2.get(0);
                    if (this.config != null) {
                        this.addComputation(this.config);
                    }

                    var3 = 0;
                } else if (var2.size() > 1) {
                    if (this.nonDeterminismChooseFirstButton.isSelected()) {
                        this.config = (Configuration) var2.get(0);
                        var3 = 0;
                    } else if (this.nonDeterminismChooseLastButton.isSelected()) {
                        var3 = var2.size() - 1;
                        this.config = (Configuration) var2.get(var3);
                    } else if (this.nonDeterminismRandomButtom.isSelected()) {
                        Random var4 = new Random();
                        var3 = var4.nextInt(var2.size());
                        this.config = (Configuration) var2.get(var3);
                    } else {
                        var8 = this.machine.nextTransitionsFor(this.config);
                        DiagramSimulatorPanel.ChoiceDialog var5 =
                                new DiagramSimulatorPanel.ChoiceDialog(
                                        new Frame(), "Choose a transition", var8);
                        var5.setVisible(true);
                        if (var5.wasStopped()) {
                            this.stop();
                            return;
                        }

                        this.config = (Configuration) var2.get(var5.getChoice());
                        var3 = var5.getChoice();
                    }

                    if (this.config != null) {
                        this.addComputation(this.config);
                    }
                } else {
                    this.stopped = true;
                    this.addResult(this.config);
                    this.diagram.repaint();
                }

                if (var2.size() != 0 && var3 > -1) {
                    var8 = this.machine.nextTransitionsFor(this.prevConfig);
                    this.currentTransition = (Transition) var8.get(var3);
                }

                this.simTool.setTransition(this.currentTransition);

                for (int var9 = 0; (double) var9 < 10.0D; ++var9) {
                    Thread.sleep((long) (100.0D / this.speed));
                    this.simTool.increaseFrame();
                    this.diagram.repaint();
                }

                this.simTool.setFrame(1);
                this.currentTransition = null;
                this.simTool.setTransition(this.currentTransition);
                this.simTool.setConfig(this.config);
                if (this.config.getState().equals(Symbols.STATE_HALTING_STATE)) {
                    this.stopped = true;
                    this.addResult(this.config);
                    this.diagram.repaint();
                }

                this.diagram.repaint();
            } catch (InterruptedException var7) {
            }
        }
    }

    public void stop() {
        synchronized (this.runner) {
            this.stopped = true;
        }

        this.runner = null;
        this.computationText.setText(this.computationText.getText() + " [stopped by user]");
        this.computationText.setCaretPosition(this.computationText.getText().length());
    }

    public void pause() {
        synchronized (this.runner) {
            this.paused = true;
        }
    }

    private void addResult(Configuration configuration) {
        var resultTableRow = new ArrayList<String>(2);
        resultTableRow.add(startWord);
        if (configuration.getState().equals(Symbols.STATE_HALTING_STATE)) {
            computationText.setText(
                    computationText.getText() + " [halts on input] " + noConfigs + " steps");
            computationText.setCaretPosition(computationText.getText().length());
            resultTableRow.add(Symbols.trim(configuration.getWord()));
        } else {
            computationText.setText(computationText.getText() + " [stuck] " + noConfigs + " steps");
            computationText.setCaretPosition(computationText.getText().length());
            resultTableRow.add("");
        }
        resultsTableModel.addRow(resultTableRow.toArray());
    }

    public void componentChanged(ComponentEvent var1) {}

    public void componentResized(ComponentEvent var1) {
        this.zoomDiagram();
    }

    public void componentMoved(ComponentEvent var1) {
        this.zoomDiagram();
    }

    public void componentShown(ComponentEvent var1) {
        this.zoomDiagram();
    }

    public void componentHidden(ComponentEvent var1) {}

    public void zoomDiagram() {
        Dimension var7 = this.diagram.getExtents();
        double var3 = this.diagram.getSize().getWidth();
        double var5 = this.diagram.getSize().getHeight();
        double var1 = var3 / var7.getWidth();
        if (var5 / var7.getHeight() < var1) {
            var1 = var5 / var7.getHeight();
        }

        if (var1 > 1.0D) {
            var1 = 1.0D;
        }

        this.diagram.setZoom(var1);
    }

    public void focusGained(FocusEvent var1) {}

    public void focusLost(FocusEvent var1) {
        if (!this.stopped) {
            this.pause();
        }
    }

    public void refresh() {
        if (this.machine.isDeterministic()) {
            if (this.nonDeterminismPanel.isVisible()) {
                this.nonDeterminismPanel.setVisible(false);
            }
        } else if (!this.nonDeterminismPanel.isVisible()) {
            this.nonDeterminismPanel.setVisible(true);
        }

        this.zoomDiagram();
    }

    public class ChoiceDialog extends JDialog implements ItemListener, ActionListener {
        private int choice;
        private boolean stopped;

        public ChoiceDialog(Frame var2, String var3, List var4) {
            super(var2, var3, true);
            Container var5 = this.getContentPane();
            this.choice = 0;
            this.stopped = true;
            var5.setLayout(new BorderLayout());
            JPanel var6 = new JPanel(new GridLayout(var4.size(), 1, 10, 10));
            var6.setBorder(BorderFactory.createTitledBorder("Possible Transitions"));
            ButtonGroup var7 = new ButtonGroup();

            for (int var8 = 0; var8 < var4.size(); ++var8) {
                Transition var9 = (Transition) var4.get(var8);
                JRadioButton var10 = new JRadioButton(var9.toString());
                var10.setName(String.valueOf(var8));
                var10.addItemListener(this);
                var7.add(var10);
                var6.add(var10);
                if (var8 == 0) {
                    var10.setSelected(true);
                }
            }

            var5.add(var6);
            JButton var11 = new JButton("Continue");
            var11.addActionListener(this);
            var11.setName("okay");
            JButton var12 = new JButton("Stop");
            var12.addActionListener(this);
            var12.setName("cancel");
            JPanel var13 = new JPanel(new GridLayout(2, 1));
            var13.add(var11);
            var13.add(var12);
            var5.add(var13, "East");
            this.pack();
            this.setLocation(
                    (int)
                            ((Toolkit.getDefaultToolkit().getScreenSize().getWidth()
                                            - this.getSize().getWidth())
                                    / 2.0D),
                    (int)
                            ((Toolkit.getDefaultToolkit().getScreenSize().getHeight()
                                            - this.getSize().getHeight())
                                    / 2.0D));
            this.setResizable(false);
        }

        public int getChoice() {
            return this.choice;
        }

        public boolean wasStopped() {
            return this.stopped;
        }

        public void itemStateChanged(ItemEvent var1) {
            JRadioButton var2 = (JRadioButton) var1.getSource();
            if (var2.isSelected()) {
                this.choice = Integer.parseInt(var2.getName());
            }
        }

        public void actionPerformed(ActionEvent var1) {
            JButton var2 = (JButton) var1.getSource();
            if (var2.getName().equals("okay")) {
                this.stopped = false;
            }

            this.setVisible(false);
        }
    }
}
