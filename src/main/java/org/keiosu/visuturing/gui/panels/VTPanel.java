package org.keiosu.visuturing.gui.panels;

import static java.util.Objects.requireNonNull;

import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class VTPanel extends JPanel {
  protected JPanel panel;

  public VTPanel(String imageName) {
    setLayout(new BorderLayout(0, 0));
    JLayeredPane pane = new JLayeredPane();
    add(pane);
    setOpaque(false);
    pane.setOpaque(false);
    JLabel imageLabel = new JLabel(createImageIcon("bitmaps/" + imageName));
    imageLabel.setBounds(
        0,
        0,
        (int) imageLabel.getPreferredSize().getWidth(),
        (int) imageLabel.getPreferredSize().getHeight());
    setPreferredSize(imageLabel.getPreferredSize());
    pane.setPreferredSize(imageLabel.getPreferredSize());
    pane.add(imageLabel, JLayeredPane.DEFAULT_LAYER);
    panel = new JPanel();
    panel.setOpaque(false);
    panel.setBounds(179, 40, 302, 298);
    panel.setLayout(new BorderLayout());
    pane.add(panel, JLayeredPane.POPUP_LAYER);
  }

  public ImageIcon createImageIcon(String imageUri) {
    return new ImageIcon(requireNonNull(getClass().getClassLoader().getResource(imageUri)));
  }
}
