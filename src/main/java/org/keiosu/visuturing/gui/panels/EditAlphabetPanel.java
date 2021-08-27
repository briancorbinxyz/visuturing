package org.keiosu.visuturing.gui.panels;

import java.awt.BorderLayout;
import java.awt.Font;
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
import org.keiosu.visuturing.core.Symbols;
import org.keiosu.visuturing.core.Transition;
import org.keiosu.visuturing.core.TuringMachine;

public class EditAlphabetPanel extends VTPanel implements ActionListener, ListSelectionListener {

    public static final String ADD = "Add";
    public static final String REMOVE = "Remove";
    public static final String REMOVE_ALL = "Remove All";
    public static final String ADD_0_1 = "Add {0,1}";
    public static final String ADD_A_B = "Add {a,b}";
    public static final String ADD_0_9 = "Add {0..9}";

    JTextField symbolField;
    JList<String> alphabetList;
    List<String> alphabet;
    JButton removeButton;
    JButton removeAllButton;
    TuringMachine machine;

    public EditAlphabetPanel(TuringMachine machine) {
        super("edit-alphabet-panel.gif");
        this.machine = machine;
        JLayeredPane pane = new JLayeredPane();
        this.panel.setLayout(new BorderLayout());
        this.panel.add(pane);
        pane.setPreferredSize(this.panel.getPreferredSize());
        JLabel symbolLabel = new JLabel("Symbol:");
        symbolLabel.setBounds(0, 16, 65, 20);
        this.symbolField = new JTextField();
        this.symbolField.setBounds(79, 16, 119, 20);
        JButton addButton = new JButton(ADD);
        addButton.setName(ADD);
        addButton.addActionListener(this);
        addButton.setBounds(205, 16, 95, 20);
        JLabel alphabetLabel = new JLabel("Alphabet:");
        alphabetLabel.setBounds(0, 49, 65, 20);
        this.alphabet = new ArrayList<>(machine.getAlphabet());
        this.alphabetList = new JList<>(this.alphabet.toArray(new String[] {}));
        this.alphabetList.addListSelectionListener(this);
        this.alphabetList.setFont(new Font("sanserif", Font.PLAIN, 14));
        JScrollPane alphabetListPane = new JScrollPane(this.alphabetList);
        alphabetListPane.setBounds(79, 48, 120, 170);
        this.removeButton = new JButton(REMOVE);
        this.removeButton.setName(REMOVE);
        this.removeButton.addActionListener(this);
        this.removeButton.setEnabled(false);
        this.removeButton.setBounds(205, 48, 95, 20);
        this.removeAllButton = new JButton(REMOVE_ALL);
        this.removeAllButton.setName(REMOVE_ALL);
        this.removeAllButton.addActionListener(this);
        this.removeAllButton.setEnabled(false);
        this.removeAllButton.setBounds(205, 76, 95, 20);
        JButton binaryButton = new JButton(ADD_0_1);
        binaryButton.setName(ADD_0_1);
        binaryButton.addActionListener(this);
        binaryButton.setBounds(205, 142, 95, 20);
        JButton abButton = new JButton(ADD_A_B);
        abButton.setName(ADD_A_B);
        abButton.addActionListener(this);
        abButton.setBounds(205, 170, 95, 20);
        JButton numeralsButton = new JButton(ADD_0_9);
        numeralsButton.setName(ADD_0_9);
        numeralsButton.addActionListener(this);
        numeralsButton.setBounds(205, 198, 95, 20);
        pane.add(symbolLabel, JLayeredPane.DEFAULT_LAYER);
        pane.add(this.symbolField, JLayeredPane.DEFAULT_LAYER);
        pane.add(alphabetLabel, JLayeredPane.DEFAULT_LAYER);
        pane.add(addButton, JLayeredPane.DEFAULT_LAYER);
        pane.add(alphabetListPane, JLayeredPane.DEFAULT_LAYER);
        pane.add(this.removeAllButton, JLayeredPane.DEFAULT_LAYER);
        pane.add(this.removeButton, JLayeredPane.DEFAULT_LAYER);
        pane.add(binaryButton, JLayeredPane.DEFAULT_LAYER);
        pane.add(abButton, JLayeredPane.DEFAULT_LAYER);
        pane.add(numeralsButton, JLayeredPane.DEFAULT_LAYER);
    }

    public boolean hasTransitions(char fromSymbol) {
        List<Transition> transitions = this.machine.getTransitions();
        for (Transition transition : transitions) {
            if (transition.getCurrentSymbol() == fromSymbol || transition.getTask() == fromSymbol) {
                return true;
            }
        }

        return false;
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() instanceof JButton) {
            JButton source = (JButton) event.getSource();
            switch (source.getName()) {
                case ADD:
                    addActionPerformed();
                    break;
                case REMOVE:
                    removeActionPerformed(source);
                    break;
                case REMOVE_ALL:
                    removeAllActionPerformed(source);
                    break;
                case ADD_0_1:
                    addSymbolsPerformed("0", "1");
                    break;
                case ADD_A_B:
                    addSymbolsPerformed("a", "b");
                    break;
                case ADD_0_9:
                    addSymbolsPerformed("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
                    break;
            }
        }
    }

    private void addSymbolsPerformed(String... symbols) {
        for (String symbol : symbols) {
            this.addSymbol(symbol);
        }
        if (!this.removeAllButton.isEnabled()) {
            this.removeAllButton.setEnabled(true);
        }
    }

    private void removeAllActionPerformed(JButton button) {
        boolean hasTransitions = false;
        List<String> newAlphabet = new ArrayList<>();

        for (String s : this.alphabet) {
            char c = s.charAt(0);
            if (this.isStandardSymbol("" + c) || this.hasTransitions(c)) {
                newAlphabet.add("" + c);
                if (!this.isStandardSymbol("" + c)) {
                    hasTransitions = true;
                }
            }
        }

        this.alphabet = newAlphabet;
        this.alphabetList.setListData(this.alphabet.toArray(new String[] {}));
        button.setEnabled(false);
        if (hasTransitions) {
            JOptionPane.showMessageDialog(
                    this,
                    "One or more of the selected symbols are involved\nin one or more transitions.\n\nPlease delete these before removing the symbol(s).",
                    "VisuTuring - Integrity Protect",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeActionPerformed(JButton button) {
        int selectedIndex = this.alphabetList.getSelectedIndex();
        if (selectedIndex > -1) {
            if (!this.hasTransitions(this.alphabet.get(selectedIndex).charAt(0))) {
                this.alphabet.remove(selectedIndex);
                this.alphabetList.setListData(this.alphabet.toArray(new String[] {}));
                button.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "The selected symbol is involved in one or more transitions.\n\nPlease delete these before removing the symbol.",
                        "VisuTuring - Integrity Protect",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addActionPerformed() {
        String symbolFieldText = this.symbolField.getText();
        if (symbolFieldText != null && symbolFieldText.length() == 1) {
            if (this.notInAlphabet(symbolFieldText) && !this.isStandardSymbol(symbolFieldText)) {
                this.alphabet.add(symbolFieldText);
            }

            this.alphabetList.setListData(this.alphabet.toArray(new String[] {}));
            this.symbolField.setText("");
            if (!this.removeAllButton.isEnabled()) {
                this.removeAllButton.setEnabled(true);
            }
        }
    }

    public List<String> getAlphabet() {
        return this.alphabet;
    }

    boolean notInAlphabet(String symbol) {
        boolean found = false;
        int symbolIndex = 0;
        while (symbolIndex < this.alphabet.size() && !found) {
            if (symbol.equals(this.alphabet.get(symbolIndex++))) {
                found = true;
            }
        }
        return !found;
    }

    void addSymbol(String symbol) {
        if (notInAlphabet(symbol)) {
            alphabet.add(symbol);
        }
        this.alphabetList.setListData(this.alphabet.toArray(new String[] {}));
    }

    boolean isStandardSymbol(String symbol) {
        return symbol.equals(String.valueOf(Symbols.LEFT_END_MARKER))
                || symbol.equals(String.valueOf(Symbols.SPACE));
    }

    public void valueChanged(ListSelectionEvent event) {
        int selectedIndex = this.alphabetList.getSelectedIndex();
        if (selectedIndex > -1) {
            String symbol = this.alphabet.get(selectedIndex);
            this.removeButton.setEnabled(!this.isStandardSymbol(symbol));
        }

        if (this.alphabet.size() > 4) {
            if (!this.removeAllButton.isEnabled()) {
                this.removeAllButton.setEnabled(true);
            }
        } else if (this.removeAllButton.isEnabled()) {
            this.removeAllButton.setEnabled(false);
        }
    }
}
