package VisuTuring.gui;

import java.awt.Dimension;
import javax.swing.JDesktopPane;

public class WindowHandler {
  JDesktopPane desktop;
  public static final int CASCADE_STEP = 30;

  public WindowHandler(JDesktopPane var1) {
    this.desktop = var1;
  }

  public void cascadeWindows() {
    Dimension var1 = this.desktop.getSize();
  }

  public void tileWindowsHorizontally() {
  }

  public void tileWindowsVertically() {
  }

  public void closeSelectedWindow() {
  }

  public void closeAllWindows() {
  }

  public void gotoNextWindow() {
  }

  public void gotoPreviousWindow() {
  }
}
