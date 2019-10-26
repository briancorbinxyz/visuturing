package org.keiosu.visuturing.gui.dialogs;

import org.keiosu.visuturing.core.TuringMachine;
import org.keiosu.visuturing.gui.panels.EditTransitionsPanel;
import java.awt.Frame;
import java.util.List;
import java.util.Vector;

public class EditTransitionsDialog extends AbstractDialog {
  private EditTransitionsPanel transitionEditor;

  public EditTransitionsDialog(Frame var1, TuringMachine var2) {
    super(var1, "Edit Transition Table");
    this.transitionEditor = new EditTransitionsPanel(var2);
    this.init(this.transitionEditor);
  }

  public List getTransitions() {
    return this.transitionEditor.getTransitions();
  }
}
