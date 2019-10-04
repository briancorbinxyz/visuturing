package VisuTuring.gui.dialogs;

import VisuTuring.core.TuringMachine;
import VisuTuring.gui.panels.EditStatesPanel;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.util.Vector;

public class EditStatesDialog extends VTDialog implements ActionListener {
  private EditStatesPanel stateEditor;

  public EditStatesDialog(Frame var1, TuringMachine var2) {
    super(var1, "Edit States");
    this.stateEditor = new EditStatesPanel(var2);
    this.init(this.stateEditor);
  }

  public Vector getStates() {
    return this.stateEditor.getStates();
  }
}
