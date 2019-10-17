package org.keiosu.visuturing.gui.dialogs;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class WelcomeDialog extends VTDialog {
  static final Dimension INFOBOX_SIZE = new Dimension(200, 150);
  JTextArea infoBox;

  public WelcomeDialog(Frame var1) {
    super(var1, "Welcome!");
    JPanel var2 = this.getMainPanel();
    var2.setOpaque(false);
    var2.setBackground(Color.WHITE);
    this.infoBox = new JTextArea("Turing machines were...");
    this.infoBox.setFont(new Font("monospaced", 0, 14));
    this.infoBox.setLineWrap(true);
    this.infoBox.setEditable(false);
    JScrollPane var3 = new JScrollPane(this.infoBox);
    this.getMainPanel().add(var3, "Center");
    JPanel var4 = new JPanel();
    var4.setLayout(new FlowLayout(1));
    this.getMainPanel().add(var4, "South");
    JButton var5 = new JButton(this.createImageIcon("buttons/openButton.gif"));
    var5.setRolloverEnabled(true);
    var5.setBorderPainted(false);
    var5.setRolloverIcon(this.createImageIcon("buttons/openButton_o.gif"));
    var5.setPressedIcon(this.createImageIcon("buttons/openButton_s.gif"));
    var5.setFocusPainted(false);
    var5.setContentAreaFilled(false);
    var5.setCursor(new Cursor(12));
    var4.add(var5);
    JButton var6 = new JButton(this.createImageIcon("buttons/createButton.gif"));
    var6.setRolloverEnabled(true);
    var6.setBorderPainted(false);
    var6.setRolloverIcon(this.createImageIcon("buttons/createButton_o.gif"));
    var6.setPressedIcon(this.createImageIcon("buttons/createButton_s.gif"));
    var6.setFocusPainted(false);
    var6.setContentAreaFilled(false);
    var6.setCursor(new Cursor(12));
    var4.add(var6);
  }
}
