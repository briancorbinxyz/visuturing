package org.keiosu.visuturing.gui;

import java.lang.invoke.MethodHandles;
import javax.help.HelpSet;
import javax.help.JHelp;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JHelpFrame extends JFrame {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public JHelpFrame() {
    super("VisuTuring User Guide");
    try {
      ClassLoader classLoader = this.getClass().getClassLoader();
      JHelp help =
          new JHelp(
              new HelpSet(classLoader, HelpSet.findHelpSet(classLoader, "help/VTHelpSet.hs")));
      help.setCurrentID("VisuTuring.Introduction");
      this.setSize(800, 600);
      this.getContentPane().add(help);
      this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      this.setVisible(true);
    } catch (Exception e) {
      LOG.atError().setCause(e).log("Error loading help.");
    }
  }
}
