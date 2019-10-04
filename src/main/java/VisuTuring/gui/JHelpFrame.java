package VisuTuring.gui;

import java.net.URL;
import javax.help.HelpSet;
import javax.help.JHelp;
import javax.swing.JFrame;

public class JHelpFrame extends JFrame {
  public JHelpFrame() {
    super("VisuTuring User Guide");
    JHelp var1 = null;

    try {
      ClassLoader var2 = this.getClass().getClassLoader();
      URL var3 = HelpSet.findHelpSet(var2, "help/VTHelpSet.hs");
      var1 = new JHelp(new HelpSet(var2, var3));
      var1.setCurrentID("VisuTuring.Introduction");
      this.setSize(800, 600);
      this.getContentPane().add(var1);
      this.setDefaultCloseOperation(2);
      this.setVisible(true);
    } catch (Exception var4) {
      System.out.println("Error loading help.");
      var4.printStackTrace();
    }

  }
}
