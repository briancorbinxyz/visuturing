package org.keiosu.visuturing.gui.dialogs;

import org.keiosu.visuturing.core.TuringMachine;
import org.keiosu.visuturing.gui.panels.EditAlphabetPanel;
import org.keiosu.visuturing.gui.panels.EditDescriptionPanel;
import org.keiosu.visuturing.gui.panels.EditStatesPanel;
import org.keiosu.visuturing.gui.panels.EditTransitionsPanel;
import java.awt.CardLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

public class CreationWizard extends AbstractDialog implements ActionListener {
  private TuringMachine tm;
  public static final String NEXT_BUTTON = "Next >>";
  public static final String BACK_BUTTON = "<< Back";
  public static final String ED_PANEL = "Edit Description";
  public static final String ES_PANEL = "Edit States";
  public static final String EA_PANEL = "Edit Alphabet";
  public static final String ET_PANEL = "Edit Transitions";
  private CardLayout cl = new CardLayout();
  private JPanel wizardPanel = new JPanel();
  private JButton backButton;
  private JButton nextButton;
  private int cardIndex;
  private EditDescriptionPanel edp;
  private EditStatesPanel esp;
  private EditAlphabetPanel eap;
  private EditTransitionsPanel etp;

  public CreationWizard(Frame var1) {
    super(var1, "New Turing Machine Wizard");
    this.wizardPanel.setLayout(this.cl);
    this.tm = new TuringMachine();
    this.edp = new EditDescriptionPanel(this.tm);
    this.esp = new EditStatesPanel(this.tm);
    this.eap = new EditAlphabetPanel(this.tm);
    this.etp = new EditTransitionsPanel(this.tm);
    this.wizardPanel.add("Edit Description", this.edp);
    this.wizardPanel.add("Edit States", this.esp);
    this.wizardPanel.add("Edit Alphabet", this.eap);
    this.wizardPanel.add("Edit Transitions", this.etp);
    this.wizardPanel.setPreferredSize(this.edp.getPreferredSize());
    this.backButton = new JButton("<< Back");
    this.backButton.setName("<< Back");
    this.backButton.addActionListener(this);
    this.addButton(this.backButton);
    this.backButton.setEnabled(false);
    this.nextButton = new JButton("Next >>");
    this.nextButton.setName("Next >>");
    this.nextButton.addActionListener(this);
    this.addButton(this.nextButton);
    this.init(this.wizardPanel);
    this.cardIndex = 0;
  }

  public void actionPerformed(ActionEvent event) {
    this.updateTuringMachine();
    if (event.getSource() instanceof JButton) {
      JButton var2 = (JButton) event.getSource();
      if (var2.getName().equals("<< Back")) {
        this.cl.previous(this.wizardPanel);
        --this.cardIndex;
        if (this.cardIndex == 0) {
          var2.setEnabled(false);
        }

        if (!this.nextButton.isEnabled()) {
          this.nextButton.setEnabled(true);
        }
      } else if (var2.getName().equals("Next >>")) {
        this.cl.next(this.wizardPanel);
        ++this.cardIndex;
        if (this.cardIndex == this.wizardPanel.getComponentCount() - 1) {
          var2.setEnabled(false);
        }

        if (!this.backButton.isEnabled()) {
          this.backButton.setEnabled(true);
        }
      } else if (var2.getName().equals("Cancel")) {
        this.tm = null;
      }
    }

    if (this.cardIndex == 3) {
      this.etp.refresh();
    }

    super.actionPerformed(event);
  }

  public TuringMachine getTuringMachine() {
    return this.tm;
  }

  private void updateTuringMachine() {
    switch(this.cardIndex) {
      case 0:
        this.tm.setName(this.edp.getName());
        this.tm.setDescription(this.edp.getDescription());
        break;
      case 1:
        this.tm.setStates(this.esp.getStates());
        break;
      case 2:
        this.tm.setAlphabet(this.eap.getAlphabet());
        break;
      case 3:
        this.tm.setTransitions(this.etp.getTransitions());
        break;
      default:
        throw new RuntimeException("Unknown index");
    }

  }
}
