package org.keiosu.visuturing.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Objects;
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
import org.keiosu.visuturing.core.Symbols;
import org.keiosu.visuturing.gui.common.CommonGraphics;
import org.keiosu.visuturing.simulator.AbstractSimulatorPanel;

public class MainSimulationPanel extends JPanel implements ActionListener, KeyListener {
    public static final String STOP = "Stop the simulation";
    public static final String PLAY = "Simulate this machine on the input word";
    public static final String PAUSE = "Pause the running simulation";
    public static final String ADD_SYMBOL = "Insert a symbol from the alphabet";
    public static final String CLOSE = "Close alphabet menu";
    public static final String CLEAR = "Clear the tape";
    public static final String INCREASE_SPEED = "Increase the speed";
    public static final String DECREASE_SPEED = "Decrease the speed";
    public static final String INSTRUCTIONS =
            "Type Input here. Press INSERT to insert the blank character.";
    ActionListener externalListener;
    AbstractSimulatorPanel simulator;
    MainSimulationPanel.InputWordBox inputBox;
    JPopupMenu alphabetMenu;

    public MainSimulationPanel(ActionListener listener, AbstractSimulatorPanel simulatorPanel) {
        this.simulator = simulatorPanel;
        this.externalListener = listener;
        this.setLayout(new BorderLayout(0, 0));
        this.setBackground(Color.WHITE);
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setOpaque(false);
        this.add(panel, "North");
        panel.add(new JLabel(this.createImageIcon("bitmaps/simulator/title.gif")), "West");
        JPanel centerPanel = new JPanel(new BorderLayout(0, 0));
        panel.add(centerPanel, "Center");
        JPanel topBorder = new JPanel(new BorderLayout(0, 0));
        topBorder.add(new JLabel(this.createImageIcon("bitmaps/diagram/topborder.gif")));
        topBorder.setBackground(Color.BLACK);
        JPanel borderPanel = new JPanel(new BorderLayout(0, 0));
        borderPanel.setBackground(Color.white);
        borderPanel.add(topBorder, "North");
        centerPanel.add(borderPanel, "Center");
        centerPanel.add(new JLabel(this.createImageIcon("bitmaps/diagram/rightcorner.gif")), "East");
        JPanel eastBorder = new JPanel(new BorderLayout(0, 0));
        eastBorder.setBackground(Color.BLACK);
        eastBorder.add(new JLabel(this.createImageIcon("bitmaps/diagram/sideborder.gif")));
        this.add(eastBorder, "East");
        JPanel westBorder = new JPanel(new BorderLayout(0, 0));
        westBorder.setBackground(Color.BLACK);
        westBorder.add(new JLabel(this.createImageIcon("bitmaps/diagram/sideborder.gif")));
        this.add(westBorder, "West");
        JPanel southBorder = new JPanel(new BorderLayout(0, 0));
        southBorder.setBackground(Color.BLACK);
        southBorder.add(new JLabel(this.createImageIcon("bitmaps/diagram/topborder.gif")));
        this.add(southBorder, "South");
        JPanel centerBorder = new JPanel(new BorderLayout(0, 0));
        centerBorder.setBackground(Color.WHITE);
        this.add(centerBorder, "Center");
        JPanel flowPanel = new JPanel(new BorderLayout(0, 0));
        flowPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 20, 10));
        flowPanel.setOpaque(false);
        centerBorder.add(flowPanel, "North");
        centerBorder.add(this.simulator);
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 10, 10));
        inputPanel.setOpaque(false);
        JPanel mediaPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING, 10, 10));
        mediaPanel.setOpaque(false);
        this.inputBox = new MainSimulationPanel.InputWordBox(this, this);
        this.inputBox.setToolTipText(
            INSTRUCTIONS);
        inputPanel.add(this.inputBox);
        inputPanel.add(new MainSimulationPanel.SymbolButton());
        mediaPanel.add(this.createMediaButton(PLAY, "play"));
        mediaPanel.add(this.createMediaButton(PAUSE, "pause"));
        mediaPanel.add(this.createMediaButton(STOP, "stop"));
        mediaPanel.add(new MainSimulationPanel.SpeedShifter());
        flowPanel.add(inputPanel, "West");
        flowPanel.add(mediaPanel, "East");
    }

    JButton createMediaButton(String name, String icon) {
        JButton button = CommonGraphics.newButtonWithHand(name);
        button.setIcon(this.createImageIcon("buttons/simulator/" + icon + ".gif"));
        button.setPreferredSize(new Dimension(52, 52));
        button.addActionListener(this);
        button.addActionListener(this.externalListener);
        return button;
    }

    ImageIcon createImageIcon(String resource) {
        return new ImageIcon(Objects.requireNonNull(this.getClass().getClassLoader().getResource(resource)));
    }

    public void actionPerformed(ActionEvent event) {
        if (!(event.getSource() instanceof JComponent)) {
            return;
        }

        JComponent source = (JComponent) event.getSource();
        String sourceName = source.getName();
        switch (sourceName) {
            case PLAY:
                simulator.setInputWord(inputBox.getText() != null ? inputBox.getText() : "");
                if (this.isAcceptableWord(this.inputBox.getText())) {
                    this.simulator.play();
                }
                break;
            case STOP:
                this.simulator.stop();
                break;
            case INCREASE_SPEED:
                this.simulator.increaseSpeed();
                break;
            case DECREASE_SPEED:
                this.simulator.decreaseSpeed();
                break;
            case PAUSE:
                this.simulator.pause();
                break;
            case ADD_SYMBOL:
                Point sourceLocation = source.getLocation();
                SwingUtilities.convertPointToScreen(sourceLocation, source);
                this.popupAlphabet(sourceLocation);
                break;
        }
    }

    private boolean isAcceptableWord(String word) {
        List<String> alphabet = this.simulator.getMachine().getAlphabet();
        boolean acceptableWord = true;

        for (int i = 0; i < word.length(); ++i) {
            boolean acceptableCharacter = false;
            char c = word.charAt(i);

            for (String s : alphabet) {
                if (s.charAt(0) == c) {
                    acceptableCharacter = true;
                    break;
                }
            }

            if (!acceptableCharacter) {
                acceptableWord = false;
                break;
            }
        }

        if (!acceptableWord) {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid Input.\n\nOnly symbols from the alphabet may be     \nused in the input word. \n\nClick the ADD SYMBOL button for a list of \nvalid symbols.",
                    "Invalid Input",
                JOptionPane.ERROR_MESSAGE);
        }

        return acceptableWord;
    }

    private void popupAlphabet(Point point) {
        if (this.alphabetMenu != null) {
            this.alphabetMenu.setVisible(false);
            this.alphabetMenu = null;
        }

        this.alphabetMenu = new JPopupMenu("Alphabet");
        List<String> alphabet = this.simulator.getMachine().getAlphabet();
        Font font = new Font("Helvetica", Font.PLAIN, 14);
        ActionListener listener =
            event -> {
                String sourceName = ((Component) event.getSource()).getName();
                if (sourceName.equals(CLOSE)) {
                    MainSimulationPanel.this.alphabetMenu.setVisible(false);
                } else if (sourceName.equals(CLEAR)) {
                    MainSimulationPanel.this.inputBox.setText("");
                } else {
                    MainSimulationPanel.this.inputBox.setText(
                            MainSimulationPanel.this
                                            .inputBox
                                            .getText()
                                            .substring(
                                                    0,
                                                    MainSimulationPanel.this
                                                            .inputBox
                                                            .getField()
                                                            .getCaretPosition())
                                    + sourceName
                                    + MainSimulationPanel.this
                                            .inputBox
                                            .getText()
                                            .substring(
                                                    MainSimulationPanel.this
                                                            .inputBox
                                                            .getField()
                                                            .getCaretPosition(),
                                                    MainSimulationPanel.this
                                                            .inputBox
                                                            .getField()
                                                            .getText()
                                                            .length()));
                }
            };
        JMenuItem clearMenuItem = this.alphabetMenu.add("clear");
        clearMenuItem.setName(CLEAR);
        clearMenuItem.addActionListener(listener);
        this.alphabetMenu.addSeparator();

        for (String s : alphabet) {
            if (s.charAt(0) != Symbols.LEFT_END_MARKER) {
                JMenuItem menuItem = new JMenuItem(s);
                menuItem.setFont(font);
                menuItem.setName(s);
                menuItem.addActionListener(listener);
                this.alphabetMenu.add(menuItem);
            }
        }

        this.alphabetMenu.addSeparator();
        JMenuItem closeMenuItem = this.alphabetMenu.add("close");
        closeMenuItem.setName(CLOSE);
        closeMenuItem.addActionListener(listener);
        this.alphabetMenu.setLocation(point);
        this.alphabetMenu.setVisible(true);
    }

    public void refresh() {
        this.simulator.refresh();
    }

    public void keyTyped(KeyEvent event) {}

    public void keyReleased(KeyEvent event) {}

    public void keyPressed(KeyEvent event) {
        if (event.getKeyCode() == 155) {
            this.inputBox.setText(
                    this.inputBox
                                    .getText()
                                    .substring(0, this.inputBox.getField().getCaretPosition())
                            + Symbols.SPACE
                            + this.inputBox
                                    .getText()
                                    .substring(
                                            this.inputBox.getField().getCaretPosition(),
                                            this.inputBox.getField().getText().length()));
        }
    }

    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (this.alphabetMenu != null) {
            this.alphabetMenu.setVisible(false);
            this.alphabetMenu = null;
        }
    }

    public class SpeedShifter extends JPanel {

        public SpeedShifter() {
            super(new BorderLayout(0, 0));
            JLabel speedLabel =
                    new JLabel(
                            MainSimulationPanel.this.createImageIcon(
                                    "bitmaps/simulator/speed.gif"));
            speedLabel.setBounds(
                    0,
                    0,
                    (int) speedLabel.getPreferredSize().getWidth(),
                    (int) speedLabel.getPreferredSize().getHeight());
            JButton increase =
                    MainSimulationPanel.this.createMediaButton(INCREASE_SPEED, "increase");
            increase.setBounds(14, 18, 22, 22);
            JButton decrease =
                    MainSimulationPanel.this.createMediaButton(DECREASE_SPEED, "decrease");
            decrease.setBounds(56, 18, 22, 22);
            JLayeredPane pane = new JLayeredPane();
            pane.setPreferredSize(speedLabel.getPreferredSize());
            this.add(pane);
            pane.add(speedLabel, JLayeredPane.DEFAULT_LAYER);
            pane.add(increase, JLayeredPane.POPUP_LAYER);
            pane.add(decrease, JLayeredPane.POPUP_LAYER);
            this.setPreferredSize(pane.getPreferredSize());
        }
    }

    public class InputWordBox extends JPanel {
        JTextField inputWord;

        public void setToolTipText(String toolTipText) {
            super.setToolTipText(toolTipText);
            this.inputWord.setToolTipText(toolTipText);
        }

        public InputWordBox(ActionListener actionListener, KeyListener keyListener) {
            super(new BorderLayout(0, 0));
            JLabel inputWord =
                    new JLabel(
                            MainSimulationPanel.this.createImageIcon(
                                    "bitmaps/simulator/inputw.gif"));
            inputWord.setBounds(
                    0,
                    0,
                    (int) inputWord.getPreferredSize().getWidth(),
                    (int) inputWord.getPreferredSize().getHeight());
            this.inputWord = new JTextField("");
            this.inputWord.setFont(new Font("monospaced", Font.PLAIN, 16));
            this.inputWord.setBounds(21, 17, 260, 23);
            this.inputWord.setName(PLAY);
            this.inputWord.addActionListener(actionListener);
            this.inputWord.addKeyListener(keyListener);
            JLayeredPane pane = new JLayeredPane();
            pane.setPreferredSize(inputWord.getPreferredSize());
            this.add(pane);
            pane.add(inputWord, JLayeredPane.DEFAULT_LAYER);
            pane.add(this.inputWord, JLayeredPane.POPUP_LAYER);
            this.setPreferredSize(pane.getPreferredSize());
        }

        public String getText() {
            return this.inputWord.getText();
        }

        public JTextField getField() {
            return this.inputWord;
        }

        public void setText(String text) {
            this.inputWord.setText(text);
            this.inputWord.setCaretPosition(text.length());
        }
    }

    public class SymbolButton extends JPanel {
        public SymbolButton() {
            super(new BorderLayout(0, 0));
            JLabel alphabetLabel =
                    new JLabel(
                            MainSimulationPanel.this.createImageIcon(
                                    "bitmaps/simulator/alphabet.gif"));
            alphabetLabel.setBounds(
                    0,
                    0,
                    (int) alphabetLabel.getPreferredSize().getWidth(),
                    (int) alphabetLabel.getPreferredSize().getHeight());
            JButton addSymbol =
                    MainSimulationPanel.this.createMediaButton(
                        ADD_SYMBOL, "add-symbol");
            addSymbol.setBounds(7, 19, 78, 18);
            JLayeredPane pane = new JLayeredPane();
            pane.setPreferredSize(alphabetLabel.getPreferredSize());
            this.add(pane);
            pane.add(alphabetLabel, JLayeredPane.DEFAULT_LAYER);
            pane.add(addSymbol, JLayeredPane.POPUP_LAYER);
            this.setPreferredSize(pane.getPreferredSize());
        }
    }
}
