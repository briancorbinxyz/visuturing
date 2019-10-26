package org.keiosu.visuturing.gui.dialogs;

import org.keiosu.visuturing.core.TuringMachine;
import org.keiosu.visuturing.gui.panels.EditStatesPanel;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

public class EditStatesDialog extends AbstractDialog implements ActionListener {
  private EditStatesPanel stateEditor;

  public EditStatesDialog(Frame var1, TuringMachine var2) {
    super(var1, "Edit States");
    this.stateEditor = new EditStatesPanel(var2);
    this.init(this.stateEditor);
  }

  public List getStates() {
    return this.stateEditor.getStates();
  }
}
