package org.keiosu.visuturing.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.keiosu.visuturing.core.TuringMachine;
import org.keiosu.visuturing.gui.common.CommonGraphics;
import org.keiosu.visuturing.mousetools.TuringMachineDiagramDeleteTool;
import org.keiosu.visuturing.mousetools.TuringMachineDiagramSelectTool;
import org.keiosu.visuturing.mousetools.TuringMachineDiagramSelfTransitionTool;
import org.keiosu.visuturing.mousetools.TuringMachineDiagramTransitionTool;

public class DiagramPanel extends JPanel implements ActionListener {
    public static final String ADD_TRANSITION = "Add state changing transition";
    public static final String ADD_SELF_TRANSITION = "Add transition";
    public static final String REMOVE_TRANSITION = "Remove a transition";
    public static final String EDIT_DESCRIPTION = "Edit the description of this Turing Machine";
    public static final String EDIT_STATES = "Edit states";
    public static final String EDIT_ALPHABET = "Edit the alphabet";
    public static final String GENERATE = "Generate a new diagram based on transition table";
    public static final String CHECK_NON_DETERMINISM =
            "Check to see if this Turing Machine is deterministic";
    public static final String SELECT = "Select tool";
    public static final String ZOOM_IN = "Zoom in";
    public static final String ZOOM_OUT = "Zoom out";
    private final ActionListener externalListener;
    private final TuringMachine machine;
    private final GraphicsPanel diagram;

    public DiagramPanel(ActionListener actionListener, TuringMachine machine) {
        this.machine = machine;
        externalListener = actionListener;
        setLayout(new BorderLayout(0, 0));
        setBackground(Color.WHITE);
        JPanel titlePanel = new JPanel(new BorderLayout(0, 0));
        titlePanel.setOpaque(false);
        add(titlePanel, "North");
        titlePanel.add(new JLabel(this.createImageIcon("bitmaps/diagram/title.gif")), "West");
        JPanel rightPanel = new JPanel(new BorderLayout(0, 0));
        titlePanel.add(rightPanel, "Center");
        JPanel northPanel = new JPanel(new BorderLayout(0, 0));
        northPanel.add(new JLabel(this.createImageIcon("bitmaps/diagram/topborder.gif")));
        northPanel.setBackground(Color.BLACK);
        JPanel centerPanel = new JPanel(new BorderLayout(0, 0));
        centerPanel.setBackground(Color.white);
        centerPanel.add(northPanel, "North");
        rightPanel.add(centerPanel, "Center");
        rightPanel.add(new JLabel(this.createImageIcon("bitmaps/diagram/rightcorner.gif")), "East");
        JPanel eastPanel = new JPanel(new BorderLayout(0, 0));
        eastPanel.setBackground(Color.BLACK);
        eastPanel.add(new JLabel(this.createImageIcon("bitmaps/diagram/sideborder.gif")));
        add(eastPanel, "East");
        JPanel westPanel = new JPanel(new BorderLayout(0, 0));
        westPanel.setBackground(Color.BLACK);
        westPanel.add(new JLabel(this.createImageIcon("bitmaps/diagram/sideborder.gif")));
        add(westPanel, "West");
        JPanel southPanel = new JPanel(new BorderLayout(0, 0));
        southPanel.setBackground(Color.BLACK);
        southPanel.add(new JLabel(this.createImageIcon("bitmaps/diagram/topborder.gif")));
        add(southPanel, "South");
        JPanel northBorder = new JPanel(new BorderLayout(0, 0));
        JPanel border = new JPanel(new BorderLayout(0, 0));
        border.setBackground(Color.WHITE);
        northBorder.add(border, "North");
        JPanel mediaPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 10, 10));
        mediaPanel.setOpaque(false);
        JPanel editPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING, 10, 10));
        editPanel.setOpaque(false);
        mediaPanel.add(this.createMediaButton(SELECT, "select"));
        mediaPanel.add(this.createMediaButton(ADD_TRANSITION, "addtrans"));
        mediaPanel.add(this.createMediaButton(ADD_SELF_TRANSITION, "addstran"));
        mediaPanel.add(this.createMediaButton(REMOVE_TRANSITION, "destroy"));
        editPanel.add(new DiagramPanel.NonDeterminismButton());
        editPanel.add(this.createMediaButton(EDIT_DESCRIPTION, "editdesc"));
        editPanel.add(this.createMediaButton(EDIT_STATES, "editsts"));
        editPanel.add(this.createMediaButton(EDIT_ALPHABET, "editalph"));
        editPanel.add(new DiagramPanel.GenerateButton());
        editPanel.add(new DiagramPanel.ZoomModifier());
        border.add(mediaPanel, "West");
        border.add(editPanel, "East");
        diagram = new GraphicsPanel(machine);
        JScrollPane diagramPane = new JScrollPane(this.diagram);
        diagramPane.setWheelScrollingEnabled(true);
        diagramPane.setBorder(BorderFactory.createEmptyBorder());
        northBorder.add(diagramPane, "Center");
        add(northBorder, "Center");
    }

    JButton createMediaButton(String name, String diagramIconName) {
        JButton button = CommonGraphics.newButtonWithHand(name);
        button.setIcon(this.createImageIcon("buttons/diagram/" + diagramIconName + ".gif"));
        button.setPreferredSize(new Dimension(52, 52));
        button.addActionListener(this);
        button.addActionListener(this.externalListener);
        return button;
    }

    public ImageIcon createImageIcon(String resource) {
        return new ImageIcon(
                Objects.requireNonNull(this.getClass().getClassLoader().getResource(resource)));
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() instanceof JButton) {
            JButton source = (JButton) event.getSource();
            String sourceName = source.getName();
            if (!sourceName.equals(EDIT_STATES) && !sourceName.equals(EDIT_ALPHABET)) {
                switch (sourceName) {
                    case SELECT:
                        diagram.setTool(new TuringMachineDiagramSelectTool(this.diagram));
                        break;
                    case CHECK_NON_DETERMINISM:
                        JOptionPane.showMessageDialog(
                                this,
                                "This Turing Machine is "
                                        + (this.machine.isDeterministic()
                                                ? "deterministic."
                                                : "non-deterministic."),
                                "Non-Determinism",
                                JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case ADD_TRANSITION:
                        diagram.setTool(new TuringMachineDiagramTransitionTool(this.diagram));
                        break;
                    case ADD_SELF_TRANSITION:
                        diagram.setTool(new TuringMachineDiagramSelfTransitionTool(this.diagram));
                        break;
                    case REMOVE_TRANSITION:
                        diagram.setTool(new TuringMachineDiagramDeleteTool(this.diagram));
                        break;
                    case ZOOM_IN:
                        diagram.zoomIn();
                        break;
                    case ZOOM_OUT:
                        diagram.zoomOut();
                        break;
                }
            }
        }
    }

    public void exportToJPEG(File file) {
        diagram.exportToJPEG(file);
    }

    public void printDiagram() {
        diagram.print();
    }

    private void createDiagramButton(JComponent component, String iconName, String name) {
        JButton button = DiagramPanel.this.createMediaButton(name, iconName);
        button.setBounds(7, 19, 78, 18);
        JLayeredPane pane = new JLayeredPane();
        pane.setPreferredSize(component.getPreferredSize());
        add(pane);
        pane.add(component, JLayeredPane.DEFAULT_LAYER);
        pane.add(button, JLayeredPane.POPUP_LAYER);
        setPreferredSize(pane.getPreferredSize());
    }

    public class ZoomModifier extends JPanel {
        public ZoomModifier() {
            super(new BorderLayout(0, 0));
            JLabel zoomLabel =
                    new JLabel(DiagramPanel.this.createImageIcon("bitmaps/diagram/zoom.gif"));
            zoomLabel.setBounds(
                    0,
                    0,
                    (int) zoomLabel.getPreferredSize().getWidth(),
                    (int) zoomLabel.getPreferredSize().getHeight());
            JButton zoomIn = DiagramPanel.this.createMediaButton(ZOOM_IN, "increase");
            zoomIn.setBounds(14, 18, 22, 22);
            JButton zoomOut = DiagramPanel.this.createMediaButton(ZOOM_OUT, "decrease");
            zoomOut.setBounds(56, 18, 22, 22);
            JLayeredPane pane = new JLayeredPane();
            pane.setPreferredSize(zoomLabel.getPreferredSize());
            add(pane);
            pane.add(zoomLabel, JLayeredPane.DEFAULT_LAYER);
            pane.add(zoomIn, JLayeredPane.POPUP_LAYER);
            pane.add(zoomOut, JLayeredPane.POPUP_LAYER);
            setPreferredSize(pane.getPreferredSize());
        }
    }

    public class NonDeterminismButton extends JPanel {
        public NonDeterminismButton() {
            super(new BorderLayout(0, 0));
            JLabel nonDeterministicButton =
                    new JLabel(DiagramPanel.this.createImageIcon("bitmaps/diagram/nd.gif"));
            nonDeterministicButton.setBounds(
                    0,
                    0,
                    (int) nonDeterministicButton.getPreferredSize().getWidth(),
                    (int) nonDeterministicButton.getPreferredSize().getHeight());
            createDiagramButton(nonDeterministicButton, "check", CHECK_NON_DETERMINISM);
        }
    }

    public class GenerateButton extends JPanel {
        public GenerateButton() {
            super(new BorderLayout(0, 0));
            JLabel diagramLabel =
                    new JLabel(DiagramPanel.this.createImageIcon("buttons/diagram/diagram.gif"));
            diagramLabel.setBounds(
                    0,
                    0,
                    (int) diagramLabel.getPreferredSize().getWidth(),
                    (int) diagramLabel.getPreferredSize().getHeight());
            createDiagramButton(diagramLabel, "generate", GENERATE);
        }
    }
}
