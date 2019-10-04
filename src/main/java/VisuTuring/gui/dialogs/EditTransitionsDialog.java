package VisuTuring.gui.dialogs;

import VisuTuring.core.TuringMachine;
import VisuTuring.gui.panels.EditTransitionsPanel;
import java.awt.Frame;
import java.util.Vector;

public class EditTransitionsDialog extends VTDialog {
  private EditTransitionsPanel transitionEditor;

  public EditTransitionsDialog(Frame var1, TuringMachine var2) {
    super(var1, "Edit Transition Table");
    this.transitionEditor = new EditTransitionsPanel(var2);
    this.init(this.transitionEditor);
  }

  public Vector getTransitions() {
    return this.transitionEditor.getTransitions();
  }
}
