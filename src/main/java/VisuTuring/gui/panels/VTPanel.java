package VisuTuring.gui.panels;

import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class VTPanel extends JPanel {
  protected JPanel panel;

  public VTPanel(String var1) {
    this.setLayout(new BorderLayout(0, 0));
    JLayeredPane var2 = new JLayeredPane();
    this.add(var2);
    this.setOpaque(false);
    var2.setOpaque(false);
    JLabel var3 = new JLabel(this.createImageIcon("bitmaps/" + var1));
    var3.setBounds(0, 0, (int)var3.getPreferredSize().getWidth(), (int)var3.getPreferredSize().getHeight());
    this.setPreferredSize(var3.getPreferredSize());
    var2.setPreferredSize(var3.getPreferredSize());
    var2.add(var3, JLayeredPane.DEFAULT_LAYER);
    this.panel = new JPanel();
    this.panel.setOpaque(false);
    this.panel.setBounds(179, 40, 302, 298);
    this.panel.setLayout(new BorderLayout());
    var2.add(this.panel, JLayeredPane.POPUP_LAYER);
  }

  public ImageIcon createImageIcon(String var1) {
    return new ImageIcon(this.getClass().getResource(var1));
  }
}
