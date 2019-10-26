package org.keiosu.visuturing.gui.dialogs;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public abstract class AbstractDialog extends JDialog implements ActionListener {
  private static final Font TITLE_FONT = new Font("Helvetica", 1, 20);
  private static final Color TITLE_COLOUR;
  private static final Dimension DIALOG_SIZE;
  private static final int BUTTON_PANEL_HEIGHT = 35;
  private static final int MARGIN = 10;
  protected static final String CANCEL_BUTTON = "Cancel";
  protected static final String OKAY_BUTTON = "Okay";
  private JPanel mainPanel;
  private JPanel buttonPanel;
  protected boolean cancelled;

  public AbstractDialog(Frame var1, String var2) {
    super(var1, var2, true);
    this.cancelled = true;
    this.buttonPanel = new JPanel(new FlowLayout(4, 5, 5));
    this.pack();
  }

  public AbstractDialog(Frame var1, String var2, JPanel var3) {
    this(var1, var2);
    this.buttonPanel = new JPanel(new FlowLayout(4, 5, 5));
    this.init(var3);
  }

  public void init(JPanel var1) {
    this.cancelled = true;
    Container var2 = this.getContentPane();
    JLayeredPane var3 = new JLayeredPane();
    var2.add(var3);
    var3.setPreferredSize(new Dimension((int)var1.getPreferredSize().getWidth(), (int)var1.getPreferredSize().getHeight() + 10 + 35));
    var1.setBounds(0, 0, (int)var1.getPreferredSize().getWidth(), (int)var1.getPreferredSize().getHeight());
    var3.add(var1, JLayeredPane.DEFAULT_LAYER);
    this.buttonPanel.setBounds(0, (int)var1.getPreferredSize().getHeight() + 10, (int)var1.getPreferredSize().getWidth(), 35);
    JButton var4 = new JButton("Okay");
    var4.setName("Okay");
    var4.addActionListener(this);
    JButton var5 = new JButton("Cancel");
    var5.setName("Cancel");
    var5.addActionListener(this);
    this.buttonPanel.add(var4);
    this.buttonPanel.add(var5);
    this.buttonPanel.setOpaque(false);
    var3.add(this.buttonPanel, JLayeredPane.DEFAULT_LAYER);
    this.pack();
    this.setResizable(false);
    this.setLocation((int)((Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.getSize().getWidth()) / 2.0D), (int)((Toolkit.getDefaultToolkit().getScreenSize().getHeight() - this.getSize().getHeight()) / 2.0D));
  }

  public boolean wasCancelled() {
    return this.cancelled;
  }

  public JPanel getMainPanel() {
    return this.mainPanel;
  }

  public AbstractDialog(Frame var1) {
    this(var1, "VisuTuring Dialog");
  }

  protected ImageIcon createImageIcon(String var1) {
    return new ImageIcon(this.getClass().getClassLoader().getResource(var1));
  }

  public void actionPerformed(ActionEvent var1) {
    if (var1.getSource() instanceof JButton) {
      JButton var2 = (JButton)var1.getSource();
      if (var2.getName().equals("Cancel")) {
        this.cancelled = true;
        this.setVisible(false);
      } else if (var2.getName().equals("Okay")) {
        this.cancelled = false;
        this.setVisible(false);
      }
    }

  }

  public void addButton(JButton var1) {
    this.buttonPanel.add(var1);
  }

  static {
    TITLE_COLOUR = Color.black;
    DIALOG_SIZE = new Dimension(550, 350);
  }
}
