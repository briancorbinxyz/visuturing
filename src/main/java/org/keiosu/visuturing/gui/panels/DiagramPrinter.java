package org.keiosu.visuturing.gui.panels;

import org.keiosu.visuturing.diagram.DiagramEditor;
import org.keiosu.visuturing.gui.dialogs.AbstractDialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

public class DiagramPrinter extends AbstractDialog implements ChangeListener, KeyListener {
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

  public DiagramPrinter(DiagramEditor editor) {
    super(null, "Print Options");
    diagram = new PrintableDiagram(editor);
    size = editor.getExtents();
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
    portraitRB = new JRadioButton("Portrait", true);
    landscapeRB = new JRadioButton("Landscape", false);
    var6.add(this.portraitRB);
    var6.add(this.landscapeRB);
    portraitRB.addChangeListener(this);
    landscapeRB.addChangeListener(this);
    portraitRB.setBounds(70, 140, 94, 16);
    landscapeRB.setBounds(70, 165, 94, 16);
    var3.add(this.portraitRB);
    var3.add(this.landscapeRB);
    JPanel var7 = new JPanel(new FlowLayout(0));
    var7.setBorder(BorderFactory.createTitledBorder("Size"));
    ButtonGroup var8 = new ButtonGroup();
    fitRB = new JRadioButton("Fit to Page", false);
    customRB = new JRadioButton("Custom:", true);
    fitRB.setBounds(10, 10, 160, 16);
    customRB.setBounds(10, 35, 160, 16);
    var8.add(this.fitRB);
    var8.add(this.customRB);
    var3.add(this.fitRB);
    var3.add(this.customRB);
    fitRB.addChangeListener(this);
    customRB.addChangeListener(this);
    JLabel var9 = new JLabel("Width:");
    JLabel var10 = new JLabel("Height:");
    var9.setBounds(10, 60, 80, 16);
    var10.setBounds(10, 85, 80, 16);
    var3.add(var9);
    var3.add(var10);
    widthTF = new JTextField("" + size.getWidth());
    heightTF = new JTextField("" + size.getHeight());
    widthTF.setBounds(90, 60, 60, 16);
    heightTF.setBounds(90, 85, 60, 16);
    widthTF.addKeyListener(this);
    heightTF.addKeyListener(this);
    var3.add(this.widthTF);
    var3.add(this.heightTF);
    JLabel var11 = new JLabel("px");
    JLabel var12 = new JLabel("px");
    var11.setBounds(155, 60, 20, 16);
    var12.setBounds(155, 85, 20, 16);
    var3.add(var11);
    var3.add(var12);
    pg = new PreviewGraphic(this.isPortrait(), this);
    pg.setBounds(240, 30, 180, 150);
    og = new OrientationGraphic(this.portraitRB.isSelected());
    og.setBounds(22, 135, 35, 45);
    var3.add(this.pg);
    var3.add(this.og);
    var3.add(var5, JLayeredPane.DEFAULT_LAYER);
    var3.add(var7, JLayeredPane.DEFAULT_LAYER);
    var3.add(var4, JLayeredPane.DEFAULT_LAYER);
    var3.setPreferredSize(new Dimension(440, 210));
    var2.setPreferredSize(var3.getPreferredSize());
    var2.add(var3);
    init(var2);
  }

  void updateGraphics() {
    og.setPortraitOrientation(this.portraitRB.isSelected());
    og.repaint();
    pg.setPortraitOrientation(this.portraitRB.isSelected());
    pg.repaint();
    if (this.fitRB.isSelected()) {
      widthTF.setEnabled(false);
      heightTF.setEnabled(false);
    } else {
      widthTF.setEnabled(true);
      heightTF.setEnabled(true);
    }

  }

  public void stateChanged(ChangeEvent var1) {
    updateGraphics();
  }

  public void printIt() {
    PrinterJob var1 = PrinterJob.getPrinterJob();
    Book var2 = new Book();
    PageFormat var3 = var1.defaultPage();
    diagram.setPrintSize(this.size);
    diagram.setFitToPage(this.isFitToPage());
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
    size = var1;
  }

  public boolean isFitToPage() {
    return fitRB.isSelected();
  }

  public boolean isPortrait() {
    return portraitRB.isSelected();
  }

  public Dimension getPrintSize() {
    return size;
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

    size.setSize(var2, var4);
    updateGraphics();
  }
}
