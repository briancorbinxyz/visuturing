package org.keiosu.visuturing.gui.panels;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.keiosu.visuturing.core.Symbols;
import org.keiosu.visuturing.core.TuringMachine;

public class EditDescriptionPanel extends VTPanel implements ActionListener {

    public static final String INSERT_LEFT_END_MARKER_SYMBOL = "Insert " + Symbols.LEFT_END_MARKER + " Symbol";
    public static final String INSERT_SPACE_SYMBOL = "Insert " + Symbols.SPACE + " Symbol";

    private final JTextArea description;
    private final JTextField nameField;

    public EditDescriptionPanel(TuringMachine machine) {
        super("edit-description-panel.gif");
        JLayeredPane pane = new JLayeredPane();
        this.panel.add(pane);
        pane.setPreferredSize(this.panel.getPreferredSize());
        this.panel.setOpaque(false);
        pane.setOpaque(false);
        this.description = new JTextArea(machine.getDescription());
        this.description.setFont(new Font("Helvetica", Font.PLAIN, 12));
        this.description.setLineWrap(true);
        this.description.setWrapStyleWord(true);
        JScrollPane descriptionPane = new JScrollPane(this.description);
        descriptionPane.setHorizontalScrollBarPolicy(31);
        descriptionPane.setBounds(0, 68, 285, 200);
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(0, 16, 50, 20);
        this.nameField = new JTextField(machine.getName());
        this.nameField.setBounds(55, 16, 230, 20);
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setBounds(0, 45, 75, 20);
        JButton insertLeftHandButton = new JButton(INSERT_LEFT_END_MARKER_SYMBOL);
        insertLeftHandButton.setName(INSERT_LEFT_END_MARKER_SYMBOL);
        insertLeftHandButton.setBounds(0, 275, 125, 20);
        insertLeftHandButton.addActionListener(this);
        insertLeftHandButton.setFont(new Font("Helvetica", Font.PLAIN, 11));
        JButton insertSpaceButton = new JButton(INSERT_SPACE_SYMBOL);
        insertSpaceButton.setName(INSERT_SPACE_SYMBOL);
        insertSpaceButton.setBounds(160, 275, 125, 20);
        insertSpaceButton.addActionListener(this);
        insertSpaceButton.setFont(new Font("Helvetica", Font.PLAIN, 11));
        pane.add(nameLabel, JLayeredPane.DEFAULT_LAYER);
        pane.add(this.nameField, JLayeredPane.DEFAULT_LAYER);
        pane.add(descriptionLabel, JLayeredPane.DEFAULT_LAYER);
        pane.add(descriptionPane, JLayeredPane.DEFAULT_LAYER);
        pane.add(insertLeftHandButton, JLayeredPane.DEFAULT_LAYER);
        pane.add(insertSpaceButton, JLayeredPane.DEFAULT_LAYER);
    }

    public String getDescription() {
        return this.description.getText();
    }

    public String getName() {
        return this.nameField.getText();
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() instanceof JButton) {
            JButton button = (JButton) event.getSource();
            if (button.getName().equals(INSERT_SPACE_SYMBOL)) {
                this.description.insert(String.valueOf(Symbols.SPACE), this.description.getCaretPosition());
            } else if (button.getName().equals(INSERT_LEFT_END_MARKER_SYMBOL)) {
                this.description.insert(String.valueOf(Symbols.LEFT_END_MARKER), this.description.getCaretPosition());
            }
        }
    }
}
