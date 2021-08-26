package org.keiosu.visuturing.simulator.diagram;

import static java.lang.System.currentTimeMillis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
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
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.AbstractButton;
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
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import org.keiosu.visuturing.core.Configuration;
import org.keiosu.visuturing.core.Symbols;
import org.keiosu.visuturing.core.Transition;
import org.keiosu.visuturing.core.TuringMachine;
import org.keiosu.visuturing.diagram.DiagramEditor;
import org.keiosu.visuturing.mousetools.TuringMachineDiagramSimulatingTool;
import org.keiosu.visuturing.simulator.AbstractSimulatorPanel;

public class DiagramSimulatorPanel extends AbstractSimulatorPanel
        implements Runnable, ComponentListener, FocusListener {
    private final JRadioButton nonDeterminismChooseFirstButton;
    private final JRadioButton nonDeterminismChooseLastButton;
    private final JRadioButton nonDeterminismRandomButton;
    private final DiagramEditor diagram;
    private final TuringMachineDiagramSimulatingTool simTool;
    private final JPanel nonDeterminismPanel;
    private final JTextArea computationText;
    private final ReentrantLock simulationLock;
    private final Condition simulationShouldProceed;
    private final Condition simulationFrameShouldProceed;
    private final JSplitPane sp;
    private final DefaultTableModel resultsTableModel;
    private Configuration config;
    private Transition currentTransition;
    private String startWord;
    private boolean paused;
    private boolean stopped;
    private Thread runner;
    private int noConfigs;

    public DiagramSimulatorPanel(TuringMachine turingMachine) {
        super(turingMachine);
        simulationLock = new ReentrantLock(true);
        simulationShouldProceed = simulationLock.newCondition();
        simulationFrameShouldProceed = simulationLock.newCondition();
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
        this.nonDeterminismRandomButton = new JRadioButton("Random Transition");
        nonDeterminismButtonGroup.add(this.nonDeterminismRandomButton);
        this.nonDeterminismPanel.add(this.nonDeterminismRandomButton);
        JRadioButton nonDeterminismChoiceButton = new JRadioButton("Prompted Transition");
        nonDeterminismButtonGroup.add(nonDeterminismChoiceButton);
        this.nonDeterminismPanel.add(nonDeterminismChoiceButton);
        nonDeterminismButtonGroup.setSelected(this.nonDeterminismRandomButton.getModel(), true);
        leftPane.add(this.nonDeterminismPanel, "North");
        if (turingMachine.isDeterministic()) {
            this.nonDeterminismPanel.setVisible(false);
        }

        if (!turingMachine.hasDiagram()) {
            turingMachine.generateDiagram();
        }

        this.diagram = new DiagramEditor(turingMachine);
        this.simTool = new TuringMachineDiagramSimulatingTool(this.diagram);
        this.diagram.setTool(this.simTool);
        this.diagram.setGrid(false);
        this.diagram.setPreferredSize(this.diagram.getExtents());
        this.diagram.addComponentListener(this);
        leftPane.add(this.diagram, "Center");
        List<String> headers = new ArrayList<>(2);
        headers.add("Input");
        headers.add("Output");
        resultsTableModel = new DefaultTableModel(new Object[][] {}, headers.toArray());
        JTable resultsTable = new JTable(resultsTableModel);
        resultsTable.setFont(new Font("Helvetica", Font.PLAIN, 14));
        rightPane.add(new JScrollPane(resultsTable));
        this.computationText = new JTextArea(" ");
        this.computationText.setFont(new Font("Helvetica", Font.PLAIN, 14));
        this.computationText.setBackground(Color.white);
        JScrollPane computationScroller =
                new JScrollPane(
                        this.computationText,
                        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        computationScroller.setBorder(
                BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Computation"));
        rightPane.add(computationScroller, "South");
        this.sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPane, rightPane);
        this.add(this.sp);
        double maxWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 0.75D;
        double diagramWidth = this.diagram.getPreferredSize().getWidth();
        this.sp.setDividerLocation((int) (Math.min(diagramWidth, maxWidth)));
    }

    private void addComputation(Configuration config) {
        this.computationText.setText(
                this.computationText.getText().equals(" ")
                        ? config.toString()
                        : this.computationText.getText()
                                + " "
                                + Symbols.ASSERTION
                                + " "
                                + config.toString());
        this.computationText.setCaretPosition(this.computationText.getText().length());
        ++this.noConfigs;
    }

    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            this.sp.setDividerLocation(
                    (int)
                            ((this.sp.getBounds().getWidth() - (double) this.sp.getDividerSize())
                                    / 2.0D));
        }
    }

    public void play() {
        simulationLock.lock();
        try {
            if (stopped) {
                if (runner != null) {
                    runner.interrupt();
                }
                runner = null;
            }
            if (runner == null) {
                resetSimulation();
                runner = new Thread(this);
                runner.start();
            } else {
                paused = false;
                simulationShouldProceed.signalAll();
            }
        } finally {
            simulationLock.unlock();
        }
    }

    public void run() {
        while (true) {
            try {
                simulationLock.lock();
                try {
                    while (true) {
                        if (this.stopped) {
                            return;
                        }
                        if (this.paused) {
                            simulationShouldProceed.await();
                        } else {
                            break;
                        }
                    }
                } finally {
                    simulationLock.unlock();
                }

                awaitNextFrame(currentTimeMillis() + (long) (1000 / this.speed));
                this.simTool.setStateFromAlphabet(config.getState());
                List<Configuration> nextConfig = this.machine.getNextConfig(this.config);
                int configIndex = -1;
                Configuration prevConfig = this.config;
                List<Transition> transitions;
                if (nextConfig.size() == 1) {
                    this.config = nextConfig.get(0);
                    if (this.config != null) {
                        this.addComputation(this.config);
                    }

                    configIndex = 0;
                } else if (nextConfig.size() > 1) {
                    if (this.nonDeterminismChooseFirstButton.isSelected()) {
                        this.config = nextConfig.get(0);
                        configIndex = 0;
                    } else if (this.nonDeterminismChooseLastButton.isSelected()) {
                        configIndex = nextConfig.size() - 1;
                        this.config = nextConfig.get(configIndex);
                    } else if (this.nonDeterminismRandomButton.isSelected()) {
                        Random random = new Random();
                        configIndex = random.nextInt(nextConfig.size());
                        this.config = nextConfig.get(configIndex);
                    } else {
                        transitions = this.machine.nextTransitionsFor(this.config);
                        DiagramSimulatorPanel.ChoiceDialog choiceDialog =
                                new ChoiceDialog(new Frame(), "Choose a transition", transitions);
                        choiceDialog.setVisible(true);
                        if (choiceDialog.wasStopped()) {
                            this.stop();
                            return;
                        }

                        this.config = nextConfig.get(choiceDialog.getChoice());
                        configIndex = choiceDialog.getChoice();
                    }

                    if (this.config != null) {
                        this.addComputation(this.config);
                    }
                } else {
                    this.stopped = true;
                    this.addResult(this.config);
                    this.diagram.repaint();
                }

                if (nextConfig.size() != 0 && configIndex > -1) {
                    transitions = this.machine.nextTransitionsFor(prevConfig);
                    this.currentTransition = transitions.get(configIndex);
                }

                this.simTool.setTransition(this.currentTransition);

                for (int s = 0; (double) s < 10.0D; ++s) {
                    awaitNextFrame(currentTimeMillis() + (long) (100 / this.speed));
                    this.simTool.increaseFrame();
                    this.diagram.repaint();
                }

                this.simTool.reset();
                this.currentTransition = null;
                this.simTool.setTransition(null);
                this.simTool.setStateFromAlphabet(this.config.getState());
                if (this.config.getState().equals(Symbols.STATE_HALTING_STATE)) {
                    this.stopped = true;
                    this.addResult(this.config);
                    this.diagram.repaint();
                }

                this.diagram.repaint();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stop() {
        simulationLock.lock();
        try {
            this.stopped = true;
        } finally {
            simulationLock.unlock();
        }

        this.runner = null;
        this.computationText.setText(this.computationText.getText() + " [stopped by user]");
        this.computationText.setCaretPosition(this.computationText.getText().length());
    }

    public void pause() {
        simulationLock.lock();
        try {
            this.paused = true;
        } finally {
            simulationLock.unlock();
        }
    }

    private void awaitNextFrame(long epochTimeMillis) throws InterruptedException {
        // All this to avoid Thread.sleep()...
        simulationLock.lock();
        try {
            boolean waitingForFrame = true;
            while (waitingForFrame) {
                waitingForFrame =
                        simulationFrameShouldProceed.awaitUntil(new Date(epochTimeMillis));
            }
        } finally {
            simulationLock.unlock();
        }
    }

    private void resetSimulation() {
        this.noConfigs = 0;
        this.config = new Configuration(Symbols.STATE_BEGINNING_STATE, this.inputWord, 0);
        this.startWord = this.inputWord;
        this.computationText.setText(" ");
        this.addComputation(this.config);
        this.simTool.setStateFromAlphabet(this.config.getState());
        this.paused = false;
        this.stopped = false;
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

    @Override
    public void componentResized(ComponentEvent event) {
        this.zoomDiagram();
    }

    @Override
    public void componentMoved(ComponentEvent event) {
        this.zoomDiagram();
    }

    @Override
    public void componentShown(ComponentEvent event) {
        this.zoomDiagram();
    }

    @Override
    public void componentHidden(ComponentEvent event) {}

    public void zoomDiagram() {
        double w = this.diagram.getSize().getWidth();
        double h = this.diagram.getSize().getHeight();
        double extentsWidth = w / this.diagram.getExtents().getWidth();
        if (h / this.diagram.getExtents().getHeight() < extentsWidth) {
            extentsWidth = h / this.diagram.getExtents().getHeight();
        }

        if (extentsWidth > 1.0D) {
            extentsWidth = 1.0D;
        }

        this.diagram.setZoom(extentsWidth);
    }

    public void focusGained(FocusEvent event) {}

    public void focusLost(FocusEvent event) {
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

    public static class ChoiceDialog extends JDialog implements ItemListener, ActionListener {
        private int choice;
        private boolean stopped;

        public ChoiceDialog(Frame frame, String title, List<Transition> transitions) {
            super(frame, title, true);
            Container contentPane = this.getContentPane();
            this.choice = 0;
            this.stopped = true;
            contentPane.setLayout(new BorderLayout());
            JPanel panel = new JPanel(new GridLayout(transitions.size(), 1, 10, 10));
            panel.setBorder(BorderFactory.createTitledBorder("Possible Transitions"));
            ButtonGroup buttonGroup = new ButtonGroup();

            for (int i = 0; i < transitions.size(); ++i) {
                Transition transition = transitions.get(i);
                JRadioButton transitionChoice = new JRadioButton(transition.toString());
                transitionChoice.setName(String.valueOf(i));
                transitionChoice.addItemListener(this);
                buttonGroup.add(transitionChoice);
                panel.add(transitionChoice);
                if (i == 0) {
                    transitionChoice.setSelected(true);
                }
            }

            contentPane.add(panel);
            JPanel actionPanel = new JPanel(new GridLayout(2, 1));
            actionPanel.add(newButton("Continue", "okay"));
            actionPanel.add(newButton("Stop", "cancel"));
            contentPane.add(actionPanel, "East");
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

        public void itemStateChanged(ItemEvent event) {
            JRadioButton source = (JRadioButton) event.getSource();
            if (source.isSelected()) {
                this.choice = Integer.parseInt(source.getName());
            }
        }

        public void actionPerformed(ActionEvent event) {
            JButton source = (JButton) event.getSource();
            if (source.getName().equals("okay")) {
                this.stopped = false;
            }

            this.setVisible(false);
        }

        private AbstractButton newButton(String text, String name) {
            JButton button = new JButton(text);
            button.addActionListener(this);
            button.setName(name);
            return button;
        }
    }
}
