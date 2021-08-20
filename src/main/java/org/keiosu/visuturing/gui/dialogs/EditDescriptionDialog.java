package org.keiosu.visuturing.gui.dialogs;

import java.awt.Frame;
import java.awt.event.ActionListener;
import org.keiosu.visuturing.core.TuringMachine;
import org.keiosu.visuturing.gui.panels.EditDescriptionPanel;

public class EditDescriptionDialog extends AbstractDialog implements ActionListener {

  private EditDescriptionPanel descriptionEditor;

  public EditDescriptionDialog(Frame frame, TuringMachine turingMachine) {
    super(frame, "Edit Description");
    this.descriptionEditor = new EditDescriptionPanel(turingMachine);
    this.init(this.descriptionEditor);
  }

  public String getDescription() {
    return this.descriptionEditor.getDescription();
  }

  public String getName() {
    return this.descriptionEditor.getName();
  }
}
