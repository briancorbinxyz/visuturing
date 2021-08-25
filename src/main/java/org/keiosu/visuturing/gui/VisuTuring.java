package org.keiosu.visuturing.gui;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import org.keiosu.visuturing.BrowserControl;
import org.keiosu.visuturing.core.TuringMachine;
import org.keiosu.visuturing.gui.dialogs.CreationWizard;
import org.keiosu.visuturing.gui.dialogs.EditAlphabetDialog;
import org.keiosu.visuturing.gui.dialogs.EditDescriptionDialog;
import org.keiosu.visuturing.gui.dialogs.EditStatesDialog;
import org.keiosu.visuturing.gui.dialogs.EditTransitionsDialog;
import org.keiosu.visuturing.gui.dialogs.SampleDialog;
import org.keiosu.visuturing.gui.dialogs.SplashWindow;
import org.keiosu.visuturing.gui.dialogs.StartupWizard;
import org.keiosu.visuturing.persistence.Persistence;
import org.keiosu.visuturing.persistence.VisuTuringFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class VisuTuring extends JFrame implements ActionListener, MouseMotionListener {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    SplashWindow splash;
    VTFrame currentWindow;
    JDesktopPane desktop;
    JMenuItem exitMI;
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
    public static final String CONFIGURE_LOOK_AND_FEEL_SYSTEM = "1 System Look and Feel";
    public static final String CONFIGURE_LOOK_AND_FEEL_JAVA = "2 Java Look and Feel";
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
                    public void windowClosing(WindowEvent event) {
                        JInternalFrame[] frames = VisuTuring.this.desktop.getAllFrames();

                        for (JInternalFrame frame : frames) {
                            frame.doDefaultCloseAction();
                        }

                        VisuTuring.this.close();
                    }
                });
        this.initWindow();
    }

    private void initWindow() {
        this.splash = new SplashWindow(this.createImageIcon("bitmaps/splash.png"), 5000);
        this.setIconImage(this.createImageIcon("bitmaps/vticon.jpg").getImage());
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.desktop = new JDesktopPane();
        this.desktop.setBorder(BorderFactory.createEtchedBorder(0));
        this.chooser = new JFileChooser();
        Container pane = this.getContentPane();
        pane.add(this.desktop, "Center");
        this.splash.setProgressText("Setting up menus and toolbars...");
        this.machineGroup = new ActionGroup();
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        JMenu fileMenu = addMenu(menuBar, "File", 'F');
        JMenu editMenu = addMenu(menuBar, "Edit", 'E');
        JMenu simulateMenu = addMenu(menuBar, "Simulate", 'S');
        JMenu configureMenu = addMenu(menuBar, "Configure", 'C');
        JMenu helpMenu = addMenu(menuBar, "Help", 'H');
        JMenu lookAndFeelMenu = new JMenu("Look & Feel");
        configureMenu.add(lookAndFeelMenu);
        JMenuItem systemLookAndFeel = new JMenuItem(CONFIGURE_LOOK_AND_FEEL_SYSTEM);
        systemLookAndFeel.setName(CONFIGURE_LOOK_AND_FEEL_SYSTEM);
        systemLookAndFeel.setToolTipText("Change the GUI to match the local system");
        systemLookAndFeel.addActionListener(this);
        systemLookAndFeel.setAccelerator(KeyStroke.getKeyStroke(49, InputEvent.CTRL_DOWN_MASK));
        systemLookAndFeel.setMnemonic('1');
        JMenuItem javaLookAndFeel = new JMenuItem(CONFIGURE_LOOK_AND_FEEL_JAVA);
        javaLookAndFeel.setName(CONFIGURE_LOOK_AND_FEEL_JAVA);
        javaLookAndFeel.setToolTipText("Change the GUI to the Java Look");
        javaLookAndFeel.addActionListener(this);
        javaLookAndFeel.setAccelerator(KeyStroke.getKeyStroke(50, InputEvent.CTRL_DOWN_MASK));
        javaLookAndFeel.setMnemonic('2');
        lookAndFeelMenu.add(systemLookAndFeel);
        lookAndFeelMenu.add(javaLookAndFeel);
        JToolBar toolBar = new JToolBar("Standard", 0);
        pane.add(toolBar, "North");
        toolBar.setRollover(true);
        this.addMenuAction(
                "New",
                FILE_NEW,
                "Starts the Turing Machine creation wizard",
                toolBar,
            fileMenu,
                'N',
                78,
                false);
        toolBar.addSeparator();
        this.addMenuAction(
                "Open", FILE_OPEN, "Open an existing Turing Machine", toolBar, fileMenu, 'O', 79, false);
        fileMenu.addSeparator();
        this.addMenuAction(
            null,
                FILE_VIEW_SAMPLE,
                "Open an example Turing machine",
                toolBar,
            fileMenu,
                'O',
                0,
                false);
        fileMenu.addSeparator();
        this.addMenuAction(
                FILE_SAVE, FILE_SAVE, "Save the current machine", toolBar, fileMenu, 'S', 83, true);
        this.addMenuAction(
                "SaveAs", FILE_SAVE_AS, "Save the current machine", toolBar, fileMenu, 'A', 0, true);
        this.addMenuAction(
                "SaveAll", FILE_SAVE_ALL, "Save all open machines", toolBar, fileMenu, 'l', 0, true);
        fileMenu.addSeparator();
        toolBar.addSeparator();
        this.addMenuAction("Export", FILE_EXPORT_JPEG, "Export the diagram", toolBar, fileMenu, 'E', 0, true);
        this.addMenuAction(
                "WebComponent",
                FILE_EXPORT_HTML,
                "Export Turing Machine in HTML format",
                toolBar,
            fileMenu,
                'H',
                0,
                true);
        toolBar.addSeparator();
        fileMenu.addSeparator();
        this.addMenuAction("Print", FILE_PRINT, "Print diagram", toolBar, fileMenu, 'H', 0, true);
        toolBar.addSeparator();
        this.addMenuAction(
                "EditDescription",
                EDIT_DESCRIPTION,
                "Edit the description for this Turing Machine",
                toolBar,
            editMenu,
                'd',
                0,
                true);
        this.addMenuAction(
                "EditStates",
                EDIT_STATES,
                "Edit the states this machine",
                toolBar,
            editMenu,
                's',
                0,
                true);
        this.addMenuAction(
                "EditAlphabet",
                EDIT_ALPHABET,
                "Edit the alphabet for this machine",
                toolBar,
            editMenu,
                'a',
                0,
                true);
        this.addMenuAction(
                "EditTransitions",
                EDIT_TABLE,
                "Edit the transition table for this machine",
                toolBar,
            editMenu,
                'T',
                84,
                true);
        editMenu.addSeparator();
        this.addMenuAction(
            null,
                EDIT_DIAGRAM,
                "Edit the transition diagram for this machine",
                toolBar,
            editMenu,
                'd',
                68,
                true);
        toolBar.addSeparator();
        this.addMenuAction(
            null,
                SIMULATE_HUMAN_COMPUTER,
                "Simulate using the human computer",
                toolBar,
            simulateMenu,
                'H',
                72,
                true);
        this.addMenuAction(
                "Play",
                SIMULATE_TRANSITION,
                "Simulate using VisuTuring® Transition Machine",
                toolBar,
            simulateMenu,
                'T',
                0,
                true);
        toolBar.addSeparator();
        this.addMenuAction("Help", HELP_USER_GUIDE, "View the User Guide", toolBar, helpMenu, 'U', 0, false);
        this.addMenuAction(
                "BrianCorbinTheAuthor",
                HELP_VISIT_BRIAN,
                "Find out more about the author",
            null,
            helpMenu,
                'F',
                0,
                false);
        helpMenu.addSeparator();
        this.addMenuAction(
                "About", HELP_ABOUT, "About VisuTuring", null, helpMenu, 'A', 0, false);
        fileMenu.addSeparator();
        this.exitMI = new JMenuItem(FILE_EXIT, 120);
        this.exitMI.setName(FILE_EXIT);
        this.exitMI.setAccelerator(KeyStroke.getKeyStroke(88, InputEvent.CTRL_DOWN_MASK));
        this.exitMI.addActionListener(this);
        fileMenu.add(this.exitMI);
        this.splash.complete();
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel.setBorder(BorderFactory.createEtchedBorder(0));
        this.statusBar = new JLabel(" ");
        this.statusBar.setHorizontalTextPosition(2);
        panel.add(this.statusBar);
        pane.add(panel, "South");
        this.setVisible(true);
        VisuTuringFileFilter fileFilter = new VisuTuringFileFilter();
        fileFilter.addExtension("vt");
        fileFilter.addExtension("xml");
        fileFilter.setDescription("VisuTuring Files (*.vt, *.xml)");
        this.chooser.setFileFilter(fileFilter);
        this.machineGroup.setEnabled(false);
        StartupWizard startupWizard = new StartupWizard(this);
        startupWizard.setVisible(true);
    }

    private JMenu addMenu(JMenuBar menuBar, String name, char mnemonic) {
        JMenu menu = new JMenu(name);
        menu.setMnemonic(mnemonic);
        menuBar.add(menu);
        return menu;
    }

    private void setStatus(String statusBarText) {
        this.statusBar.setText(statusBarText);
    }

    private void addAction(String iconName, String buttonName, String tooltip, JToolBar toolBar, boolean hasIcon) {
        JButton button;
        if (iconName != null) {
            button = new JButton(this.createImageIcon("buttons/" + iconName + "16.gif"));
        } else {
            button = new JButton();
        }

        button.addActionListener(this);
        button.setName(buttonName);
        button.setToolTipText(tooltip);
        button.addMouseMotionListener(this);
        toolBar.add(button);
        if (hasIcon) {
            this.machineGroup.add(button);
        }

    }

    void addMenuAction(
            String iconName,
            String buttonName,
            String tooltip,
            JToolBar toolBar,
            JMenu menu,
            char mnemonic,
            int acceleratorKeyStroke,
            boolean hasIcon) {
        if (iconName != null && toolBar != null) {
            this.addAction(iconName, buttonName, tooltip, toolBar, hasIcon);
        }

        JMenuItem menuItem = new JMenuItem(buttonName);
        menuItem.setMnemonic(mnemonic);
        menuItem.addActionListener(this);
        menuItem.setToolTipText(tooltip);
        menuItem.addMouseMotionListener(this);
        menuItem.setName(buttonName);
        if (hasIcon) {
            this.machineGroup.add(menuItem);
        }

        if (iconName != null) {
            menuItem.setIcon(this.createImageIcon("buttons/" + iconName + "16.gif"));
        }

        if (acceleratorKeyStroke != 0) {
            menuItem.setAccelerator(KeyStroke.getKeyStroke(acceleratorKeyStroke, InputEvent.CTRL_DOWN_MASK));
        }

        menu.add(menuItem);
    }

    void close() {
        this.dispose();
        System.exit(0);
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() instanceof JComponent) {
            TuringMachine machine = null;
            if (this.desktop.getSelectedFrame() instanceof VTFrame) {
                this.currentWindow = (VTFrame) this.desktop.getSelectedFrame();
                machine = this.currentWindow.getTuringMachine();
            }

            JComponent source = (JComponent) event.getSource();
            String sourceName = source.getName();
            if (sourceName.equals(FILE_EXIT)) {
                this.close();
            } else if (!sourceName.equals(EDIT_STATES) && !sourceName.equals("Edit states")) {
                if (!sourceName.equals(EDIT_ALPHABET) && !sourceName.equals("Edit the alphabet")) {
                    if (sourceName.equals(EDIT_TABLE)) {
                        if (machine != null) {
                            EditTransitionsDialog dialog = new EditTransitionsDialog(this, machine);
                            dialog.setVisible(true);
                            if (dialog.didSucceed()) {
                                machine.setTransitions(dialog.getTransitions());
                            }
                        }
                    } else {
                        if (sourceName.equals(EDIT_DIAGRAM)) {
                            if (machine != null) {
                                if (!machine.hasDiagram()) {
                                    if (JOptionPane.showConfirmDialog(
                                        this,
                                            "This Turing Machine has no diagram.\n\n" 
                                                + "You must first generate a diagram based on the current\n" 
                                                + "Turing Machine transition table to be able to edit it to ensure consistency.\n" 
                                                + "Would you like VisuTuring to do this for you now?",
                                            TITLE,
                                        JOptionPane.YES_NO_CANCEL_OPTION,
                                        JOptionPane.QUESTION_MESSAGE) != 0) {
                                        return;
                                    }

                                    machine.generateDiagram();
                                }

                                this.currentWindow.displayDiagram();
                            }
                        } else if (sourceName.equals(SIMULATE_TRANSITION)) {
                            if (machine != null) {
                                this.currentWindow.displaySimulation();
                            }
                        } else if (sourceName.equals(SIMULATE_HUMAN_COMPUTER)) {
                            if (machine != null) {
                                this.currentWindow.displayHuman();
                            }
                        } else if (!sourceName.equals(EDIT_DESCRIPTION)
                                && !sourceName.equals("Edit the description of this Turing Machine")) {
                            switch (sourceName) {
                                case FILE_NEW:
                                    this.createNewMachine();
                                    break;
                                case FILE_SAVE:
                                    this.saveCurrent();
                                    break;
                                case FILE_SAVE_AS:
                                    this.saveAs();
                                    break;
                                case FILE_SAVE_ALL:
                                    this.saveAll();
                                    break;
                                case FILE_VIEW_SAMPLE:
                                    SampleDialog sampleDialog = new SampleDialog();
                                    sampleDialog.setVisible(true);
                                    File selectedFile = sampleDialog.getSelectedFile();
                                    if (sampleDialog.didSucceed() && selectedFile != null) {
                                        this.open(selectedFile);
                                    }
                                    break;
                                case FILE_OPEN:
                                    this.chooser.setCurrentDirectory(
                                        new File("My Turing Machines"));
                                    if (this.chooser.showOpenDialog(this) == 0
                                        && !this.isOpen(
                                        this.chooser.getSelectedFile().toString())) {
                                        this.open(this.chooser.getSelectedFile());
                                        this.setStatus("opened.");
                                    }
                                    break;
                                case FILE_PRINT:
                                    this.currentWindow.printDiagram();
                                    break;
                                default:
                                    if (fileActionPerformed(machine, sourceName)) {
                                        return;
                                    }
                                    break;
                            }
                        } else if (machine != null) {
                            EditDescriptionDialog dialog = new EditDescriptionDialog(this, machine);
                            dialog.setVisible(true);
                            if (dialog.didSucceed()) {
                                machine.setDescription(dialog.getDescription());
                                machine.setName(dialog.getName());
                            }
                        }
                    }
                } else if (machine != null) {
                    EditAlphabetDialog dialog = new EditAlphabetDialog(this, machine);
                    dialog.setVisible(true);
                    if (dialog.didSucceed()) {
                        machine.setAlphabet(dialog.getAlphabet());
                    }
                }
            } else if (machine != null) {
                EditStatesDialog dialog = new EditStatesDialog(this, machine);
                dialog.setVisible(true);
                if (dialog.didSucceed()) {
                    machine.setStates(dialog.getStates());
                }
            }
        }

        if (this.currentWindow != null) {
            this.currentWindow.repaint();
        }

        this.machineGroup.setEnabled(this.desktop.getAllFrames().length > 0);
    }

    private boolean fileActionPerformed(TuringMachine machine, String sourceName) {
        JFileChooser fileChooser;
        int returnVal;
        VisuTuringFileFilter fileFilter;
        switch (sourceName) {
            case FILE_EXPORT_JPEG:
                fileFilter = new VisuTuringFileFilter();
                fileFilter.addExtension("jpeg");
                fileFilter.addExtension("jpg");
                fileFilter.setDescription("Images Files (*.jpg, *.jpeg)");
                fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File("My Diagrams"));
                fileChooser.setFileFilter(fileFilter);
                returnVal = fileChooser.showSaveDialog(this);
                if (returnVal == 0) {
                    this.currentWindow.exportToJPEG(
                        fileChooser.getSelectedFile());
                    this.setStatus("exported successfully.");
                    JOptionPane.showMessageDialog(
                        this, "Exported Successfully");
                }
                break;
            case FILE_EXPORT_HTML:
                try {
                    fileFilter = new VisuTuringFileFilter();
                    fileFilter.addExtension("html");
                    fileFilter.addExtension("htm");
                    fileFilter.setDescription("HTML Files (*.htm, *.html)");
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("web"));
                    fileChooser.setFileFilter(fileFilter);
                    returnVal = fileChooser.showSaveDialog(this);
                    if (returnVal == 0) {
                        Persistence.exportToHTML(
                            machine,
                            fileChooser.getSelectedFile().getAbsolutePath());
                        this.setStatus("exported successfully.");
                        JOptionPane.showMessageDialog(
                            this, "Exported Successfully");
                    }
                } catch (Exception e) {
                    this.setStatus("Failed on export.");
                    JOptionPane.showMessageDialog(this,
                        "Export Failed.");
                    e.printStackTrace();
                }
                break;
            case "Generate a new diagram based on transition table":
                if (machine.hasDiagram()) {
                    if (JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you wish to regenerate the Turing Machine diagram?",
                        TITLE,
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE) != 0) {
                        return true;
                    }
                }

                machine.generateDiagram();
                machine.setHasDiagram(true);
                this.currentWindow.repaint();
                break;
            case CONFIGURE_LOOK_AND_FEEL_SYSTEM:
                try {
                    this.setStatus(
                        "Setting Local System Look & Feel...");
                    UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
                    SwingUtilities.updateComponentTreeUI(this);
                    this.setStatus("Look & Feel Set.");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Oops! There was an error setting look and feel",
                        TITLE,
                        JOptionPane.ERROR_MESSAGE,
                        null);
                }
                break;
            case CONFIGURE_LOOK_AND_FEEL_JAVA:
                try {
                    this.setStatus("Setting Java Look & Feel...");
                    UIManager.setLookAndFeel(
                        UIManager.getCrossPlatformLookAndFeelClassName());
                    SwingUtilities.updateComponentTreeUI(this);
                    this.setStatus("Look & Feel Set.");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Oops! There was an error setting look and feel",
                        TITLE,
                        JOptionPane.ERROR_MESSAGE,
                        null);
                }
                break;
            case HELP_ABOUT:
                this.splash =
                    new SplashWindow(
                        this.createImageIcon("bitmaps/splash.png"),
                        7000);
                this.splash.complete();
                this.splash.setProgressText(VISUTURING_DESCRIPTION);
                break;
            case HELP_VISIT_BRIAN:
                this.visitBrian();
                break;
            case HELP_USER_GUIDE:
                new JHelpFrame();
                break;
        }
        return false;
    }

    public void open(File file) {
        try {
            VTFrame window = this.openWindow(Persistence.load(file));
            window.setFileName(file.toString());
            window.setTitle(file.getName());
            this.setStatus("opened.");
        } catch (SAXException e) {
            JOptionPane.showMessageDialog(
                    this, "Error! Invalid VisuTuring File!", TITLE, JOptionPane.ERROR_MESSAGE, null);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this, "There was an error loading the file.", TITLE, JOptionPane.ERROR_MESSAGE,
                null);
        }
    }

    private boolean isOpen(String fileName) {
        return Arrays.stream(this.desktop.getAllFrames())
            .map(jInternalFrame -> (VTFrame) jInternalFrame)
            .anyMatch(vtFrame -> vtFrame.getFileName().equals(fileName));
    }

    private void createNewMachine() {
        CreationWizard wizard = new CreationWizard(this);
        wizard.setVisible(true);
        TuringMachine machine = wizard.getTuringMachine();
        if (machine != null) {
            VTFrame frame = new VTFrame(new VisuTuring.FrameHandler(),
                this,
                machine,
                1);
            frame.setVisible(true);
            frame.setSize(this.desktop.getSize());
            this.currentWindow = frame;
            this.desktop.add(frame);

            try {
                frame.setSelected(true);
            } catch (PropertyVetoException e) {
                // do nothing
            }
        }
    }

    public static void main(String[] args) {
        new VisuTuring();
    }

    private ImageIcon createImageIcon(String resource) {
        return new ImageIcon(
            Objects.requireNonNull(this.getClass().getClassLoader().getResource(resource)));
    }

    private VTFrame openWindow(TuringMachine machine) {
        VTFrame frame = new VTFrame(new VisuTuring.FrameHandler(), this, machine, 0);
        frame.setVisible(true);
        frame.setSize(this.desktop.getSize());
        this.currentWindow = frame;
        this.desktop.add(frame);

        try {
            frame.setSelected(true);
        } catch (PropertyVetoException ignored) {
            // do nothing
        }

        return frame;
    }

    private void saveAll() {
        JInternalFrame[] allFrames = this.desktop.getAllFrames();

        for (JInternalFrame internalFrame : allFrames) {
            VTFrame frame = (VTFrame) internalFrame;
            if (!frame.getFileName().equals("-untitled-")) {
                try {
                    Persistence.save(frame.getTuringMachine(), frame.getFileName());
                    frame.getTuringMachine().setChanged(false);
                    this.setStatus("saved.");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                        this, "There was an error saving the file.", TITLE,
                        JOptionPane.ERROR_MESSAGE, null);
                }
            } else {
                if (this.chooser.showSaveDialog(this) == 0) {
                    try {
                        Persistence.save(frame.getTuringMachine(), this.chooser.getSelectedFile());
                        frame.setFileName(this.chooser.getSelectedFile().toString());
                        frame.getTuringMachine().setChanged(false);
                        this.setStatus("saved.");
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(
                            this, "There was an error saving the file.", TITLE,
                            JOptionPane.ERROR_MESSAGE, null);
                    }
                }
            }
        }
    }

    private void saveAs() {
        this.chooser.setCurrentDirectory(new File("My Turing Machines"));
        if (this.chooser.showSaveDialog(this) == 0) {
            saveMachine();
        }
    }

    private void saveMachine() {
        try {
            Persistence.save(
                    this.currentWindow.getTuringMachine(), this.chooser.getSelectedFile());
            this.currentWindow.setFileName(this.chooser.getSelectedFile().toString());
            this.currentWindow.getTuringMachine().setChanged(false);
            this.setStatus("saved.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this, "There was an error saving the file.", TITLE, JOptionPane.ERROR_MESSAGE,
                null);
        }
    }

    private void saveCurrent() {
        if (!this.currentWindow.getFileName().equals("-untitled-")) {
            try {
                Persistence.save(
                        this.currentWindow.getTuringMachine(), this.currentWindow.getFileName());
                this.currentWindow.getTuringMachine().setChanged(false);
                this.setStatus("saved.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        this, "There was an error saving the file.", TITLE,
                    JOptionPane.ERROR_MESSAGE, null);
            }
        } else {
            this.chooser.setCurrentDirectory(new File("My Turing Machines"));
            if (this.chooser.showSaveDialog(this) == 0) {
                saveMachine();
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent event) {}

    @Override
    public void mouseMoved(MouseEvent event) {
        JComponent source = (JComponent) event.getSource();
        this.setStatus(source.getToolTipText());
    }

    public void visitBrian() {
        BrowserControl.displayURL("www.briancorbin.co.uk");
    }

    class FrameHandler extends InternalFrameAdapter {
        FrameHandler() {}

        public void internalFrameActivated(InternalFrameEvent event) {
            VTFrame source = (VTFrame) event.getSource();
            VisuTuring.this.setTitle("VisuTuring - [" + source.getTitle() + "]");
            VisuTuring.this.machineGroup.setEnabled(
                VisuTuring.this.desktop.getAllFrames().length > 0);
        }

        public void internalFrameClosing(InternalFrameEvent event) {
            VTFrame source = (VTFrame) event.getSource();
            if (source.getTuringMachine().isChanged()) {
                int returnValue = JOptionPane.showConfirmDialog(
                    null,
                    "Save changes to " + source.getFileName() + " before closing?",
                    TITLE,
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

                if (returnValue == 0) {
                    VisuTuring.this.saveCurrent();
                    source.setVisible(false);
                    source.dispose();
                } else if (returnValue == 1) {
                    source.setVisible(false);
                    source.dispose();
                } else if (returnValue == 2) {
                    LOG.atDebug().log("Close cancelled.");
                }
            } else {
                source.setVisible(false);
                source.dispose();
            }

            VisuTuring.this.machineGroup.setEnabled(
                VisuTuring.this.desktop.getAllFrames().length > 0);
        }
    }

    static class ActionGroup {
        List<JComponent> group = new ArrayList<>();

        public ActionGroup() {}

        public void add(JComponent component) {
            this.group.add(component);
        }

        public void setEnabled(boolean enabled) {
            for (JComponent jComponent : this.group) {
                jComponent.setEnabled(enabled);
            }
        }
    }
}
