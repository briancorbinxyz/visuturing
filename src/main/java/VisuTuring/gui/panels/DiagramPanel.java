package VisuTuring.gui.panels;

import VisuTuring.core.TuringMachine;
import VisuTuring.mousetools.DeleteTool;
import VisuTuring.mousetools.SelectTool;
import VisuTuring.mousetools.SelfTransitionTool;
import VisuTuring.mousetools.TransitionTool;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class DiagramPanel extends JPanel implements ActionListener {
  public static final Dimension DIAGRAM_SIZE = new Dimension(5000, 5000);
  public static final String ADD_TRANSITION = "Add state changing transition";
  public static final String ADD_SELF_TRANSITION = "Add transition";
  public static final String REMOVE_TRANSITION = "Remove a transition";
  public static final String EDIT_DESCRIPTION = "Edit the description of this Turing Machine";
  public static final String EDIT_STATES = "Edit states";
  public static final String EDIT_ALPHABET = "Edit the alphabet";
  public static final String GENERATE = "Generate a new diagram based on transition table";
  public static final String CHECK_NON_DETERMINISM = "Check to see if this Turing Machine is deterministic";
  public static final String SELECT = "Select tool";
  public static final String ZOOM_IN = "Zoom in";
  public static final String ZOOM_OUT = "Zoom out";
  private ActionListener externalListener;
  private TuringMachine machine;
  private GraphicsPanel diagram;

  public DiagramPanel(ActionListener var1, TuringMachine var2) {
    this.machine = var2;
    this.externalListener = var1;
    this.setLayout(new BorderLayout(0, 0));
    this.setBackground(Color.WHITE);
    JPanel var3 = new JPanel(new BorderLayout(0, 0));
    var3.setOpaque(false);
    this.add(var3, "North");
    var3.add(new JLabel(this.createImageIcon("bitmaps/diagram/title.gif")), "West");
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
    JPanel var11 = new JPanel(new BorderLayout(0, 0));
    var11.setBackground(Color.WHITE);
    var10.add(var11, "North");
    JPanel var12 = new JPanel(new FlowLayout(3, 10, 10));
    var12.setOpaque(false);
    JPanel var13 = new JPanel(new FlowLayout(4, 10, 10));
    var13.setOpaque(false);
    var12.add(this.createMediaButton("Select tool", "select"));
    var12.add(this.createMediaButton("Add state changing transition", "addtrans"));
    var12.add(this.createMediaButton("Add transition", "addstran"));
    var12.add(this.createMediaButton("Remove a transition", "destroy"));
    var13.add(new DiagramPanel.NonDeterminismButton());
    var13.add(this.createMediaButton("Edit the description of this Turing Machine", "editdesc"));
    var13.add(this.createMediaButton("Edit states", "editsts"));
    var13.add(this.createMediaButton("Edit the alphabet", "editalph"));
    var13.add(new DiagramPanel.GenerateButton());
    var13.add(new DiagramPanel.ZoomModifier());
    var11.add(var12, "West");
    var11.add(var13, "East");
    this.diagram = new GraphicsPanel(var2);
    JScrollPane var14 = new JScrollPane(this.diagram);
    var14.setWheelScrollingEnabled(true);
    var14.setBorder(BorderFactory.createEmptyBorder());
    var10.add(var14, "Center");
    this.add(var10, "Center");
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
    var3.setIcon(this.createImageIcon("buttons/diagram/" + var2 + ".gif"));
    var3.setPreferredSize(new Dimension(52, 52));
    var3.addActionListener(this);
    var3.addActionListener(this.externalListener);
    return var3;
  }

  public ImageIcon createImageIcon(String var1) {
    return new ImageIcon(this.getClass().getClassLoader().getResource(var1));
  }

  public void actionPerformed(ActionEvent var1) {
    if (var1.getSource() instanceof JButton) {
      JButton var2 = (JButton)var1.getSource();
      String var3 = var2.getName();
      if (!var3.equals("Edit states") && !var3.equals("Edit the alphabet")) {
        if (var3.equals("Select tool")) {
          this.diagram.setTool(new SelectTool(this.diagram));
        } else if (var3.equals("Check to see if this Turing Machine is deterministic")) {
          JOptionPane.showMessageDialog(this, "This Turing Machine is " + (this.machine.isDeterministic() ? "deterministic." : "non-deterministic."), "Non-Determinism", 1);
        } else if (var3.equals("Add state changing transition")) {
          this.diagram.setTool(new TransitionTool(this.diagram));
        } else if (var3.equals("Add transition")) {
          this.diagram.setTool(new SelfTransitionTool(this.diagram));
        } else if (var3.equals("Remove a transition")) {
          this.diagram.setTool(new DeleteTool(this.diagram));
        } else if (var3.equals("Zoom in")) {
          this.diagram.zoomIn();
        } else if (var3.equals("Zoom out")) {
          this.diagram.zoomOut();
        }
      }
    }

  }

  public void exportToJPEG(File var1) {
    this.diagram.exportToJPEG(var1);
  }

  public void printDiagram() {
    this.diagram.print();
  }

  public class ZoomModifier extends JPanel {
    public ZoomModifier() {
      super(new BorderLayout(0, 0));
      JLabel var2 = new JLabel(DiagramPanel.this.createImageIcon("bitmaps/diagram/zoom.gif"));
      var2.setBounds(0, 0, (int)var2.getPreferredSize().getWidth(), (int)var2.getPreferredSize().getHeight());
      JButton var3 = DiagramPanel.this.createMediaButton("Zoom in", "increase");
      var3.setBounds(14, 18, 22, 22);
      JButton var4 = DiagramPanel.this.createMediaButton("Zoom out", "decrease");
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

  public class NonDeterminismButton extends JPanel {
    public NonDeterminismButton() {
      super(new BorderLayout(0, 0));
      JLabel var2 = new JLabel(DiagramPanel.this.createImageIcon("bitmaps/diagram/nd.gif"));
      var2.setBounds(0, 0, (int)var2.getPreferredSize().getWidth(), (int)var2.getPreferredSize().getHeight());
      JButton var3 = DiagramPanel.this.createMediaButton("Check to see if this Turing Machine is deterministic", "check");
      var3.setBounds(7, 19, 78, 18);
      JLayeredPane var4 = new JLayeredPane();
      var4.setPreferredSize(var2.getPreferredSize());
      this.add(var4);
      var4.add(var2, JLayeredPane.DEFAULT_LAYER);
      var4.add(var3, JLayeredPane.POPUP_LAYER);
      this.setPreferredSize(var4.getPreferredSize());
    }
  }

  public class GenerateButton extends JPanel {
    public GenerateButton() {
      super(new BorderLayout(0, 0));
      JLabel var2 = new JLabel(DiagramPanel.this.createImageIcon("buttons/diagram/diagram.gif"));
      var2.setBounds(0, 0, (int)var2.getPreferredSize().getWidth(), (int)var2.getPreferredSize().getHeight());
      JButton var3 = DiagramPanel.this.createMediaButton("Generate a new diagram based on transition table", "generate");
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
