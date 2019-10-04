package VisuTuring.gui.panels;

import VisuTuring.diagram.DiagramEditor;
import VisuTuring.gui.dialogs.VTDialog;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DiagramPrinter extends VTDialog implements ChangeListener, KeyListener {
  private PrintableDiagram diagram;
  private OrientationGraphic og;
  private PreviewGraphic pg;
  private Dimension size;
  private JRadioButton portraitRB;
  private JRadioButton landscapeRB;
  private JRadioButton fitRB;
  private JRadioButton customRB;
  private JTextField widthTF;
  private JTextField heightTF;

  public DiagramPrinter(DiagramEditor var1) {
    super((Frame)null, "Print Options");
    this.diagram = new PrintableDiagram(var1);
    this.size = var1.getExtents();
    JPanel var2 = new JPanel(new BorderLayout());
    JLayeredPane var3 = new JLayeredPane();
    JPanel var4 = new JPanel(new BorderLayout());
    var4.setBorder(BorderFactory.createTitledBorder("Preview"));
    var4.setPreferredSize(new Dimension(400, 300));
    var4.setBounds(230, 10, 200, 190);
    JPanel var5 = new JPanel(new FlowLayout(0));
    var5.setBorder(BorderFactory.createTitledBorder("Orientation"));
    var5.setBounds(10, 120, 170, 80);
    ButtonGroup var6 = new ButtonGroup();
    this.portraitRB = new JRadioButton("Portrait", true);
    this.landscapeRB = new JRadioButton("Landscape", false);
    var6.add(this.portraitRB);
    var6.add(this.landscapeRB);
    this.portraitRB.addChangeListener(this);
    this.landscapeRB.addChangeListener(this);
    this.portraitRB.setBounds(70, 140, 94, 16);
    this.landscapeRB.setBounds(70, 165, 94, 16);
    var3.add(this.portraitRB);
    var3.add(this.landscapeRB);
    JPanel var7 = new JPanel(new FlowLayout(0));
    var7.setBorder(BorderFactory.createTitledBorder("Size"));
    ButtonGroup var8 = new ButtonGroup();
    this.fitRB = new JRadioButton("Fit to Page", false);
    this.customRB = new JRadioButton("Custom:", true);
    this.fitRB.setBounds(10, 10, 160, 16);
    this.customRB.setBounds(10, 35, 160, 16);
    var8.add(this.fitRB);
    var8.add(this.customRB);
    var3.add(this.fitRB);
    var3.add(this.customRB);
    this.fitRB.addChangeListener(this);
    this.customRB.addChangeListener(this);
    JLabel var9 = new JLabel("Width:");
    JLabel var10 = new JLabel("Height:");
    var9.setBounds(10, 60, 80, 16);
    var10.setBounds(10, 85, 80, 16);
    var3.add(var9);
    var3.add(var10);
    this.widthTF = new JTextField("" + this.size.getWidth());
    this.heightTF = new JTextField("" + this.size.getHeight());
    this.widthTF.setBounds(90, 60, 60, 16);
    this.heightTF.setBounds(90, 85, 60, 16);
    this.widthTF.addKeyListener(this);
    this.heightTF.addKeyListener(this);
    var3.add(this.widthTF);
    var3.add(this.heightTF);
    JLabel var11 = new JLabel("px");
    JLabel var12 = new JLabel("px");
    var11.setBounds(155, 60, 20, 16);
    var12.setBounds(155, 85, 20, 16);
    var3.add(var11);
    var3.add(var12);
    this.pg = new PreviewGraphic(this.isPortrait(), this);
    this.pg.setBounds(240, 30, 180, 150);
    this.og = new OrientationGraphic(this.portraitRB.isSelected());
    this.og.setBounds(22, 135, 35, 45);
    var3.add(this.pg);
    var3.add(this.og);
    var3.add(var5, JLayeredPane.DEFAULT_LAYER);
    var3.add(var7, JLayeredPane.DEFAULT_LAYER);
    var3.add(var4, JLayeredPane.DEFAULT_LAYER);
    var3.setPreferredSize(new Dimension(440, 210));
    var2.setPreferredSize(var3.getPreferredSize());
    var2.add(var3);
    this.init(var2);
  }

  void updateGraphics() {
    this.og.setPortrait(this.portraitRB.isSelected());
    this.og.repaint();
    this.pg.setPortrait(this.portraitRB.isSelected());
    this.pg.repaint();
    if (this.fitRB.isSelected()) {
      this.widthTF.setEnabled(false);
      this.heightTF.setEnabled(false);
    } else {
      this.widthTF.setEnabled(true);
      this.heightTF.setEnabled(true);
    }

  }

  public void stateChanged(ChangeEvent var1) {
    this.updateGraphics();
  }

  public void printIt() {
    PrinterJob var1 = PrinterJob.getPrinterJob();
    Book var2 = new Book();
    PageFormat var3 = var1.defaultPage();
    this.diagram.setPrintSize(this.size);
    this.diagram.setFitToPage(this.isFitToPage());
    if (this.portraitRB.isSelected()) {
      var3.setOrientation(1);
    } else {
      var3.setOrientation(2);
    }

    var2.append(this.diagram, var3);
    var1.setPageable(var2);
    if (var1.printDialog()) {
      try {
        var1.print();
      } catch (Exception var5) {
        var5.printStackTrace();
      }
    }

  }

  public void setPrintSize(Dimension var1) {
    this.size = var1;
  }

  public boolean isFitToPage() {
    return this.fitRB.isSelected();
  }

  public boolean isPortrait() {
    return this.portraitRB.isSelected();
  }

  public Dimension getPrintSize() {
    return this.size;
  }

  public void keyTyped(KeyEvent var1) {
  }

  public void keyPressed(KeyEvent var1) {
  }

  public void keyReleased(KeyEvent var1) {
    double var2;
    try {
      var2 = Double.parseDouble(this.widthTF.getText());
    } catch (NumberFormatException var8) {
      var2 = 1.0D;
    }

    double var4;
    try {
      var4 = Double.parseDouble(this.heightTF.getText());
    } catch (NumberFormatException var7) {
      var4 = 1.0D;
    }

    this.size.setSize(var2, var4);
    this.updateGraphics();
  }
}
