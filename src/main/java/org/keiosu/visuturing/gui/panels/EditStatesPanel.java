package org.keiosu.visuturing.gui.panels;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.keiosu.visuturing.core.State;
import org.keiosu.visuturing.core.Symbols;
import org.keiosu.visuturing.core.Transition;
import org.keiosu.visuturing.core.TuringMachine;

public class EditStatesPanel extends VTPanel implements ActionListener, ListSelectionListener {
    JTextField stateField;
    JList<State> stateList;
    List<State> states;
    JButton removeButton;
    JButton removeAllButton;
    TuringMachine machine;
    int noAdditions;

    public EditStatesPanel(TuringMachine machine) {
        super("edit-states-panel.gif");
        this.states = new ArrayList<>(machine.getStates());
        this.machine = machine;
        this.initialize();
        this.noAdditions = 0;
    }

    public boolean hasTransitions(String state) {
        List<Transition> transitions = this.machine.getTransitions();
        for (Transition transition : transitions) {
            if (transition.getCurrentState().equals(state) || transition.getNextState().equals(state)) {
                return true;
            }
        }
        return false;
    }

    public void initialize() {
        if (this.states.size() <= 1) {
            this.states = new ArrayList<>();
            this.states.add(new State(Symbols.STATE_BEGINNING_STATE));
            this.states.add(new State(Symbols.STATE_HALTING_STATE));
        }

        this.stateList = new JList<>(this.states.toArray(new State[]{}));
        JLayeredPane pane = new JLayeredPane();
        this.panel.setLayout(new BorderLayout());
        this.panel.add(pane);
        pane.setPreferredSize(this.panel.getPreferredSize());
        JLabel stateLabel = new JLabel("State:");
        stateLabel.setBounds(0, 16, 65, 20);
        this.stateField = new JTextField();
        this.stateField.setBounds(79, 16, 119, 20);
        JButton addLabel = new JButton("Add");
        addLabel.setName("Add");
        addLabel.addActionListener(this);
        addLabel.setBounds(205, 16, 94, 20);
        JLabel statesLabel = new JLabel("States:");
        statesLabel.setBounds(0, 49, 65, 20);
        this.stateList = new JList<>(this.states.toArray(new State[]{}));
        this.stateList.addListSelectionListener(this);
        JScrollPane statesPane = new JScrollPane(this.stateList);
        statesPane.setBounds(79, 48, 120, 170);
        this.removeButton = new JButton("Remove");
        this.removeButton.setName("Remove");
        this.removeButton.addActionListener(this);
        this.removeButton.setEnabled(false);
        this.removeButton.setBounds(205, 48, 94, 20);
        this.removeAllButton = new JButton("Remove All");
        this.removeAllButton.setName("Remove All");
        this.removeAllButton.addActionListener(this);
        this.removeAllButton.setEnabled(false);
        this.removeAllButton.setBounds(205, 76, 94, 20);
        pane.add(stateLabel, JLayeredPane.DEFAULT_LAYER);
        pane.add(this.stateField, JLayeredPane.DEFAULT_LAYER);
        pane.add(statesLabel, JLayeredPane.DEFAULT_LAYER);
        pane.add(addLabel, JLayeredPane.DEFAULT_LAYER);
        pane.add(statesPane, JLayeredPane.DEFAULT_LAYER);
        pane.add(this.removeAllButton, JLayeredPane.DEFAULT_LAYER);
        pane.add(this.removeButton, JLayeredPane.DEFAULT_LAYER);
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() instanceof JButton) {
            JButton source = (JButton) event.getSource();
            switch (source.getName()) {
                case "Add":
                    String stateName = this.stateField.getText();
                    if (stateName != null && !stateName.trim().equals("")) {
                        boolean stateExists = false;
                        int idx = 0;
                        while (idx < this.states.size() && !stateExists) {
                            if (stateName.equals(this.states.get(idx++).getName())) {
                                stateExists = true;
                            }
                        }

                        if (!stateExists
                            && !stateName.equals(Symbols.STATE_HALTING_STATE)
                            && !stateName.equals(Symbols.STATE_BEGINNING_STATE)) {
                            this.states.add(
                                new State(stateName, new Point(100 * (this.noAdditions + 1), 50)));
                            ++this.noAdditions;
                        }

                        this.stateList.setListData(this.states.toArray(new State[]{}));
                        this.stateField.setText("");
                        if (!this.removeAllButton.isEnabled()) {
                            this.removeAllButton.setEnabled(true);
                        }
                    }
                    break;
                case "Remove":
                    int selectedIndex = this.stateList.getSelectedIndex();
                    if (selectedIndex > -1) {
                        if (this.hasTransitions(this.states.get(selectedIndex).getName())) {
                            JOptionPane.showMessageDialog(
                                this,
                                "The selected state has one or more transitions.\n\nPlease delete these before removing the state.",
                                "VisuTuring - Integrity Protect",
                                JOptionPane.ERROR_MESSAGE);
                        } else {
                            this.states.remove(selectedIndex);
                            this.stateList.setListData(this.states.toArray(new State[]{}));
                            this.removeButton.setEnabled(false);
                        }
                    }
                    break;
                case "Remove All":
                    boolean customStatesHaveTransitions = false;
                    List<State> customStates = new ArrayList<>();

                    for (State state : this.states) {
                        if (!state
                            .getName()
                            .equals(Symbols.STATE_BEGINNING_STATE)
                            && !state
                            .getName()
                            .equals(Symbols.STATE_HALTING_STATE)) {
                            if (this.hasTransitions(state.getName())) {
                                customStatesHaveTransitions = true;
                                customStates.add(state);
                            }
                        } else {
                            customStates.add(state);
                        }
                    }

                    if (customStatesHaveTransitions) {
                        JOptionPane.showMessageDialog(
                            this,
                            "One or more of the states have transitions and were not removed.\n\nPlease delete these transitions before removing the state(s).",
                            "VisuTuring - Integrity Protect",
                            JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                    this.states = customStates;
                    this.stateList.setListData(this.states.toArray(new State[]{}));
                    this.removeAllButton.setEnabled(false);
                    break;
            }
        }
    }

    public void valueChanged(ListSelectionEvent event) {
        this.updateButtonStates();
    }

    public void updateButtonStates() {
        int selectedIndex = this.stateList.getSelectedIndex();
        if (selectedIndex > -1) {
            String selectedState = this.states.get(selectedIndex).getName();
            this.removeButton.setEnabled(!selectedState.equals(Symbols.STATE_BEGINNING_STATE)
                && !selectedState.equals(Symbols.STATE_HALTING_STATE));
        }

        if (this.states.size() > 2) {
            if (!this.removeAllButton.isEnabled()) {
                this.removeAllButton.setEnabled(true);
            }
        } else if (this.removeAllButton.isEnabled()) {
            this.removeAllButton.setEnabled(false);
        }
    }

    public List<State> getStates() {
        return this.states;
    }
}
