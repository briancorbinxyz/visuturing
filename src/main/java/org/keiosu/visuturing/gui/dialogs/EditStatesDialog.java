package org.keiosu.visuturing.gui.dialogs;

import org.keiosu.visuturing.core.TuringMachine;
import org.keiosu.visuturing.gui.panels.EditStatesPanel;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.util.List;

public class EditStatesDialog extends AbstractDialog implements ActionListener {
  private EditStatesPanel stateEditor;

  public EditStatesDialog(Frame frame, TuringMachine turingMachine) {
    super(frame, "Edit States");
    this.stateEditor = new EditStatesPanel(turingMachine);
    this.init(this.stateEditor);
  }

  public List getStates() {
    return this.stateEditor.getStates();
  }
}
