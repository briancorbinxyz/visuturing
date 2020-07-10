package org.keiosu.visuturing.gui.dialogs;

import org.keiosu.visuturing.core.TuringMachine;
import org.keiosu.visuturing.gui.panels.EditAlphabetPanel;
import java.awt.Frame;
import java.util.List;

public class EditAlphabetDialog extends AbstractDialog {
  private EditAlphabetPanel alphabetEditor;

  public EditAlphabetDialog(Frame frame, TuringMachine turingMachine) {
    super(frame, "Edit Alphabet");
    alphabetEditor = new EditAlphabetPanel(turingMachine);
    init(alphabetEditor);
  }

  public List<String> getAlphabet() {
    return alphabetEditor.getAlphabet();
  }
}
