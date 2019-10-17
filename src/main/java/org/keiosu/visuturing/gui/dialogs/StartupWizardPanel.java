package org.keiosu.visuturing.gui.dialogs;

import org.keiosu.visuturing.gui.VisuTuring;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class StartupWizardPanel extends JPanel {
  StartupWizardPanel.Rollover ro;
  VisuTuring mainProgram;
  ActionListener listener;

  public StartupWizardPanel(ActionListener var1, VisuTuring var2) {
    super(new BorderLayout());
    this.mainProgram = var2;
    this.listener = var1;
    JLabel var3 = new JLabel(this.createImageIcon("bitmaps/startup.gif"));
    JLayeredPane var4 = new JLayeredPane();
    this.add(var4);
    var3.setBounds(0, 0, (int)var3.getPreferredSize().getWidth(), (int)var3.getPreferredSize().getHeight());
    var4.add(var3, JLayeredPane.DEFAULT_LAYER);
    var4.add(this.invisibleButton("New...", 41, 139, 129, 25), JLayeredPane.POPUP_LAYER);
    var4.add(this.invisibleButton("Open...", 184, 139, 129, 25), JLayeredPane.POPUP_LAYER);
    var4.add(this.invisibleButton("Open Sample...", 330, 139, 129, 25), JLayeredPane.POPUP_LAYER);
    this.setPreferredSize(var3.getPreferredSize());
    var4.setPreferredSize(var3.getPreferredSize());
    this.ro = new StartupWizardPanel.Rollover();
  }

  public JButton invisibleButton(String var1, int var2, int var3, int var4, int var5) {
    JButton var6 = new JButton();
    var6.setName(var1);
    var6.setBorderPainted(false);
    var6.setContentAreaFilled(false);
    var6.setBounds(var2, var3, var4, var5);
    var6.setCursor(Cursor.getPredefinedCursor(12));
    var6.addMouseMotionListener(this.ro);
    var6.addActionListener(this.mainProgram);
    var6.addActionListener(this.listener);
    return var6;
  }

  protected ImageIcon createImageIcon(String var1) {
    return new ImageIcon(this.getClass().getClassLoader().getResource(var1));
  }

  public class Rollover extends MouseMotionAdapter {
    public Rollover() {
    }

    public void mouseMoved(MouseEvent var1) {
      System.out.println("whatappen!");
    }
  }
}
