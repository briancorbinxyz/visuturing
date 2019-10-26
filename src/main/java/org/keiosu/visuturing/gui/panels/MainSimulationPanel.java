package org.keiosu.visuturing.gui.panels;

import org.keiosu.visuturing.core.Symbols;
import org.keiosu.visuturing.simulator.AbstractSimulatorPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class MainSimulationPanel extends JPanel implements ActionListener, KeyListener {
  public static final Dimension DIAGRAM_SIZE = new Dimension(5000, 5000);
  public static final String STOP = "Stop the simulation";
  public static final String PLAY = "Simulate this machine on the input word";
  public static final String PAUSE = "Pause the running simulation";
  public static final String ADD_SYMBOL = "Insert a symbol from the alphabet";
  public static final String CLOSE = "Close alphabet menu";
  public static final String CLEAR = "Clear the tape";
  public static final String INCREASE_SPEED = "Increase the speed";
  public static final String DECREASE_SPEED = "Decrease the speed";
  public static final String INSTRUCTIONS = "Type Input here. Press INSERT to insert the blank character.";
  ActionListener externalListener;
  AbstractSimulatorPanel simulator;
  MainSimulationPanel.InputWordBox inputBox;
  JPopupMenu alphabetMenu;

  public MainSimulationPanel(ActionListener var1, AbstractSimulatorPanel var2) {
    this.simulator = var2;
    this.externalListener = var1;
    this.setLayout(new BorderLayout(0, 0));
    this.setBackground(Color.WHITE);
    JPanel var3 = new JPanel(new BorderLayout(0, 0));
    var3.setOpaque(false);
    this.add(var3, "North");
    var3.add(new JLabel(this.createImageIcon("bitmaps/simulator/title.gif")), "West");
    JPanel var4 = new JPanel(new BorderLayout(0, 0));
    var3.add(var4, "Center");
    JPanel var5 = new JPanel(new BorderLayout(0, 0));
    var5.add(new JLabel(this.createImageIcon("bitmaps/diagram/topborder.gif")));
    var5.setBackground(Color.BLACK);
    JPanel var6 = new JPanel(new BorderLayout(0, 0));
    var6.setBackground(Color.white);
    var6.add(var5, "North");
    var4.add(var6, "Center");
    var4.add(new JLabel(this.createImageIcon("bitmaps/diagram/rightcorner.gif")), "East");
    JPanel var7 = new JPanel(new BorderLayout(0, 0));
    var7.setBackground(Color.BLACK);
    var7.add(new JLabel(this.createImageIcon("bitmaps/diagram/sideborder.gif")));
    this.add(var7, "East");
    JPanel var8 = new JPanel(new BorderLayout(0, 0));
    var8.setBackground(Color.BLACK);
    var8.add(new JLabel(this.createImageIcon("bitmaps/diagram/sideborder.gif")));
    this.add(var8, "West");
    JPanel var9 = new JPanel(new BorderLayout(0, 0));
    var9.setBackground(Color.BLACK);
    var9.add(new JLabel(this.createImageIcon("bitmaps/diagram/topborder.gif")));
    this.add(var9, "South");
    JPanel var10 = new JPanel(new BorderLayout(0, 0));
    var10.setBackground(Color.WHITE);
    this.add(var10, "Center");
    JPanel var11 = new JPanel(new BorderLayout(0, 0));
    var11.setLayout(new FlowLayout(3, 20, 10));
    var11.setOpaque(false);
    var10.add(var11, "North");
    var10.add(this.simulator);
    JPanel var12 = new JPanel(new FlowLayout(3, 10, 10));
    var12.setOpaque(false);
    JPanel var13 = new JPanel(new FlowLayout(4, 10, 10));
    var13.setOpaque(false);
    this.inputBox = new MainSimulationPanel.InputWordBox(this, this);
    this.inputBox.setToolTipText("Type Input here. Press INSERT to insert the blank character.");
    var12.add(this.inputBox);
    var12.add(new MainSimulationPanel.SymbolButton());
    var13.add(this.createMediaButton("Simulate this machine on the input word", "play"));
    var13.add(this.createMediaButton("Pause the running simulation", "pause"));
    var13.add(this.createMediaButton("Stop the simulation", "stop"));
    var13.add(new MainSimulationPanel.SpeedShifter());
    var11.add(var12, "West");
    var11.add(var13, "East");
  }

  JButton createMediaButton(String var1, String var2) {
    JButton var3 = new JButton();
    var3.setCursor(new Cursor(12));
    var3.setToolTipText(var1);
    var3.setName(var1);
    var3.setOpaque(false);
    var3.setFocusPainted(false);
    var3.setBorderPainted(false);
    var3.setContentAreaFilled(false);
    var3.setIcon(this.createImageIcon("buttons/simulator/" + var2 + ".gif"));
    var3.setPreferredSize(new Dimension(52, 52));
    var3.addActionListener(this);
    var3.addActionListener(this.externalListener);
    return var3;
  }

  public ImageIcon createImageIcon(String var1) {
    return new ImageIcon(this.getClass().getClassLoader().getResource(var1));
  }

  public void actionPerformed(ActionEvent var1) {
    if (var1.getSource() instanceof JComponent) {
      JComponent var2 = (JComponent)var1.getSource();
      String var3 = var2.getName();
      if (var3.equals("Simulate this machine on the input word")) {
        if (this.inputBox.getText() != null) {
          this.simulator.setInputWord(this.inputBox.getText());
        } else {
          this.simulator.setInputWord("");
        }

        if (this.isAcceptableWord(this.inputBox.getText())) {
          this.simulator.play();
        }
      } else if (var3.equals("Stop the simulation")) {
        this.simulator.stop();
      } else if (var3.equals("Increase the speed")) {
        this.simulator.increaseSpeed();
      } else if (var3.equals("Decrease the speed")) {
        this.simulator.decreaseSpeed();
      } else if (var3.equals("Pause the running simulation")) {
        this.simulator.pause();
      } else if (var3.equals("Insert a symbol from the alphabet")) {
        Point var4 = var2.getLocation();
        SwingUtilities.convertPointToScreen(var4, var2);
        this.popupAlphabet(var4);
      }
    }

  }

  private boolean isAcceptableWord(String var1) {
    List var2 = this.simulator.getMachine().getAlphabet();
    boolean var3 = true;

    for(int var4 = 0; var4 < var1.length(); ++var4) {
      boolean var5 = false;
      char var6 = var1.charAt(var4);

      for(int var7 = 0; var7 < var2.size(); ++var7) {
        String var8 = (String)var2.get(var7);
        if (var8.charAt(0) == var6) {
          var5 = true;
          break;
        }
      }

      if (!var5) {
        var3 = false;
        break;
      }
    }

    if (!var3) {
      JOptionPane.showMessageDialog(this, "Invalid Input.\n\nOnly symbols from the alphabet may be     \nused in the input word. \n\nClick the ADD SYMBOL button for a list of \nvalid symbols.", "Invalid Input", 0);
    }

    return var3;
  }

  private void popupAlphabet(Point var1) {
    if (this.alphabetMenu != null) {
      this.alphabetMenu.setVisible(false);
      this.alphabetMenu = null;
    }

    this.alphabetMenu = new JPopupMenu("Alphabet");
    List var2 = this.simulator.getMachine().getAlphabet();
    Font var3 = new Font("Helvetica", 0, 14);
    ActionListener var4 = new ActionListener() {
      public void actionPerformed(ActionEvent var1) {
        String var2 = ((Component)var1.getSource()).getName();
        if (var2.equals("Close alphabet menu")) {
          MainSimulationPanel.this.alphabetMenu.setVisible(false);
        } else if (var2.equals("Clear the tape")) {
          MainSimulationPanel.this.inputBox.setText("");
        } else {
          MainSimulationPanel.this.inputBox.setText(MainSimulationPanel.this.inputBox.getText().substring(0, MainSimulationPanel.this.inputBox.getField().getCaretPosition()) + var2 + MainSimulationPanel.this.inputBox.getText().substring(MainSimulationPanel.this.inputBox.getField().getCaretPosition(), MainSimulationPanel.this.inputBox.getField().getText().length()));
        }
      }
    };
    JMenuItem var5 = this.alphabetMenu.add("clear");
    var5.setName("Clear the tape");
    var5.addActionListener(var4);
    this.alphabetMenu.addSeparator();

    for(int var6 = 0; var6 < var2.size(); ++var6) {
      String var7 = (String)var2.get(var6);
      if (var7.charAt(0) != 8883) {
        JMenuItem var8 = new JMenuItem(var7);
        var8.setFont(var3);
        var8.setName(var7);
        var8.addActionListener(var4);
        this.alphabetMenu.add(var8);
      }
    }

    this.alphabetMenu.addSeparator();
    JMenuItem var9 = this.alphabetMenu.add("close");
    var9.setName("Close alphabet menu");
    var9.addActionListener(var4);
    this.alphabetMenu.setLocation(var1);
    this.alphabetMenu.setVisible(true);
  }

  public void refresh() {
    this.simulator.refresh();
  }

  public void keyTyped(KeyEvent var1) {
  }

  public void keyReleased(KeyEvent var1) {
  }

  public void keyPressed(KeyEvent var1) {
    if (var1.getKeyCode() == 155) {
      this.inputBox.setText(this.inputBox.getText().substring(0, this.inputBox.getField().getCaretPosition()) + Symbols.SPACE + this.inputBox.getText().substring(this.inputBox.getField().getCaretPosition(), this.inputBox.getField().getText().length()));
    }

  }

  public void setVisible(boolean var1) {
    super.setVisible(var1);
    if (this.alphabetMenu != null) {
      this.alphabetMenu.setVisible(false);
      this.alphabetMenu = null;
    }

  }

  public class SpeedShifter extends JPanel {
    JTextField inputWord;

    public SpeedShifter() {
      super(new BorderLayout(0, 0));
      JLabel var2 = new JLabel(MainSimulationPanel.this.createImageIcon("bitmaps/simulator/speed.gif"));
      var2.setBounds(0, 0, (int)var2.getPreferredSize().getWidth(), (int)var2.getPreferredSize().getHeight());
      JButton var3 = MainSimulationPanel.this.createMediaButton("Increase the speed", "increase");
      var3.setBounds(14, 18, 22, 22);
      JButton var4 = MainSimulationPanel.this.createMediaButton("Decrease the speed", "decrease");
      var4.setBounds(56, 18, 22, 22);
      JLayeredPane var5 = new JLayeredPane();
      var5.setPreferredSize(var2.getPreferredSize());
      this.add(var5);
      var5.add(var2, JLayeredPane.DEFAULT_LAYER);
      var5.add(var3, JLayeredPane.POPUP_LAYER);
      var5.add(var4, JLayeredPane.POPUP_LAYER);
      this.setPreferredSize(var5.getPreferredSize());
    }
  }

  public class InputWordBox extends JPanel {
    JTextField inputWord;

    public void setToolTipText(String var1) {
      super.setToolTipText(var1);
      this.inputWord.setToolTipText(var1);
    }

    public InputWordBox(ActionListener var2, KeyListener var3) {
      super(new BorderLayout(0, 0));
      JLabel var4 = new JLabel(MainSimulationPanel.this.createImageIcon("bitmaps/simulator/inputw.gif"));
      var4.setBounds(0, 0, (int)var4.getPreferredSize().getWidth(), (int)var4.getPreferredSize().getHeight());
      this.inputWord = new JTextField("");
      this.inputWord.setFont(new Font("monospaced", 0, 16));
      this.inputWord.setBounds(21, 17, 260, 23);
      this.inputWord.setName("Simulate this machine on the input word");
      this.inputWord.addActionListener(var2);
      this.inputWord.addKeyListener(var3);
      JLayeredPane var5 = new JLayeredPane();
      var5.setPreferredSize(var4.getPreferredSize());
      this.add(var5);
      var5.add(var4, JLayeredPane.DEFAULT_LAYER);
      var5.add(this.inputWord, JLayeredPane.POPUP_LAYER);
      this.setPreferredSize(var5.getPreferredSize());
    }

    public String getText() {
      return this.inputWord.getText();
    }

    public JTextField getField() {
      return this.inputWord;
    }

    public void setText(String var1) {
      this.inputWord.setText(var1);
      this.inputWord.setCaretPosition(var1.length());
    }
  }

  public class SymbolButton extends JPanel {
    public SymbolButton() {
      super(new BorderLayout(0, 0));
      JLabel var2 = new JLabel(MainSimulationPanel.this.createImageIcon("bitmaps/simulator/alphabet.gif"));
      var2.setBounds(0, 0, (int)var2.getPreferredSize().getWidth(), (int)var2.getPreferredSize().getHeight());
      JButton var3 = MainSimulationPanel.this.createMediaButton("Insert a symbol from the alphabet", "addsymbol");
      var3.setBounds(7, 19, 78, 18);
      JLayeredPane var4 = new JLayeredPane();
      var4.setPreferredSize(var2.getPreferredSize());
      this.add(var4);
      var4.add(var2, JLayeredPane.DEFAULT_LAYER);
      var4.add(var3, JLayeredPane.POPUP_LAYER);
      this.setPreferredSize(var4.getPreferredSize());
    }
  }
}
