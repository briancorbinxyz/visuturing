package VisuTuring.gui.panels;

import VisuTuring.core.TuringMachine;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class EditDescriptionPanel extends VTPanel implements ActionListener {
  JTextArea description;
  JTextField nameField;

  public EditDescriptionPanel(TuringMachine var1) {
    super("edpanel.gif");
    JLayeredPane var2 = new JLayeredPane();
    this.panel.add(var2);
    var2.setPreferredSize(this.panel.getPreferredSize());
    this.panel.setOpaque(false);
    var2.setOpaque(false);
    this.description = new JTextArea(new String(var1.getDescription()));
    this.description.setFont(new Font("Helvetica", 0, 12));
    this.description.setLineWrap(true);
    this.description.setWrapStyleWord(true);
    JScrollPane var3 = new JScrollPane(this.description);
    var3.setHorizontalScrollBarPolicy(31);
    var3.setBounds(0, 68, 285, 200);
    JLabel var4 = new JLabel("Name:");
    var4.setBounds(0, 16, 50, 20);
    this.nameField = new JTextField(var1.getName());
    this.nameField.setBounds(55, 16, 230, 20);
    JLabel var5 = new JLabel("Description:");
    var5.setBounds(0, 45, 75, 20);
    JButton var6 = new JButton("Insert ⊳ Symbol");
    var6.setName("Insert ⊳ Symbol");
    var6.setBounds(0, 275, 125, 20);
    var6.addActionListener(this);
    var6.setFont(new Font("Helvetica", 0, 11));
    JButton var7 = new JButton("Insert ⊔ Symbol");
    var7.setName("Insert ⊔ Symbol");
    var7.setBounds(160, 275, 125, 20);
    var7.addActionListener(this);
    var7.setFont(new Font("Helvetica", 0, 11));
    var2.add(var4, JLayeredPane.DEFAULT_LAYER);
    var2.add(this.nameField, JLayeredPane.DEFAULT_LAYER);
    var2.add(var5, JLayeredPane.DEFAULT_LAYER);
    var2.add(var3, JLayeredPane.DEFAULT_LAYER);
    var2.add(var6, JLayeredPane.DEFAULT_LAYER);
    var2.add(var7, JLayeredPane.DEFAULT_LAYER);
  }

  public String getDescription() {
    return this.description.getText();
  }

  public String getName() {
    return this.nameField.getText();
  }

  public void actionPerformed(ActionEvent var1) {
    if (var1.getSource() instanceof JButton) {
      JButton var2 = (JButton)var1.getSource();
      if (var2.getName().equals("Insert ⊔ Symbol")) {
        this.description.insert("⊔", this.description.getCaretPosition());
      } else if (var2.getName().equals("Insert ⊳ Symbol")) {
        this.description.insert("⊳", this.description.getCaretPosition());
      }
    }

  }
}
