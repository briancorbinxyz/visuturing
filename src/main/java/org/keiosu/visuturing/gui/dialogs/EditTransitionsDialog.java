package org.keiosu.visuturing.gui.dialogs;

import org.keiosu.visuturing.core.TuringMachine;
import org.keiosu.visuturing.gui.panels.EditTransitionsPanel;
import java.awt.Frame;
import java.util.List;

public class EditTransitionsDialog extends AbstractDialog {
  private EditTransitionsPanel transitionEditor;

  public EditTransitionsDialog(Frame frame, TuringMachine turingMachine) {
    super(frame, "Edit Transition Table");
    transitionEditor = new EditTransitionsPanel(turingMachine);
    init(transitionEditor);
  }

  public List getTransitions() {
    return transitionEditor.getTransitions();
  }
}
