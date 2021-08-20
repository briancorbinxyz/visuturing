package org.keiosu.visuturing.gui.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.util.Objects.requireNonNull;

public abstract class AbstractDialog extends JDialog implements ActionListener {
  private static final int BUTTON_PANEL_HEIGHT = 35;
  private static final int MARGIN = 10;
  private static final String CANCEL_BUTTON = "Cancel";
  private static final String OKAY_BUTTON = "Okay";
  private JPanel buttonPanel;
  boolean cancelled = true;

  public AbstractDialog(Frame owner, String title) {
    super(owner, title, true);
    buttonPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING, 5, 5));
    pack();
  }

  protected void init(JPanel panel) {
    Container contentPane = getContentPane();
    JLayeredPane pane = new JLayeredPane();
    contentPane.add(pane);
    pane.setPreferredSize(
        new Dimension(
            (int) panel.getPreferredSize().getWidth(),
            (int) panel.getPreferredSize().getHeight() + MARGIN + BUTTON_PANEL_HEIGHT));
    panel.setBounds(
        0,
        0,
        (int) panel.getPreferredSize().getWidth(),
        (int) panel.getPreferredSize().getHeight());
    pane.add(panel, JLayeredPane.DEFAULT_LAYER);
    buttonPanel.setBounds(
        0,
        (int) panel.getPreferredSize().getHeight() + MARGIN,
        (int) panel.getPreferredSize().getWidth(),
        BUTTON_PANEL_HEIGHT);
    buttonPanel.add(newButton(OKAY_BUTTON));
    buttonPanel.add(newButton(CANCEL_BUTTON));
    buttonPanel.setOpaque(false);
    pane.add(buttonPanel, JLayeredPane.DEFAULT_LAYER);
    pack();
    setResizable(false);
    setLocation(
        (int)
            ((Toolkit.getDefaultToolkit().getScreenSize().getWidth() - getSize().getWidth())
                / 2.0D),
        (int)
            ((Toolkit.getDefaultToolkit().getScreenSize().getHeight() - getSize().getHeight())
                / 2.0D));
  }

  private JButton newButton(String name) {
    var button = new JButton(name);
    button.setName(name);
    button.addActionListener(this);
    button.setActionCommand(name);
    return button;
  }

  public boolean didSucceed() {
    return !cancelled;
  }

  protected ImageIcon createImageIcon(String imageUri) {
    return new ImageIcon(requireNonNull(getClass().getClassLoader().getResource(imageUri)));
  }

  public void actionPerformed(ActionEvent event) {
    if (CANCEL_BUTTON.equals(event.getActionCommand())) {
      cancelled = true;
      setVisible(false);
    } else if (OKAY_BUTTON.equals(event.getActionCommand())) {
      cancelled = false;
      setVisible(false);
    }
  }

  void addButton(JButton button) {
    buttonPanel.add(button);
  }
}
