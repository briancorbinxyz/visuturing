package org.keiosu.visuturing.gui;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyVetoException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import org.keiosu.visuturing.BrowserControl;
import org.keiosu.visuturing.core.TuringMachine;
import org.keiosu.visuturing.gui.dialogs.*;
import org.keiosu.visuturing.persistence.Persistence;
import org.keiosu.visuturing.persistence.VisuTuringFileFilter;
import org.xml.sax.SAXException;

public class VisuTuring extends JFrame implements ActionListener, MouseMotionListener {
    SplashWindow splash;
    VTFrame currentWindow;
    JDesktopPane desktop;
    JMenuItem exitMI;
    JMenuItem simulateMI;
    JFileChooser chooser;
    JLabel statusBar;
    VisuTuring.ActionGroup machineGroup;
    public static final String FILE_NEW = "New...";
    public static final String FILE_OPEN = "Open...";
    public static final String FILE_SAVE = "Save";
    public static final String FILE_SAVE_AS = "Save As...";
    public static final String FILE_SAVE_ALL = "Save All";
    public static final String FILE_EXIT = "Exit";
    public static final String FILE_VIEW_SAMPLE = "Open Sample...";
    public static final String FILE_EXPORT_JPEG = "Export Diagram...";
    public static final String FILE_EXPORT_HTML = "Export to HTML File...";
    public static final String FILE_PRINT = "Print Diagram...";
    public static final String EDIT_DESCRIPTION = "Description...";
    public static final String EDIT_STATES = "States...";
    public static final String EDIT_ALPHABET = "Alphabet...";
    public static final String EDIT_TABLE = "Table...";
    public static final String EDIT_DIAGRAM = "Transition Diagram";
    public static final String SIMULATE_HUMAN_COMPUTER = "Human Computer...";
    public static final String SIMULATE_TRANSITION = "Transition Diagram...";
    public static final String CONFIGURE_LNF_SYSTEM = "1 System Look and Feel";
    public static final String CONFIGURE_LNF_JAVA = "2 Java Look and Feel";
    public static final String HELP_USER_GUIDE = "User Guide...";
    public static final String HELP_ABOUT = "About VisuTuring...";
    public static final String HELP_VISIT_BRIAN = "Brian Corbin on the web..";
    public static final String TITLE = "VisuTuring";
    public static final String VISUTURING_DESCRIPTION =
            "This is VisuTuring - The Turing Machine AbstractSimulatorPanel created by Brian Corbin\nas part of a Final Year Project";

    public VisuTuring() {
        super(TITLE);
        this.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent var1) {
                        boolean var2 = true;
                        JInternalFrame[] var3 = VisuTuring.this.desktop.getAllFrames();

                        for (int var4 = 0; var4 < var3.length; ++var4) {
                            JInternalFrame var5 = var3[var4];
                            var5.doDefaultCloseAction();
                            if (!var5.isClosed()) {
                                var2 = false;
                            }
                        }

                        VisuTuring.this.close();
                    }
                });
        this.initWindow();
    }

    private void initWindow() {
        this.splash = new SplashWindow(this.createImageIcon("bitmaps/splash.png"), 5000);
        this.setIconImage(this.createImageIcon("bitmaps/vticon.jpg").getImage());
        Dimension var1 = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(var1);
        this.desktop = new JDesktopPane();
        this.desktop.setBorder(BorderFactory.createEtchedBorder(0));
        this.chooser = new JFileChooser();
        Container var2 = this.getContentPane();
        var2.add(this.desktop, "Center");
        this.splash.setProgressText("Setting up menus and toolbars...");
        this.machineGroup = new VisuTuring.ActionGroup();
        JMenuBar var3 = new JMenuBar();
        this.setJMenuBar(var3);
        JMenu var4 = new JMenu("File");
        var4.setMnemonic('F');
        var3.add(var4);
        JMenu var5 = new JMenu("Edit");
        var5.setMnemonic('E');
        var3.add(var5);
        JMenu var6 = new JMenu("Simulate");
        var6.setMnemonic('S');
        var3.add(var6);
        JMenu var7 = new JMenu("Configure");
        var7.setMnemonic('C');
        var3.add(var7);
        JMenu var8 = new JMenu("Help");
        var8.setMnemonic('H');
        var3.add(var8);
        JMenu var9 = new JMenu("Look & Feel");
        var7.add(var9);
        JMenuItem var10 = new JMenuItem(CONFIGURE_LNF_SYSTEM);
        var10.setName(CONFIGURE_LNF_SYSTEM);
        var10.setToolTipText("Change the GUI to match the local system");
        var10.addActionListener(this);
        var10.setAccelerator(KeyStroke.getKeyStroke(49, 2));
        var10.setMnemonic('1');
        JMenuItem var11 = new JMenuItem(CONFIGURE_LNF_JAVA);
        var11.setName(CONFIGURE_LNF_JAVA);
        var11.setToolTipText("Change the GUI to the Java Look");
        var11.addActionListener(this);
        var11.setAccelerator(KeyStroke.getKeyStroke(50, 2));
        var11.setMnemonic('2');
        var9.add(var10);
        var9.add(var11);
        JToolBar var12 = new JToolBar("Standard", 0);
        var2.add(var12, "North");
        var12.setRollover(true);
        this.addAction(
                "New",
                FILE_NEW,
                "Starts the Turing Machine creation wizard",
                var12,
                var4,
                'N',
                78,
                false);
        var12.addSeparator();
        this.addAction(
                "Open", FILE_OPEN, "Open an existing Turing Machine", var12, var4, 'O', 79, false);
        var4.addSeparator();
        this.addAction(
                (String) null,
                FILE_VIEW_SAMPLE,
                "Open an example Turing machine",
                var12,
                var4,
                'O',
                0,
                false);
        var4.addSeparator();
        this.addAction(
                FILE_SAVE, FILE_SAVE, "Save the current machine", var12, var4, 'S', 83, true);
        this.addAction(
                "SaveAs", FILE_SAVE_AS, "Save the current machine", var12, var4, 'A', 0, true);
        this.addAction(
                "SaveAll", FILE_SAVE_ALL, "Save all open machines", var12, var4, 'l', 0, true);
        var4.addSeparator();
        var12.addSeparator();
        this.addAction("Export", FILE_EXPORT_JPEG, "Export the diagram", var12, var4, 'E', 0, true);
        this.addAction(
                "WebComponent",
                FILE_EXPORT_HTML,
                "Export Turing Machine in HTML format",
                var12,
                var4,
                'H',
                0,
                true);
        var12.addSeparator();
        var4.addSeparator();
        this.addAction("Print", FILE_PRINT, "Print diagram", var12, var4, 'H', 0, true);
        var12.addSeparator();
        this.addAction(
                "EditDescription",
                EDIT_DESCRIPTION,
                "Edit the description for this Turing Machine",
                var12,
                var5,
                'd',
                0,
                true);
        this.addAction(
                "EditStates",
                EDIT_STATES,
                "Edit the states this machine",
                var12,
                var5,
                's',
                0,
                true);
        this.addAction(
                "EditAlphabet",
                EDIT_ALPHABET,
                "Edit the alphabet for this machine",
                var12,
                var5,
                'a',
                0,
                true);
        this.addAction(
                "EditTransitions",
                EDIT_TABLE,
                "Edit the transition table for this machine",
                var12,
                var5,
                'T',
                84,
                true);
        var5.addSeparator();
        this.addAction(
                (String) null,
                EDIT_DIAGRAM,
                "Edit the transition diagram for this machine",
                var12,
                var5,
                'd',
                68,
                true);
        var12.addSeparator();
        this.addAction(
                (String) null,
                SIMULATE_HUMAN_COMPUTER,
                "Simulate using the human computer",
                var12,
                var6,
                'H',
                72,
                true);
        this.addAction(
                "Play",
                SIMULATE_TRANSITION,
                "Simulate using VisuTuring® Transition Machine",
                var12,
                var6,
                'T',
                0,
                true);
        var12.addSeparator();
        this.addAction("Help", HELP_USER_GUIDE, "View the User Guide", var12, var8, 'U', 0, false);
        this.addAction(
                "BrianCorbinTheAuthor",
                HELP_VISIT_BRIAN,
                "Find out more about the author",
                (JToolBar) null,
                var8,
                'F',
                0,
                false);
        var8.addSeparator();
        this.addAction(
                "About", HELP_ABOUT, "About VisuTuring", (JToolBar) null, var8, 'A', 0, false);
        var4.addSeparator();
        this.exitMI = new JMenuItem(FILE_EXIT, 120);
        this.exitMI.setName(FILE_EXIT);
        this.exitMI.setAccelerator(KeyStroke.getKeyStroke(88, 2));
        this.exitMI.addActionListener(this);
        var4.add(this.exitMI);
        this.splash.complete();
        JPanel var16 = new JPanel(new FlowLayout(0, 5, 5));
        var16.setBorder(BorderFactory.createEtchedBorder(0));
        this.statusBar = new JLabel(" ");
        this.statusBar.setHorizontalTextPosition(2);
        var16.add(this.statusBar);
        var2.add(var16, "South");
        this.setVisible(true);
        VisuTuringFileFilter var17 = new VisuTuringFileFilter();
        var17.addExtension("vt");
        var17.addExtension("xml");
        var17.setDescription("VisuTuring Files (*.vt, *.xml)");
        this.chooser.setFileFilter(var17);
        this.machineGroup.setEnabled(false);
        StartupWizard var18 = new StartupWizard(this);
        var18.setVisible(true);
    }

    void setStatus(String var1) {
        this.statusBar.setText(var1);
    }

    JButton addAction(String var1, String var2, String var3, JToolBar var4, boolean var5) {
        JButton var6;
        if (var1 != null) {
            var6 = new JButton(this.createImageIcon("buttons/" + var1 + "16.gif"));
        } else {
            var6 = new JButton();
        }

        var6.addActionListener(this);
        var6.setName(var2);
        var6.setToolTipText(var3);
        var6.addMouseMotionListener(this);
        var4.add(var6);
        if (var5) {
            this.machineGroup.add(var6);
        }

        return var6;
    }

    JButton addAction(
            String var1,
            String var2,
            String var3,
            JToolBar var4,
            JMenu var5,
            char var6,
            int var7,
            boolean var8) {
        JButton var9 = new JButton();
        if (var1 != null && var4 != null) {
            var9 = this.addAction(var1, var2, var3, var4, var8);
        }

        JMenuItem var10 = new JMenuItem(var2);
        var10.setMnemonic(var6);
        var10.addActionListener(this);
        var10.setToolTipText(var3);
        var10.addMouseMotionListener(this);
        var10.setName(var2);
        if (var8) {
            this.machineGroup.add(var10);
        }

        if (var1 != null) {
            var10.setIcon(this.createImageIcon("buttons/" + var1 + "16.gif"));
        }

        if (var7 != 0) {
            var10.setAccelerator(KeyStroke.getKeyStroke(var7, 2));
        }

        var5.add(var10);
        return var9;
    }

    void close() {
        this.dispose();
        System.exit(0);
    }

    public void actionPerformed(ActionEvent var1) {
        if (var1.getSource() instanceof JComponent) {
            TuringMachine var2 = null;
            if (this.desktop.getSelectedFrame() instanceof VTFrame) {
                this.currentWindow = (VTFrame) this.desktop.getSelectedFrame();
                var2 = this.currentWindow.getTuringMachine();
            }

            JComponent var3 = (JComponent) var1.getSource();
            String var4 = var3.getName();
            if (var4.equals(FILE_EXIT)) {
                this.close();
            } else if (!var4.equals(EDIT_STATES) && !var4.equals("Edit states")) {
                if (!var4.equals(EDIT_ALPHABET) && !var4.equals("Edit the alphabet")) {
                    if (var4.equals(EDIT_TABLE)) {
                        if (var2 != null) {
                            EditTransitionsDialog var12 = new EditTransitionsDialog(this, var2);
                            var12.setVisible(true);
                            if (var12.didSucceed()) {
                                var2.setTransitions(var12.getTransitions());
                            }
                        }
                    } else {
                        int var13;
                        if (var4.equals(EDIT_DIAGRAM)) {
                            if (var2 != null) {
                                if (!var2.hasDiagram()) {
                                    var13 =
                                            JOptionPane.showConfirmDialog(
                                                    this,
                                                    "This Turing Machine has no diagram.\n\nYou must first generate a diagram based on the current\nTuring Machine transition table to be able to edit it to ensure consistency.\nWould you like VisuTuring to do this for you now?",
                                                    TITLE,
                                                    1,
                                                    3);
                                    if (var13 != 0) {
                                        return;
                                    }

                                    var2.generateDiagram();
                                }

                                if (var2 != null) {
                                    this.currentWindow.displayDiagram();
                                }
                            }
                        } else if (var4.equals(SIMULATE_TRANSITION)) {
                            if (var2 != null) {
                                this.currentWindow.displaySimulation();
                            }
                        } else if (var4.equals(SIMULATE_HUMAN_COMPUTER)) {
                            if (var2 != null) {
                                this.currentWindow.displayHuman();
                            }
                        } else if (!var4.equals(EDIT_DESCRIPTION)
                                && !var4.equals("Edit the description of this Turing Machine")) {
                            if (var4.equals(FILE_NEW)) {
                                this.createNewMachine();
                            } else if (var4.equals(FILE_SAVE)) {
                                this.saveCurrent();
                            } else if (var4.equals(FILE_SAVE_AS)) {
                                this.saveAs();
                            } else if (var4.equals(FILE_SAVE_ALL)) {
                                this.saveAll();
                            } else if (var4.equals(FILE_VIEW_SAMPLE)) {
                                SampleDialog sampleDialog = new SampleDialog();
                                sampleDialog.setVisible(true);
                                File selectedFile = sampleDialog.getSelectedFile();
                                if (sampleDialog.didSucceed() && selectedFile != null) {
                                    this.open(selectedFile);
                                }
                            } else if (var4.equals(FILE_OPEN)) {
                                this.chooser.setCurrentDirectory(new File("My Turing Machines"));
                                var13 = this.chooser.showOpenDialog(this);
                                if (var13 == 0
                                        && !this.isOpen(
                                                this.chooser.getSelectedFile().toString())) {
                                    this.open(this.chooser.getSelectedFile());
                                    this.setStatus("opened.");
                                }
                            } else if (var4.equals(FILE_PRINT)) {
                                this.currentWindow.printDiagram();
                            } else {
                                int var7;
                                JFileChooser var15;
                                VisuTuringFileFilter var17;
                                if (var4.equals(FILE_EXPORT_JPEG)) {
                                    var17 = new VisuTuringFileFilter();
                                    var17.addExtension("jpeg");
                                    var17.addExtension("jpg");
                                    var17.setDescription("Images Files (*.jpg, *.jpeg)");
                                    var15 = new JFileChooser();
                                    var15.setCurrentDirectory(new File("My Diagrams"));
                                    var15.setFileFilter(var17);
                                    var7 = var15.showSaveDialog(this);
                                    if (var7 == 0) {
                                        this.currentWindow.exportToJPEG(var15.getSelectedFile());
                                        this.setStatus("exported successfully.");
                                        JOptionPane.showMessageDialog(
                                                this, "Exported Successfully");
                                    }
                                } else if (var4.equals(FILE_EXPORT_HTML)) {
                                    try {
                                        var17 = new VisuTuringFileFilter();
                                        var17.addExtension("html");
                                        var17.addExtension("htm");
                                        var17.setDescription("HTML Files (*.htm, *.html)");
                                        var15 = new JFileChooser();
                                        var15.setCurrentDirectory(new File("web"));
                                        var15.setFileFilter(var17);
                                        var7 = var15.showSaveDialog(this);
                                        if (var7 == 0) {
                                            Persistence.exportToHTML(
                                                    var2,
                                                    var15.getSelectedFile().getAbsolutePath());
                                            this.setStatus("exported successfully.");
                                            JOptionPane.showMessageDialog(
                                                    this, "Exported Successfully");
                                        }
                                    } catch (Exception var10) {
                                        this.setStatus("Failed on export.");
                                        JOptionPane.showMessageDialog(this, "Export Failed.");
                                        var10.printStackTrace();
                                    }
                                } else if (var4.equals(
                                        "Generate a new diagram based on transition table")) {
                                    if (var2.hasDiagram()) {
                                        var13 =
                                                JOptionPane.showConfirmDialog(
                                                        this,
                                                        "Are you sure you wish to regenerate the Turing Machine diagram?",
                                                        TITLE,
                                                        1,
                                                        3);
                                        if (var13 != 0) {
                                            return;
                                        }
                                    }

                                    var2.generateDiagram();
                                    var2.setHasDiagram(true);
                                    this.currentWindow.repaint();
                                } else if (var4.equals(CONFIGURE_LNF_SYSTEM)) {
                                    try {
                                        this.setStatus("Setting Local System Look & Feel...");
                                        UIManager.setLookAndFeel(
                                                UIManager.getSystemLookAndFeelClassName());
                                        SwingUtilities.updateComponentTreeUI(this);
                                        this.setStatus("Look & Feel Set.");
                                    } catch (Exception var9) {
                                        JOptionPane.showMessageDialog(
                                                this,
                                                "Oops! There was an error setting look and feel",
                                                TITLE,
                                                0,
                                                (Icon) null);
                                    }
                                } else if (var4.equals(CONFIGURE_LNF_JAVA)) {
                                    try {
                                        this.setStatus("Setting Java Look & Feel...");
                                        UIManager.setLookAndFeel(
                                                UIManager.getCrossPlatformLookAndFeelClassName());
                                        SwingUtilities.updateComponentTreeUI(this);
                                        this.setStatus("Look & Feel Set.");
                                    } catch (Exception var8) {
                                        JOptionPane.showMessageDialog(
                                                this,
                                                "Oops! There was an error setting look and feel",
                                                TITLE,
                                                0,
                                                (Icon) null);
                                    }
                                } else if (var4.equals(HELP_ABOUT)) {
                                    this.splash =
                                            new SplashWindow(
                                                    this.createImageIcon("bitmaps/splash.png"),
                                                    7000);
                                    this.splash.complete();
                                    this.splash.setProgressText(VISUTURING_DESCRIPTION);
                                } else if (var4.equals(HELP_VISIT_BRIAN)) {
                                    this.visitBrian();
                                } else if (var4.equals(HELP_USER_GUIDE)) {
                                    new JHelpFrame();
                                }
                            }
                        } else if (var2 != null) {
                            EditDescriptionDialog var14 = new EditDescriptionDialog(this, var2);
                            var14.setVisible(true);
                            if (var14.didSucceed()) {
                                var2.setDescription(var14.getDescription());
                                var2.setName(var14.getName());
                            }
                        }
                    }
                } else if (var2 != null) {
                    EditAlphabetDialog var11 = new EditAlphabetDialog(this, var2);
                    var11.setVisible(true);
                    if (var11.didSucceed()) {
                        var2.setAlphabet(var11.getAlphabet());
                    }
                }
            } else if (var2 != null) {
                EditStatesDialog var5 = new EditStatesDialog(this, var2);
                var5.setVisible(true);
                if (var5.didSucceed()) {
                    var2.setStates(var5.getStates());
                }
            }
        }

        if (this.currentWindow != null) {
            this.currentWindow.repaint();
        }

        if (this.desktop.getAllFrames().length > 0) {
            this.machineGroup.setEnabled(true);
        } else {
            this.machineGroup.setEnabled(false);
        }
    }

    public void open(File var1) {
        try {
            VTFrame var2 = this.openWindow(Persistence.load(var1));
            var2.setFileName(var1.toString());
            var2.setTitle(var1.getName());
            this.setStatus("opened.");
        } catch (SAXException var3) {
            JOptionPane.showMessageDialog(
                    this, "Error! Invalid VisuTuring File!", TITLE, 0, (Icon) null);
        } catch (Exception var4) {
            JOptionPane.showMessageDialog(
                    this, "There was an error loading the file.", TITLE, 0, (Icon) null);
        }
    }

    private boolean isOpen(String var1) {
        for (int var2 = 0; var2 < this.desktop.getAllFrames().length; ++var2) {
            VTFrame var3 = (VTFrame) this.desktop.getAllFrames()[var2];
            if (var3.getFileName().equals(var1)) {
                return true;
            }
        }

        return false;
    }

    private void createNewMachine() {
        CreationWizard var1 = new CreationWizard(this);
        var1.setVisible(true);
        TuringMachine var2 = var1.getTuringMachine();
        if (var2 != null) {
            VTFrame var3 = new VTFrame(new VisuTuring.FrameHandler(), this, var2, 1);
            var3.setVisible(true);
            var3.setSize(this.desktop.getSize());
            this.currentWindow = var3;
            this.desktop.add(var3);

            try {
                var3.setSelected(true);
            } catch (PropertyVetoException var5) {
            }
        }
    }

    public static void main(String[] var0) {
        new VisuTuring();
    }

    private ImageIcon createImageIcon(String var1) {
        return new ImageIcon(this.getClass().getClassLoader().getResource(var1));
    }

    private VTFrame openWindow(TuringMachine var1) {
        VTFrame var2 = new VTFrame(new VisuTuring.FrameHandler(), this, var1, 0);
        var2.setVisible(true);
        var2.setSize(this.desktop.getSize());
        this.currentWindow = var2;
        this.desktop.add(var2);

        try {
            var2.setSelected(true);
        } catch (PropertyVetoException var4) {
        }

        return var2;
    }

    private void saveAll() {
        JInternalFrame[] var2 = this.desktop.getAllFrames();

        for (int var3 = 0; var3 < var2.length; ++var3) {
            VTFrame var4 = (VTFrame) var2[var3];
            if (!var4.getFileName().equals("-untitled-")) {
                try {
                    Persistence.save(var4.getTuringMachine(), var4.getFileName());
                    var4.getTuringMachine().setChanged(false);
                    this.setStatus("saved.");
                } catch (Exception var7) {
                    JOptionPane.showMessageDialog(
                            this, "There was an error saving the file.", TITLE, 0, (Icon) null);
                }
            } else {
                int var5 = this.chooser.showSaveDialog(this);
                if (var5 == 0) {
                    try {
                        Persistence.save(var4.getTuringMachine(), this.chooser.getSelectedFile());
                        var4.setFileName(this.chooser.getSelectedFile().toString());
                        var4.getTuringMachine().setChanged(false);
                        this.setStatus("saved.");
                    } catch (Exception var8) {
                        JOptionPane.showMessageDialog(
                                this, "There was an error saving the file.", TITLE, 0, (Icon) null);
                    }
                }
            }
        }
    }

    private void saveAs() {
        this.chooser.setCurrentDirectory(new File("My Turing Machines"));
        int var1 = this.chooser.showSaveDialog(this);
        if (var1 == 0) {
            try {
                Persistence.save(
                        this.currentWindow.getTuringMachine(), this.chooser.getSelectedFile());
                this.currentWindow.setFileName(this.chooser.getSelectedFile().toString());
                this.currentWindow.getTuringMachine().setChanged(false);
                this.setStatus("saved.");
            } catch (Exception var3) {
                JOptionPane.showMessageDialog(
                        this, "There was an error saving the file.", TITLE, 0, (Icon) null);
            }
        }
    }

    private void saveCurrent() {
        if (!this.currentWindow.getFileName().equals("-untitled-")) {
            try {
                Persistence.save(
                        this.currentWindow.getTuringMachine(), this.currentWindow.getFileName());
                this.currentWindow.getTuringMachine().setChanged(false);
                this.setStatus("saved.");
            } catch (Exception var4) {
                JOptionPane.showMessageDialog(
                        this, "There was an error saving the file.", TITLE, 0, (Icon) null);
            }
        } else {
            this.chooser.setCurrentDirectory(new File("My Turing Machines"));
            int var1 = this.chooser.showSaveDialog(this);
            if (var1 == 0) {
                try {
                    Persistence.save(
                            this.currentWindow.getTuringMachine(), this.chooser.getSelectedFile());
                    this.currentWindow.setFileName(this.chooser.getSelectedFile().toString());
                    this.currentWindow.getTuringMachine().setChanged(false);
                    this.setStatus("saved.");
                } catch (Exception var3) {
                    JOptionPane.showMessageDialog(
                            this, "There was an error saving the file.", TITLE, 0, (Icon) null);
                }
            }
        }
    }

    public void mouseDragged(MouseEvent var1) {}

    public void mouseMoved(MouseEvent var1) {
        JComponent var2 = (JComponent) var1.getSource();
        this.setStatus(var2.getToolTipText());
    }

    public void visitBrian() {
        BrowserControl.displayURL("http://www.briancorbin.co.uk");
    }

    class FrameHandler extends InternalFrameAdapter {
        FrameHandler() {}

        public void internalFrameActivated(InternalFrameEvent var1) {
            VTFrame var2 = (VTFrame) var1.getSource();
            VisuTuring.this.setTitle("VisuTuring - [" + var2.getTitle() + "]");
            if (VisuTuring.this.desktop.getAllFrames().length > 0) {
                VisuTuring.this.machineGroup.setEnabled(true);
            } else {
                VisuTuring.this.machineGroup.setEnabled(false);
            }
        }

        public void internalFrameClosing(InternalFrameEvent var1) {
            VTFrame var2 = (VTFrame) var1.getSource();
            if (var2.getTuringMachine().isChanged()) {
                int var3 =
                        JOptionPane.showConfirmDialog(
                                (Component) null,
                                "Save changes to " + var2.getFileName() + " before closing?",
                                TITLE,
                                1,
                                3);
                if (var3 == 0) {
                    VisuTuring.this.saveCurrent();
                    var2.setVisible(false);
                    var2.dispose();
                    var2 = null;
                } else if (var3 == 1) {
                    var2.setVisible(false);
                    var2.dispose();
                    var2 = null;
                } else if (var3 == 2) {
                    System.out.println("Close cancelled.");
                }
            } else {
                var2.setVisible(false);
                var2.dispose();
                var2 = null;
            }

            if (VisuTuring.this.desktop.getAllFrames().length > 0) {
                VisuTuring.this.machineGroup.setEnabled(true);
            } else {
                VisuTuring.this.machineGroup.setEnabled(false);
            }
        }
    }

    class ActionGroup {
        List group = new ArrayList();

        public ActionGroup() {}

        public void add(JComponent var1) {
            this.group.add(var1);
        }

        public void setEnabled(boolean var1) {
            for (int var2 = 0; var2 < this.group.size(); ++var2) {
                JComponent var3 = (JComponent) this.group.get(var2);
                var3.setEnabled(var1);
            }
        }
    }
}
