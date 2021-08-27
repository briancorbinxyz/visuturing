package org.keiosu.visuturing.gui.dialogs;

import java.awt.Frame;
import java.awt.event.ActionListener;
import java.util.List;
import org.keiosu.visuturing.core.State;
import org.keiosu.visuturing.core.TuringMachine;
import org.keiosu.visuturing.gui.panels.EditStatesPanel;

public class EditStatesDialog extends AbstractDialog implements ActionListener {
    private final EditStatesPanel stateEditor;

    public EditStatesDialog(Frame frame, TuringMachine turingMachine) {
        super(frame, "Edit States");
        this.stateEditor = new EditStatesPanel(turingMachine);
        this.init(this.stateEditor);
    }

    public List<State> getStates() {
        return this.stateEditor.getStates();
    }
}
