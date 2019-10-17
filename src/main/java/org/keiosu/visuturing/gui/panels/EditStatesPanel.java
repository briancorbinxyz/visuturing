package org.keiosu.visuturing.gui.panels;

import org.keiosu.visuturing.core.State;
import org.keiosu.visuturing.core.Transition;
import org.keiosu.visuturing.core.TuringMachine;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class EditStatesPanel extends VTPanel implements ActionListener, ListSelectionListener {
  JTextField stateField;
  JList stateList;
  Vector states;
  JButton removeButton;
  JButton removeAllButton;
  TuringMachine machine;
  ActionListener listener;
  int noAdditions;

  public EditStatesPanel(TuringMachine var1) {
    super("espanel.gif");
    this.states = new Vector(var1.getStates());
    this.machine = var1;
    this.initialize();
    this.noAdditions = 0;
  }

  public boolean hasTransitions(String var1) {
    Vector var2 = this.machine.getTransitions();
    boolean var3 = false;

    for(int var4 = 0; var4 < var2.size(); ++var4) {
      Transition var5 = (Transition)var2.get(var4);
      if (var5.getCurrentState().equals(var1) || var5.getNextState().equals(var1)) {
        var3 = true;
        return var3;
      }
    }

    return var3;
  }

  public void initialize() {
    if (this.states.size() <= 1) {
      this.states = new Vector();
      this.states.add(new State("s"));
      this.states.add(new State("h"));
    }

    this.stateList = new JList();
    this.stateList.setListData(this.states);
    JLayeredPane var1 = new JLayeredPane();
    this.panel.setLayout(new BorderLayout());
    this.panel.add(var1);
    var1.setPreferredSize(this.panel.getPreferredSize());
    JLabel var2 = new JLabel("State:");
    var2.setBounds(0, 16, 65, 20);
    this.stateField = new JTextField();
    this.stateField.setBounds(79, 16, 119, 20);
    JButton var3 = new JButton("Add");
    var3.setName("Add");
    var3.addActionListener(this);
    var3.setBounds(205, 16, 94, 20);
    JLabel var4 = new JLabel("States:");
    var4.setBounds(0, 49, 65, 20);
    this.stateList = new JList(this.states);
    this.stateList.addListSelectionListener(this);
    JScrollPane var5 = new JScrollPane(this.stateList);
    var5.setBounds(79, 48, 120, 170);
    this.removeButton = new JButton("Remove");
    this.removeButton.setName("Remove");
    this.removeButton.addActionListener(this);
    this.removeButton.setEnabled(false);
    this.removeButton.setBounds(205, 48, 94, 20);
    this.removeAllButton = new JButton("Remove All");
    this.removeAllButton.setName("Remove All");
    this.removeAllButton.addActionListener(this);
    this.removeAllButton.setEnabled(false);
    this.removeAllButton.setBounds(205, 76, 94, 20);
    var1.add(var2, JLayeredPane.DEFAULT_LAYER);
    var1.add(this.stateField, JLayeredPane.DEFAULT_LAYER);
    var1.add(var4, JLayeredPane.DEFAULT_LAYER);
    var1.add(var3, JLayeredPane.DEFAULT_LAYER);
    var1.add(var5, JLayeredPane.DEFAULT_LAYER);
    var1.add(this.removeAllButton, JLayeredPane.DEFAULT_LAYER);
    var1.add(this.removeButton, JLayeredPane.DEFAULT_LAYER);
  }

  public void actionPerformed(ActionEvent var1) {
    if (var1.getSource() instanceof JButton) {
      JButton var2 = (JButton)var1.getSource();
      int var5;
      if (var2.getName().equals("Add")) {
        String var3 = this.stateField.getText();
        if (var3 != null && !var3.trim().equals("")) {
          boolean var4 = false;
          var5 = 0;

          while(var5 < this.states.size() && !var4) {
            if (var3.equals(((State)this.states.get(var5++)).getName())) {
              var4 = true;
            }
          }

          if (!var4 && !var3.equals("h") && !var3.equals("s")) {
            this.states.add(new State(var3, new Point(100 * (this.noAdditions + 1), 50)));
            ++this.noAdditions;
          }

          this.stateList.setListData(this.states);
          this.stateField.setText("");
          if (!this.removeAllButton.isEnabled()) {
            this.removeAllButton.setEnabled(true);
          }
        }
      } else if (var2.getName().equals("Remove")) {
        int var6 = this.stateList.getSelectedIndex();
        if (var6 > -1) {
          if (this.hasTransitions(((State)this.states.get(var6)).getName())) {
            JOptionPane.showMessageDialog(this, "The selected state has one or more transitions.\n\nPlease delete these before removing the state.", "VisuTuring - Integrity Protect", 0);
          } else {
            this.states.remove(var6);
            this.stateList.setListData(this.states);
            this.removeButton.setEnabled(false);
          }
        }
      } else if (var2.getName().equals("Remove All")) {
        boolean var7 = false;
        Vector var8 = new Vector();

        for(var5 = 0; var5 < this.states.size(); ++var5) {
          if (!((State)this.states.get(var5)).getName().equals("s") && !((State)this.states.get(var5)).getName().equals("h")) {
            if (this.hasTransitions(((State)this.states.get(var5)).getName())) {
              var7 = true;
              var8.add((State)this.states.get(var5));
            }
          } else {
            var8.add((State)this.states.get(var5));
          }
        }

        this.states = var8;
        this.stateList.setListData(this.states);
        if (var7) {
          JOptionPane.showMessageDialog(this, "One or more of the states have transitions and were not removed.\n\nPlease delete these tranistions before removing the state(s).", "VisuTuring - Integrity Protect", 0);
        }

        this.removeAllButton.setEnabled(false);
      }
    }

  }

  public void valueChanged(ListSelectionEvent var1) {
    this.updateButtonStates();
  }

  public void updateButtonStates() {
    int var1 = this.stateList.getSelectedIndex();
    if (var1 > -1) {
      String var2 = ((State)this.states.get(var1)).getName();
      if (!var2.equals("s") && !var2.equals("h")) {
        this.removeButton.setEnabled(true);
      } else {
        this.removeButton.setEnabled(false);
      }
    }

    if (this.states.size() > 2) {
      if (!this.removeAllButton.isEnabled()) {
        this.removeAllButton.setEnabled(true);
      }
    } else if (this.removeAllButton.isEnabled()) {
      this.removeAllButton.setEnabled(false);
    }

  }

  public Vector getStates() {
    return this.states;
  }
}
