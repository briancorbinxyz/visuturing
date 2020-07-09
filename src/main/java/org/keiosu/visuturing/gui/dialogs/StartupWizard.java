package org.keiosu.visuturing.gui.dialogs;

import org.keiosu.visuturing.gui.VisuTuring;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartupWizard extends AbstractDialog implements ActionListener {

  public StartupWizard(VisuTuring app) {
    super(app, "Welcome!");
    this.init(new StartupWizardPanel(this, app));
  }

  public void actionPerformed(ActionEvent var1) {
    this.setVisible(false);
  }
}
