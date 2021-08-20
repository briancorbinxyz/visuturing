package org.keiosu.visuturing.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionListener;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import org.keiosu.visuturing.gui.VisuTuring;

public class StartupWizardPanel extends JPanel {
  private VisuTuring app;
  private ActionListener listener;

  StartupWizardPanel(ActionListener actionListener, VisuTuring app) {
    super(new BorderLayout());
    this.app = app;
    listener = actionListener;
    JLabel startupLabel = new JLabel(createImageIcon("bitmaps/startup.gif"));
    JLayeredPane pane = new JLayeredPane();
    add(pane);
    startupLabel.setBounds(
        0,
        0,
        (int) startupLabel.getPreferredSize().getWidth(),
        (int) startupLabel.getPreferredSize().getHeight());
    pane.add(startupLabel, JLayeredPane.DEFAULT_LAYER);
    pane.add(hotspotRowButton("New...", 41), JLayeredPane.POPUP_LAYER);
    pane.add(hotspotRowButton("Open...", 184), JLayeredPane.POPUP_LAYER);
    pane.add(hotspotRowButton("Open Sample...", 330), JLayeredPane.POPUP_LAYER);
    setPreferredSize(startupLabel.getPreferredSize());
    pane.setPreferredSize(startupLabel.getPreferredSize());
  }

  private JButton hotspotRowButton(String name, int locationX) {
    JButton button;
    button = new JButton();
    button.setName(name);
    button.setBorderPainted(false);
    button.setContentAreaFilled(false);
    button.setBounds(locationX, 139, 129, 25);
    button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    button.addActionListener(app);
    button.addActionListener(listener);
    return button;
  }

  protected ImageIcon createImageIcon(String resourceLocation) {
    return new ImageIcon(
        Objects.requireNonNull(getClass().getClassLoader().getResource(resourceLocation)));
  }
}
