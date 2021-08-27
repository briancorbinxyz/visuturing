package org.keiosu.visuturing.gui.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.keiosu.visuturing.gui.VisuTuring;

public class StartupWizard extends AbstractDialog implements ActionListener {

    public StartupWizard(VisuTuring app) {
        super(app, "Welcome!");
        init(new StartupWizardPanel(this, app));
    }

    public void actionPerformed(ActionEvent event) {
        setVisible(false);
    }
}
