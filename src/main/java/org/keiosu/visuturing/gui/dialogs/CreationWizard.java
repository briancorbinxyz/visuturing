package org.keiosu.visuturing.gui.dialogs;

import java.awt.CardLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.keiosu.visuturing.core.TuringMachine;
import org.keiosu.visuturing.gui.panels.EditAlphabetPanel;
import org.keiosu.visuturing.gui.panels.EditDescriptionPanel;
import org.keiosu.visuturing.gui.panels.EditStatesPanel;
import org.keiosu.visuturing.gui.panels.EditTransitionsPanel;

public class CreationWizard extends AbstractDialog implements ActionListener {
    private TuringMachine tm;
    public static final String NEXT_BUTTON = "Next >>";
    public static final String BACK_BUTTON = "<< Back";
    public static final String ED_PANEL = "Edit Description";
    public static final String ES_PANEL = "Edit States";
    public static final String EA_PANEL = "Edit Alphabet";
    public static final String ET_PANEL = "Edit Transitions";
    private final CardLayout cl = new CardLayout();
    private final JPanel wizardPanel = new JPanel();
    private final JButton backButton;
    private final JButton nextButton;
    private int cardIndex;
    private final EditDescriptionPanel edp;
    private final EditStatesPanel esp;
    private final EditAlphabetPanel eap;
    private final EditTransitionsPanel etp;

    public CreationWizard(Frame frame) {
        super(frame, "New Turing Machine Wizard");
        wizardPanel.setLayout(cl);
        tm = new TuringMachine();
        edp = new EditDescriptionPanel(tm);
        esp = new EditStatesPanel(tm);
        eap = new EditAlphabetPanel(tm);
        etp = new EditTransitionsPanel(tm);
        wizardPanel.add(ED_PANEL, edp);
        wizardPanel.add(ES_PANEL, esp);
        wizardPanel.add(EA_PANEL, eap);
        wizardPanel.add(ET_PANEL, etp);
        wizardPanel.setPreferredSize(edp.getPreferredSize());
        backButton = new JButton(BACK_BUTTON);
        backButton.setName(BACK_BUTTON);
        backButton.addActionListener(this);
        addButton(backButton);
        backButton.setEnabled(false);
        nextButton = new JButton(NEXT_BUTTON);
        nextButton.setName(NEXT_BUTTON);
        nextButton.addActionListener(this);
        addButton(nextButton);
        init(wizardPanel);
        cardIndex = 0;
    }

    public void actionPerformed(ActionEvent event) {
        updateTuringMachine();
        if (event.getSource() instanceof JButton) {
            JButton button = (JButton) event.getSource();
            switch (button.getName()) {
                case "<< Back":
                    cl.previous(wizardPanel);
                    --cardIndex;
                    if (cardIndex == 0) {
                        button.setEnabled(false);
                    }

                    if (!nextButton.isEnabled()) {
                        nextButton.setEnabled(true);
                    }
                    break;
                case "Next >>":
                    cl.next(wizardPanel);
                    ++cardIndex;
                    if (cardIndex == wizardPanel.getComponentCount() - 1) {
                        button.setEnabled(false);
                    }

                    if (!backButton.isEnabled()) {
                        backButton.setEnabled(true);
                    }
                    break;
                case "Cancel":
                    tm = null;
                    break;
            }
        }

        if (cardIndex == 3) {
            etp.refresh();
        }

        super.actionPerformed(event);
    }

    public TuringMachine getTuringMachine() {
        return tm;
    }

    private void updateTuringMachine() {
        switch (cardIndex) {
            case 0:
                tm.setName(edp.getName());
                tm.setDescription(edp.getDescription());
                break;
            case 1:
                tm.setStates(esp.getStates());
                break;
            case 2:
                tm.setAlphabet(eap.getAlphabet());
                break;
            case 3:
                tm.setTransitions(etp.getTransitions());
                break;
            default:
                throw new RuntimeException("Unknown index");
        }
    }
}
