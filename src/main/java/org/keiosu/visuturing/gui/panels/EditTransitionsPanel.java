package org.keiosu.visuturing.gui.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.keiosu.visuturing.core.State;
import org.keiosu.visuturing.core.Symbols;
import org.keiosu.visuturing.core.Transition;
import org.keiosu.visuturing.core.TuringMachine;

public class EditTransitionsPanel extends VTPanel implements ActionListener {
    JTextArea description;
    JButton diagramButton;
    JTable table;
    TuringMachine machine;
    List<String> columnNames;
    public static final String ADD_TRANSITION_BUTTON = "Add Transition";
    public static final String REMOVE_TRANSITION_BUTTON = "Remove Transition";
    public static final String GENERATE_BUTTON = "Generate Transition Diagram";

    public void refresh() {
        List<Object[]> rows = new ArrayList<>();
        List<Transition> transitions = this.machine.getTransitions();
        List<State> states = this.machine.getStates();
        List<String> alphabet = new ArrayList<>(this.machine.getAlphabet());

        for (Transition transition : transitions) {
            List<String> columnValues = new ArrayList<>(4);
            columnValues.add(transition.getCurrentState());
            columnValues.add("" + transition.getCurrentSymbol());
            columnValues.add(transition.getNextState());
            columnValues.add("" + transition.getTask());
            rows.add(columnValues.toArray());
        }

        DefaultTableModel var18 = (DefaultTableModel) this.table.getModel();
        var18.setDataVector(rows.toArray(new Object[][] {}), this.columnNames.toArray());
        TableColumn var19 = this.table.getColumnModel().getColumn(2);
        JComboBox var20 = new JComboBox(states.toArray());
        var19.setCellEditor(new DefaultCellEditor(var20));
        TableColumn var8 = this.table.getColumnModel().getColumn(0);
        List var9 = new ArrayList();

        for (int var10 = 0; var10 < states.size(); ++var10) {
            if (!((State) states.get(var10)).getName().equals(Symbols.STATE_HALTING_STATE)) {
                var9.add(((State) states.get(var10)).getName());
            }
        }

        JComboBox var21 = new JComboBox(var9.toArray());
        var8.setCellEditor(new DefaultCellEditor(var21));
        TableColumn var11 = this.table.getColumnModel().getColumn(1);
        List var12 = new ArrayList();

        for (int var13 = 0; var13 < alphabet.size(); ++var13) {
            var12.add((String) alphabet.get(var13));
        }

        List var22 = new ArrayList(var12);
        JComboBox var14 = new JComboBox(var22.toArray());
        var11.setCellEditor(new DefaultCellEditor(var14));
        TableColumn var15 = this.table.getColumnModel().getColumn(3);
        List var16 = new ArrayList(var12);
        var16.remove(String.valueOf(Symbols.LEFT_END_MARKER));
        var16.add(String.valueOf(Symbols.LEFT_ARROW));
        var16.add(String.valueOf(Symbols.RIGHT_ARROW));
        JComboBox var17 = new JComboBox(var16.toArray());
        var15.setCellEditor(new DefaultCellEditor(var17));
    }

    public EditTransitionsPanel(TuringMachine var1) {
        super("edit-transitions-panel.gif");
        this.machine = var1;
        JLayeredPane var2 = new JLayeredPane();
        this.panel.add(var2);
        var2.setPreferredSize(this.panel.getPreferredSize());
        this.columnNames = new ArrayList();
        this.columnNames.add(new String("Current State"));
        this.columnNames.add(new String("Current Symbol"));
        this.columnNames.add(new String("Next State"));
        this.columnNames.add(new String("Task"));
        this.table = new JTable();
        this.refresh();
        JScrollPane var3 = new JScrollPane(this.table);
        var3.setBounds(0, 0, 300, 130);
        JButton var4 = new JButton("Add Transition");
        var4.setBounds(0, 138, 148, 20);
        var4.addActionListener(this);
        var4.setName("Add Transition");
        JButton var5 = new JButton("Remove Transition");
        var5.setBounds(152, 138, 148, 20);
        var5.addActionListener(this);
        var5.setName("Remove Transition");
        this.diagramButton = new JButton("Generate Transition Diagram");
        this.diagramButton.setBounds(0, 165, 300, 20);
        this.diagramButton.setName("Generate Transition Diagram");
        this.diagramButton.addActionListener(this);
        this.description = new JTextArea();
        this.description.setBounds(0, 187, 300, 50);
        this.description.setLineWrap(true);
        this.description.setWrapStyleWord(true);
        this.description.setText(
                "THIS TRANSITION MACHINE HAS A DIAGRAM.\nGENERATING A NEW DIAGRAM WILL REPLACE THAT ONE.\nADDING OR REMOVING A TRANSITION USING THE TABLE WILL MODIFY DIAGRAM.");
        this.description.setOpaque(false);
        this.description.setEditable(false);
        this.description.setFont(new Font("Helvetica", 0, 10));
        this.description.setForeground(Color.red);
        if (!var1.hasDiagram()) {
            this.description.setVisible(false);
        }

        JButton var6 = new JButton("Create / Edit / View Transition Diagram");
        var6.setBounds(0, 245, 300, 20);
        var2.add(var3, JLayeredPane.DEFAULT_LAYER);
        var2.add(var4, JLayeredPane.DEFAULT_LAYER);
        var2.add(var5, JLayeredPane.DEFAULT_LAYER);
        var2.add(this.diagramButton, JLayeredPane.DEFAULT_LAYER);
        var2.add(this.description, JLayeredPane.DEFAULT_LAYER);
        var2.add(var6, JLayeredPane.DEFAULT_LAYER);
    }

    public String getDescription() {
        return this.description.getText();
    }

    public List getTransitions() {
        DefaultTableModel var1 = (DefaultTableModel) this.table.getModel();
        List var2 = var1.getDataVector();
        List var3 = new ArrayList();

        for (int var4 = 0; var4 < var2.size(); ++var4) {
            boolean var5 = false;
            List var6 = (List) var2.get(var4);
            String var7 = var6.get(0).toString();
            if (var7 == null || var7.equals("")) {
                var5 = true;
            }

            String var8 = (String) var6.get(1);
            char var9 = ' ';
            if (var8 == null || var8.equals("")) {
                var5 = true;
            }

            if (!var5) {
                var9 = ((String) var6.get(1)).charAt(0);
            }

            String var10 = var6.get(2).toString();
            if (var10 == null || var10.equals("")) {
                var5 = true;
            }

            String var11 = (String) var6.get(3);
            char var12 = ' ';
            if (var11 == null || var11.equals("")) {
                var5 = true;
            }

            if (!var5) {
                var12 = ((String) var6.get(3)).charAt(0);
            }

            if (!var5) {
                Transition var13 = new Transition(var7, var9, var10, var12);
                Point2D var14 = this.machine.stateFor(var7).getLocation();
                Point2D var15 = this.machine.stateFor(var10).getLocation();
                Double var16 =
                        new Double(
                                var14.getX() + (var15.getX() - var14.getX()) / 2.0D,
                                var14.getY() + (var15.getY() - var14.getY()) / 2.0D);
                java.awt.geom.QuadCurve2D.Double var17 = new java.awt.geom.QuadCurve2D.Double();
                var17.setCurve(var14, var16, var15);
                var13.setEdge(var17);
                Transition var18 = this.machine.getEqualTransition(var13);
                if (var18 != null) {
                    var3.add(var18);
                } else {
                    var3.add(var13);
                }
            }
        }

        return var3;
    }

    public void actionPerformed(ActionEvent var1) {
        if (var1.getSource() instanceof JButton) {
            JButton var2 = (JButton) var1.getSource();
            DefaultTableModel var3;
            if (var2.getName().equals("Add Transition")) {
                var3 = (DefaultTableModel) this.table.getModel();
                var3.addRow(new Object[] {"", "", "", ""});
                if (var3.getRowCount() > 0 && !this.diagramButton.isEnabled()) {
                    this.diagramButton.setEnabled(true);
                }
            } else if (var2.getName().equals("Remove Transition")) {
                var3 = (DefaultTableModel) this.table.getModel();
                if (this.table.getSelectedRow() != -1) {
                    var3.removeRow(this.table.getSelectedRow());
                }

                if (var3.getRowCount() < 1 && this.diagramButton.isEnabled()) {
                    this.diagramButton.setEnabled(false);
                }
            } else if (var2.getName().equals("Generate Transition Diagram")) {
                if (this.machine.hasDiagram()) {
                    int var4 =
                            JOptionPane.showConfirmDialog(
                                    this,
                                    "Are you sure you want to generate a new diagram?\n\nThis will replace your existing diagram",
                                    "VisuTuring",
                                    0,
                                    3,
                                    (Icon) null);
                    if (var4 == 1) {
                        return;
                    }
                }

                JOptionPane.showMessageDialog(
                        this,
                        "A transition diagram was automatically created for your Turing Machine by VisuTuring.",
                        "VisuTuring",
                        1,
                        (Icon) null);
                this.machine.setHasDiagram(true);
                this.description.setVisible(true);
            }
        }
    }
}
