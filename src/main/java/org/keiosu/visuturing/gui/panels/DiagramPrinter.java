package org.keiosu.visuturing.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.lang.invoke.MethodHandles;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.keiosu.visuturing.diagram.DiagramEditor;
import org.keiosu.visuturing.gui.dialogs.AbstractDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiagramPrinter extends AbstractDialog implements ChangeListener, KeyListener {
    private final static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final PrintableDiagram diagram;
    private final OrientationGraphic og;
    private final PreviewGraphic pg;
    private final JRadioButton portraitRB;
    private final JRadioButton fitRB;
    private final JTextField widthTF;
    private final JTextField heightTF;
    private final Dimension size;

    public DiagramPrinter(DiagramEditor editor) {
        super(null, "Print Options");
        diagram = new PrintableDiagram(editor);
        size = editor.getExtents();
        JPanel panel = new JPanel(new BorderLayout());
        JLayeredPane pane = new JLayeredPane();
        JPanel previewPanel = new JPanel(new BorderLayout());
        previewPanel.setBorder(BorderFactory.createTitledBorder("Preview"));
        previewPanel.setPreferredSize(new Dimension(400, 300));
        previewPanel.setBounds(230, 10, 200, 190);
        JPanel orientationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        orientationPanel.setBorder(BorderFactory.createTitledBorder("Orientation"));
        orientationPanel.setBounds(10, 120, 170, 80);
        ButtonGroup orientationGroup = new ButtonGroup();
        portraitRB = new JRadioButton("Portrait", true);
        JRadioButton landscapeRB = new JRadioButton("Landscape", false);
        orientationGroup.add(this.portraitRB);
        orientationGroup.add(landscapeRB);
        portraitRB.addChangeListener(this);
        landscapeRB.addChangeListener(this);
        portraitRB.setBounds(70, 140, 94, 16);
        landscapeRB.setBounds(70, 165, 94, 16);
        pane.add(this.portraitRB);
        pane.add(landscapeRB);
        JPanel sizePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sizePanel.setBorder(BorderFactory.createTitledBorder("Size"));
        ButtonGroup sizeGroup = new ButtonGroup();
        fitRB = new JRadioButton("Fit to Page", false);
        JRadioButton customRB = new JRadioButton("Custom:", true);
        fitRB.setBounds(10, 10, 160, 16);
        customRB.setBounds(10, 35, 160, 16);
        sizeGroup.add(this.fitRB);
        sizeGroup.add(customRB);
        pane.add(this.fitRB);
        pane.add(customRB);
        fitRB.addChangeListener(this);
        customRB.addChangeListener(this);
        JLabel widthLabel = new JLabel("Width:");
        JLabel heightLabel = new JLabel("Height:");
        widthLabel.setBounds(10, 60, 80, 16);
        heightLabel.setBounds(10, 85, 80, 16);
        pane.add(widthLabel);
        pane.add(heightLabel);
        widthTF = new JTextField("" + size.getWidth());
        heightTF = new JTextField("" + size.getHeight());
        widthTF.setBounds(90, 60, 60, 16);
        heightTF.setBounds(90, 85, 60, 16);
        widthTF.addKeyListener(this);
        heightTF.addKeyListener(this);
        pane.add(this.widthTF);
        pane.add(this.heightTF);
        JLabel pxLabel1 = new JLabel("px");
        JLabel pxLabel2 = new JLabel("px");
        pxLabel1.setBounds(155, 60, 20, 16);
        pxLabel2.setBounds(155, 85, 20, 16);
        pane.add(pxLabel1);
        pane.add(pxLabel2);
        pg = new PreviewGraphic(this.isPortrait(), this);
        pg.setBounds(240, 30, 180, 150);
        og = new OrientationGraphic(this.portraitRB.isSelected());
        og.setBounds(22, 135, 35, 45);
        pane.add(this.pg);
        pane.add(this.og);
        pane.add(orientationPanel, JLayeredPane.DEFAULT_LAYER);
        pane.add(sizePanel, JLayeredPane.DEFAULT_LAYER);
        pane.add(previewPanel, JLayeredPane.DEFAULT_LAYER);
        pane.setPreferredSize(new Dimension(440, 210));
        panel.setPreferredSize(pane.getPreferredSize());
        panel.add(pane);
        init(panel);
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

    public void stateChanged(ChangeEvent event) {
        updateGraphics();
    }

    public void printIt() {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        Book book = new Book();
        PageFormat pageFormat = printerJob.defaultPage();
        diagram.setPrintSize(this.size);
        diagram.setFitToPage(this.isFitToPage());
        if (this.portraitRB.isSelected()) {
            pageFormat.setOrientation(1);
        } else {
            pageFormat.setOrientation(2);
        }

        book.append(this.diagram, pageFormat);
        printerJob.setPageable(book);
        if (printerJob.printDialog()) {
            try {
                printerJob.print();
            } catch (Exception e) {
                LOG.atError().setCause(e.getCause());
            }
        }
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

    public void keyTyped(KeyEvent event) {}

    public void keyPressed(KeyEvent event) {}

    public void keyReleased(KeyEvent event) {
        double width;
        try {
            width = Double.parseDouble(this.widthTF.getText());
        } catch (NumberFormatException e) {
            width = 1.0D;
        }

        double height;
        try {
            height = Double.parseDouble(this.heightTF.getText());
        } catch (NumberFormatException e) {
            height = 1.0D;
        }

        size.setSize(width, height);
        updateGraphics();
    }
}
