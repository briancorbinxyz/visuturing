package org.keiosu.visuturing.gui.dialogs;

import org.keiosu.visuturing.gui.VisuTuring;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionListener;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class StartupWizardPanel extends JPanel {
  private VisuTuring app;
  private ActionListener listener;

  StartupWizardPanel(ActionListener actionListener, VisuTuring app) {
    super(new BorderLayout());
    this.app = app;
    this.listener = actionListener;
    JLabel startupLabel = new JLabel(this.createImageIcon("bitmaps/startup.gif"));
    JLayeredPane pane = new JLayeredPane();
    this.add(pane);
    startupLabel.setBounds(0, 0, (int) startupLabel.getPreferredSize().getWidth(), (int) startupLabel.getPreferredSize().getHeight());
    pane.add(startupLabel, JLayeredPane.DEFAULT_LAYER);
    pane.add(this.invisibleButton("New...", 41, 139, 129, 25), JLayeredPane.POPUP_LAYER);
    pane.add(this.invisibleButton("Open...", 184, 139, 129, 25), JLayeredPane.POPUP_LAYER);
    pane.add(this.invisibleButton("Open Sample...", 330, 139, 129, 25), JLayeredPane.POPUP_LAYER);
    this.setPreferredSize(startupLabel.getPreferredSize());
    pane.setPreferredSize(startupLabel.getPreferredSize());
  }

  private JButton invisibleButton(String name, int locationX, int locationY, int width, int height) {
    JButton button;
    button = new JButton();
    button.setName(name);
    button.setBorderPainted(false);
    button.setContentAreaFilled(false);
    button.setBounds(locationX, locationY, width, height);
    button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    button.addActionListener(this.app);
    button.addActionListener(this.listener);
    return button;
  }

  protected ImageIcon createImageIcon(String resourceLocation) {
    return new ImageIcon(Objects.requireNonNull(this.getClass().getClassLoader().getResource(resourceLocation)));
  }
}
