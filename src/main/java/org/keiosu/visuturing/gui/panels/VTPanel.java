package org.keiosu.visuturing.gui.panels;

import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import static java.util.Objects.requireNonNull;

public class VTPanel extends JPanel {
  protected JPanel panel;

  public VTPanel(String imageName) {
    setLayout(new BorderLayout(0, 0));
    JLayeredPane var2 = new JLayeredPane();
    add(var2);
    setOpaque(false);
    var2.setOpaque(false);
    JLabel var3 = new JLabel(createImageIcon("bitmaps/" + imageName));
    var3.setBounds(0, 0, (int)var3.getPreferredSize().getWidth(), (int)var3.getPreferredSize().getHeight());
    setPreferredSize(var3.getPreferredSize());
    var2.setPreferredSize(var3.getPreferredSize());
    var2.add(var3, JLayeredPane.DEFAULT_LAYER);
    panel = new JPanel();
    panel.setOpaque(false);
    panel.setBounds(179, 40, 302, 298);
    panel.setLayout(new BorderLayout());
    var2.add(panel, JLayeredPane.POPUP_LAYER);
  }

  public ImageIcon createImageIcon(String imageUri) {
    return new ImageIcon(requireNonNull(getClass().getClassLoader().getResource(imageUri)));
  }
}
