package VisuTuring.gui.dialogs;

import VisuTuring.gui.VisuTuring;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class StartupWizard extends VTDialog implements ActionListener {
  private StartupWizardPanel wizard;

  public StartupWizard(VisuTuring var1) {
    super(var1, "Welcome!");
    this.wizard = new StartupWizardPanel(this, var1);
    this.init(this.wizard);
  }

  public void actionPerformed(ActionEvent var1) {
    if (var1.getSource() instanceof JButton) {
      JButton var2 = (JButton)var1.getSource();
    }

    this.setVisible(false);
  }
}
