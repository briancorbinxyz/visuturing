package org.keiosu.visuturing.gui;

import static org.keiosu.visuturing.gui.common.CommonGraphics.newButtonWithHand;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JScrollPane;
import org.keiosu.visuturing.core.Symbols;
import org.keiosu.visuturing.core.Transition;
import org.keiosu.visuturing.core.TuringMachine;

public class TransitionFrame extends JDialog implements ActionListener {

  public static final Font TRANSITION_FONT = new Font("Helvetica", Font.BOLD, 15);

  private final Transition transition;
  private final JList<String> transitionSymbol;
  private final JList<String> task;

  public TransitionFrame(TuringMachine machine, Transition transition) {
    super(new Frame(), true);
    this.transition = new Transition(transition);
    setResizable(false);
    setUndecorated(true);
    List<String> alphabet = new ArrayList<>(machine.getAlphabet());
    transitionSymbol = new JList<>(alphabet.toArray(new String[] {}));
    transitionSymbol.setFont(TRANSITION_FONT);
    task = new JList<>(actionableAlphabet(alphabet));
    task.setFont(TRANSITION_FONT);
    JLayeredPane pane = new JLayeredPane();
    Container contentPane = getContentPane();
    contentPane.add(pane);
    pane.add(newTransitionImageLabel(), JLayeredPane.DEFAULT_LAYER);
    pane.add(newUpdateButton(), JLayeredPane.POPUP_LAYER);
    pane.add(newStateLabel(10, transition.getCurrentState()), JLayeredPane.POPUP_LAYER);
    pane.add(newStateLabel(131, transition.getNextState()), JLayeredPane.POPUP_LAYER);
    pane.add(newSymbolPane(transition.getCurrentSymbol()), JLayeredPane.POPUP_LAYER);
    pane.add(newScrollPane(task, 192), JLayeredPane.POPUP_LAYER);
    task.setSelectedValue("" + transition.getTask(), true);
    pane.setPreferredSize(
        new Dimension(
            (int) newTransitionImageLabel().getPreferredSize().getWidth(),
            (int) newTransitionImageLabel().getPreferredSize().getHeight()));
    pack();
  }

  private JScrollPane newScrollPane(JList<String> task, int x) {
    JScrollPane pane = new JScrollPane(task);
    pane.setBounds(x, 19, 60, 20);
    return pane;
  }

  public Transition getTransition() {
    if (transitionSymbol.getSelectedIndex() > -1) {
      transition.setCurrentSymbol(transitionSymbol.getSelectedValue().charAt(0));
    } else {
      transition.setCurrentSymbol(Symbols.SPACE);
    }

    if (task.getSelectedIndex() > -1) {
      transition.setTask(task.getSelectedValue().charAt(0));
    } else {
      transition.setTask(Symbols.SPACE);
    }

    return transition.getCurrentSymbol() == Symbols.LEFT_END_MARKER
            && transition.getTask() != Symbols.RIGHT_ARROW
        ? null
        : transition;
  }

  public void actionPerformed(ActionEvent var1) {
    setVisible(false);
  }

  private String[] actionableAlphabet(List<String> original) {
    List<String> alphabet = new ArrayList<>(original);
    alphabet.remove(String.valueOf(Symbols.LEFT_END_MARKER));
    alphabet.add(String.valueOf(Symbols.LEFT_ARROW));
    alphabet.add(String.valueOf(Symbols.RIGHT_ARROW));
    return alphabet.toArray(new String[] {});
  }

  private JScrollPane newSymbolPane(char currentSymbol) {
    JScrollPane scrollPane = newScrollPane(transitionSymbol, 70);
    transitionSymbol.setBounds(70, 19, 60, 20);
    scrollPane.setWheelScrollingEnabled(true);
    transitionSymbol.setSelectedValue("" + currentSymbol, true);
    return scrollPane;
  }

  private JLabel newStateLabel(int x, String name) {
    JLabel label = new JLabel(name);
    label.setForeground(Color.BLACK);
    label.setFont(TRANSITION_FONT);
    label.setHorizontalAlignment(0);
    label.setBounds(x, 19, 60, 20);
    return label;
  }

  private JButton newUpdateButton() {
    JButton button = newTickButton();
    button.setBounds(258, 19, 22, 22);
    return button;
  }

  private JLabel newTransitionImageLabel() {
    JLabel imageLabel = new JLabel(createImageIcon("bitmaps/transition-frame.gif"));
    imageLabel.setBounds(
        0,
        0,
        (int) imageLabel.getPreferredSize().getWidth(),
        (int) imageLabel.getPreferredSize().getHeight());
    return imageLabel;
  }

  private JButton newTickButton() {
    JButton button = newButtonWithHand("Update");
    button.setIcon(createImageIcon("buttons/tick.gif"));
    button.setPreferredSize(new Dimension(22, 22));
    button.addActionListener(this);
    return button;
  }

  private ImageIcon createImageIcon(String resource) {
    return new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource(resource)));
  }
}
