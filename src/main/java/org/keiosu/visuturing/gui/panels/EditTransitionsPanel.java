package org.keiosu.visuturing.gui.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.keiosu.visuturing.core.State;
import org.keiosu.visuturing.core.Symbols;
import org.keiosu.visuturing.core.Transition;
import org.keiosu.visuturing.core.TuringMachine;

public class EditTransitionsPanel extends VTPanel implements ActionListener {
    JTextArea description;
    JButton diagramButton;
    JTable table;
    TuringMachine machine;
    List<String> columnNames;
    public static final String ADD_TRANSITION_BUTTON = "Add Transition";
    public static final String REMOVE_TRANSITION_BUTTON = "Remove Transition";
    public static final String GENERATE_BUTTON = "Generate Transition Diagram";

    public void refresh() {
        List<Object[]> rows = new ArrayList<>();
        List<Transition> transitions = this.machine.getTransitions();
        List<State> states = this.machine.getStates();
        List<String> alphabet = new ArrayList<>(this.machine.getAlphabet());

        for (Transition transition : transitions) {
            List<String> columnValues = new ArrayList<>(4);
            columnValues.add(transition.getCurrentState());
            columnValues.add("" + transition.getCurrentSymbol());
            columnValues.add(transition.getNextState());
            columnValues.add("" + transition.getTask());
            rows.add(columnValues.toArray());
        }

        DefaultTableModel tableModel = (DefaultTableModel) this.table.getModel();
        tableModel.setDataVector(rows.toArray(new Object[][] {}), this.columnNames.toArray());
        TableColumn nextStateColumn = this.table.getColumnModel().getColumn(2);
        JComboBox<State> stateJComboBox = new JComboBox<>(states.toArray(new State[]{}));
        nextStateColumn.setCellEditor(new DefaultCellEditor(stateJComboBox));
        TableColumn currentStateColumn = this.table.getColumnModel().getColumn(0);
        List<String> nonHaltingStates = new ArrayList<>();

        for (State state : states) {
            if (!state.getName().equals(Symbols.STATE_HALTING_STATE)) {
                nonHaltingStates.add(state.getName());
            }
        }

        JComboBox<String> nonHaltingStateComboBox = new JComboBox<>(nonHaltingStates.toArray(new String[]{}));
        currentStateColumn.setCellEditor(new DefaultCellEditor(nonHaltingStateComboBox));
        TableColumn currentSymbolColumn = this.table.getColumnModel().getColumn(1);
        currentSymbolColumn.setCellEditor(new DefaultCellEditor(
            new JComboBox<>(alphabet.toArray(new String[]{}))));
        TableColumn taskColumn = this.table.getColumnModel().getColumn(3);
        List<String> actionSymbols = new ArrayList<>(alphabet);
        actionSymbols.remove(String.valueOf(Symbols.LEFT_END_MARKER));
        actionSymbols.add(String.valueOf(Symbols.LEFT_ARROW));
        actionSymbols.add(String.valueOf(Symbols.RIGHT_ARROW));
        taskColumn.setCellEditor(new DefaultCellEditor(
            new JComboBox<>(actionSymbols.toArray(new String[]{}))));
    }

    public EditTransitionsPanel(TuringMachine machine) {
        super("edit-transitions-panel.gif");
        this.machine = machine;
        JLayeredPane pane = new JLayeredPane();
        this.panel.add(pane);
        pane.setPreferredSize(this.panel.getPreferredSize());
        this.columnNames = new ArrayList<>();
        this.columnNames.add("Current State");
        this.columnNames.add("Current Symbol");
        this.columnNames.add("Next State");
        this.columnNames.add("Task");
        this.table = new JTable();
        this.refresh();
        JScrollPane scrollPane = new JScrollPane(this.table);
        scrollPane.setBounds(0, 0, 300, 130);
        JButton addTransitionButton = new JButton(ADD_TRANSITION_BUTTON);
        addTransitionButton.setBounds(0, 138, 148, 20);
        addTransitionButton.addActionListener(this);
        addTransitionButton.setName(ADD_TRANSITION_BUTTON);
        JButton removeTransitionButton = new JButton(REMOVE_TRANSITION_BUTTON);
        removeTransitionButton.setBounds(152, 138, 148, 20);
        removeTransitionButton.addActionListener(this);
        removeTransitionButton.setName(REMOVE_TRANSITION_BUTTON);
        this.diagramButton = new JButton(GENERATE_BUTTON);
        this.diagramButton.setBounds(0, 165, 300, 20);
        this.diagramButton.setName(GENERATE_BUTTON);
        this.diagramButton.addActionListener(this);
        this.description = new JTextArea();
        this.description.setBounds(0, 187, 300, 50);
        this.description.setLineWrap(true);
        this.description.setWrapStyleWord(true);
        this.description.setText(
                "THIS TRANSITION MACHINE HAS A DIAGRAM.\nGENERATING A NEW DIAGRAM WILL REPLACE THAT ONE.\n"
                    + "ADDING OR REMOVING A TRANSITION USING THE TABLE WILL MODIFY DIAGRAM.");
        this.description.setOpaque(false);
        this.description.setEditable(false);
        this.description.setFont(new Font("Helvetica", Font.PLAIN, 10));
        this.description.setForeground(Color.red);
        if (!machine.hasDiagram()) {
            this.description.setVisible(false);
        }

        JButton modifyDiagramButton = new JButton("Create / Edit / View Transition Diagram");
        modifyDiagramButton.setBounds(0, 245, 300, 20);
        pane.add(scrollPane, JLayeredPane.DEFAULT_LAYER);
        pane.add(addTransitionButton, JLayeredPane.DEFAULT_LAYER);
        pane.add(removeTransitionButton, JLayeredPane.DEFAULT_LAYER);
        pane.add(this.diagramButton, JLayeredPane.DEFAULT_LAYER);
        pane.add(this.description, JLayeredPane.DEFAULT_LAYER);
        pane.add(modifyDiagramButton, JLayeredPane.DEFAULT_LAYER);
    }

    public String getDescription() {
        return this.description.getText();
    }

    @SuppressWarnings("rawtypes")
    public List<Transition> getTransitions() {
        DefaultTableModel tableModel = (DefaultTableModel) this.table.getModel();
        Vector<Vector> transitionModel = tableModel.getDataVector();
        List<Transition> transitions = new ArrayList<>();

        for (List<?> columns : transitionModel) {
            boolean incompleteTransition = false;
            String currentStateValue = columns.get(0).toString();
            if (currentStateValue == null || currentStateValue.equals("")) {
                incompleteTransition = true;
            }

            String currentSymbolValue = (String) columns.get(1);
            char currentSymbol = ' ';
            if (currentSymbolValue == null || currentSymbolValue.equals("")) {
                incompleteTransition = true;
            }

            if (!incompleteTransition) {
                currentSymbol = ((String) columns.get(1)).charAt(0);
            }

            String nextStateValue = columns.get(2).toString();
            if (nextStateValue == null || nextStateValue.equals("")) {
                incompleteTransition = true;
            }

            String nextTaskValue = (String) columns.get(3);
            char nextTask = ' ';
            if (nextTaskValue == null || nextTaskValue.equals("")) {
                incompleteTransition = true;
            }

            if (!incompleteTransition) {
                nextTask = ((String) columns.get(3)).charAt(0);
            }

            if (!incompleteTransition) {
                Transition transition = new Transition(currentStateValue, currentSymbol, nextStateValue,
                    nextTask);
                Point2D from = this.machine.stateFor(currentStateValue).getLocation();
                Point2D to = this.machine.stateFor(nextStateValue).getLocation();
                Double cp =
                    new Double(
                        from.getX() + (to.getX() - from.getX()) / 2.0D,
                        from.getY() + (to.getY() - from.getY()) / 2.0D);
                java.awt.geom.QuadCurve2D.Double curve = new java.awt.geom.QuadCurve2D.Double();
                curve.setCurve(from, cp, to);
                transition.setEdge(curve);
                transitions.add(Objects.requireNonNullElse(
                    machine.getEqualTransition(transition), transition));
            }
        }

        return transitions;
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() instanceof JButton) {
            JButton source = (JButton) event.getSource();
            switch (source.getName()) {
                case ADD_TRANSITION_BUTTON:
                    addTransitionActionPerformed();
                    break;
                case REMOVE_TRANSITION_BUTTON:
                    removeTransitionActionPerformed();
                    break;
                case GENERATE_BUTTON:
                    generateDiagramActionPerformed();
                    break;
            }
        }
    }

    private void generateDiagramActionPerformed() {
        if (this.machine.hasDiagram()) {
            int returnValue =
                JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to generate a new diagram?\n\nThis will replace your existing diagram",
                    "VisuTuring",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null);
            if (returnValue == 1) {
                return;
            }
        }

        JOptionPane.showMessageDialog(
            this,
            "A transition diagram was automatically created for your Turing Machine by VisuTuring.",
            "VisuTuring",
            JOptionPane.INFORMATION_MESSAGE,
            null);
        this.machine.setHasDiagram(true);
        this.description.setVisible(true);
    }

    private void removeTransitionActionPerformed() {
        DefaultTableModel tableModel = (DefaultTableModel) this.table.getModel();
        if (this.table.getSelectedRow() != -1) {
            tableModel.removeRow(this.table.getSelectedRow());
        }

        if (tableModel.getRowCount() < 1 && this.diagramButton.isEnabled()) {
            this.diagramButton.setEnabled(false);
        }
    }

    private void addTransitionActionPerformed() {
        DefaultTableModel tableModel = (DefaultTableModel) this.table.getModel();
        tableModel.addRow(new Object[]{"", "", "", ""});
        if (tableModel.getRowCount() > 0 && !this.diagramButton.isEnabled()) {
            this.diagramButton.setEnabled(true);
        }
    }
}
