package org.keiosu.visuturing.gui.panels;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.keiosu.visuturing.core.Transition;
import org.keiosu.visuturing.core.TuringMachine;

public class EditAlphabetPanel extends VTPanel implements ActionListener, ListSelectionListener {
    JTextField symbolField;
    JList alphabetList;
    List<String> alphabet;
    JButton removeButton;
    JButton removeAllButton;
    TuringMachine machine;

    public EditAlphabetPanel(TuringMachine var1) {
        super("eapanel.gif");
        this.machine = var1;
        JLayeredPane var2 = new JLayeredPane();
        this.panel.setLayout(new BorderLayout());
        this.panel.add(var2);
        var2.setPreferredSize(this.panel.getPreferredSize());
        JLabel var3 = new JLabel("Symbol:");
        var3.setBounds(0, 16, 65, 20);
        this.symbolField = new JTextField();
        this.symbolField.setBounds(79, 16, 119, 20);
        JButton var4 = new JButton("Add");
        var4.setName("Add");
        var4.addActionListener(this);
        var4.setBounds(205, 16, 95, 20);
        JLabel var5 = new JLabel("Alphabet:");
        var5.setBounds(0, 49, 65, 20);
        this.alphabet = new ArrayList(var1.getAlphabet());
        this.alphabetList = new JList(this.alphabet.toArray());
        this.alphabetList.addListSelectionListener(this);
        this.alphabetList.setFont(new Font("sanserif", 0, 14));
        JScrollPane var6 = new JScrollPane(this.alphabetList);
        var6.setBounds(79, 48, 120, 170);
        this.removeButton = new JButton("Remove");
        this.removeButton.setName("Remove");
        this.removeButton.addActionListener(this);
        this.removeButton.setEnabled(false);
        this.removeButton.setBounds(205, 48, 95, 20);
        this.removeAllButton = new JButton("Remove All");
        this.removeAllButton.setName("Remove All");
        this.removeAllButton.addActionListener(this);
        this.removeAllButton.setEnabled(false);
        this.removeAllButton.setBounds(205, 76, 95, 20);
        JButton var7 = new JButton("Add {0,1}");
        var7.setName("Add {0,1}");
        var7.addActionListener(this);
        var7.setBounds(205, 142, 95, 20);
        JButton var8 = new JButton("Add {a,b}");
        var8.setName("Add {a,b}");
        var8.addActionListener(this);
        var8.setBounds(205, 170, 95, 20);
        JButton var9 = new JButton("Add {0..9}");
        var9.setName("Add {0..9}");
        var9.addActionListener(this);
        var9.setBounds(205, 198, 95, 20);
        var2.add(var3, JLayeredPane.DEFAULT_LAYER);
        var2.add(this.symbolField, JLayeredPane.DEFAULT_LAYER);
        var2.add(var5, JLayeredPane.DEFAULT_LAYER);
        var2.add(var4, JLayeredPane.DEFAULT_LAYER);
        var2.add(var6, JLayeredPane.DEFAULT_LAYER);
        var2.add(this.removeAllButton, JLayeredPane.DEFAULT_LAYER);
        var2.add(this.removeButton, JLayeredPane.DEFAULT_LAYER);
        var2.add(var7, JLayeredPane.DEFAULT_LAYER);
        var2.add(var8, JLayeredPane.DEFAULT_LAYER);
        var2.add(var9, JLayeredPane.DEFAULT_LAYER);
    }

    public boolean hasTransitions(char var1) {
        List var2 = this.machine.getTransitions();
        boolean var3 = false;

        for (int var4 = 0; var4 < var2.size(); ++var4) {
            Transition var5 = (Transition) var2.get(var4);
            if (var5.getCurrentSymbol() == var1 || var5.getTask() == var1) {
                var3 = true;
                return var3;
            }
        }

        return var3;
    }

    public void actionPerformed(ActionEvent var1) {
        if (var1.getSource() instanceof JButton) {
            JButton var2 = (JButton) var1.getSource();
            if (var2.getName().equals("Add")) {
                String var3 = this.symbolField.getText();
                if (var3 != null && var3.length() == 1) {
                    if (!this.symbolExists(var3) && !this.isStandardSymbol(var3)) {
                        this.alphabet.add(var3);
                    }

                    this.alphabetList.setListData(this.alphabet.toArray());
                    this.symbolField.setText("");
                    if (!this.removeAllButton.isEnabled()) {
                        this.removeAllButton.setEnabled(true);
                    }
                }
            } else if (var2.getName().equals("Remove")) {
                int var7 = this.alphabetList.getSelectedIndex();
                if (var7 > -1) {
                    if (!this.hasTransitions(((String) this.alphabet.get(var7)).charAt(0))) {
                        this.alphabet.remove(var7);
                        this.alphabetList.setListData(this.alphabet.toArray());
                        var2.setEnabled(false);
                    } else {
                        JOptionPane.showMessageDialog(
                                this,
                                "The selected symbols is involved in one or more transitions.\n\nPlease delete these before removing the symbol.",
                                "VisuTuring - Integrity Protect",
                                0);
                    }
                }
            } else if (var2.getName().equals("Remove All")) {
                boolean var8 = false;
                List var4 = new ArrayList();

                for (int var5 = 0; var5 < this.alphabet.size(); ++var5) {
                    char var6 = ((String) this.alphabet.get(var5)).charAt(0);
                    if (this.isStandardSymbol("" + var6) || this.hasTransitions(var6)) {
                        var4.add(new String("" + var6));
                        if (!this.isStandardSymbol("" + var6)) {
                            var8 = true;
                        }
                    }
                }

                this.alphabet = var4;
                this.alphabetList.setListData(this.alphabet.toArray());
                var2.setEnabled(false);
                if (var8) {
                    JOptionPane.showMessageDialog(
                            this,
                            "One or more of the selected symbols was involved\nin one or more transitions.\n\nPlease delete these before removing the symbol(s).",
                            "VisuTuring - Integrity Protect",
                            0);
                }
            } else if (var2.getName().equals("Add {0,1}")) {
                this.addSymbol("0");
                this.addSymbol("1");
                if (!this.removeAllButton.isEnabled()) {
                    this.removeAllButton.setEnabled(true);
                }
            } else if (var2.getName().equals("Add {a,b}")) {
                this.addSymbol("a");
                this.addSymbol("b");
                if (!this.removeAllButton.isEnabled()) {
                    this.removeAllButton.setEnabled(true);
                }
            } else if (var2.getName().equals("Add {0..9}")) {
                this.addSymbol("0");
                this.addSymbol("1");
                this.addSymbol("2");
                this.addSymbol("3");
                this.addSymbol("4");
                this.addSymbol("5");
                this.addSymbol("6");
                this.addSymbol("7");
                this.addSymbol("8");
                this.addSymbol("9");
                if (!this.removeAllButton.isEnabled()) {
                    this.removeAllButton.setEnabled(true);
                }
            }
        }
    }

    public List<String> getAlphabet() {
        return this.alphabet;
    }

    boolean symbolExists(String var1) {
        boolean var2 = false;
        int var3 = 0;

        while (var3 < this.alphabet.size() && !var2) {
            if (var1.equals((String) this.alphabet.get(var3++))) {
                var2 = true;
            }
        }

        return var2;
    }

    void addSymbol(String var1) {
        if (!this.symbolExists(var1)) {
            this.alphabet.add(var1);
        }

        this.alphabetList.setListData(this.alphabet.toArray());
    }

    boolean isStandardSymbol(String var1) {
        boolean var2 = false;
        if (var1.equals("⊳") || var1.equals("⊔")) {
            var2 = true;
        }

        return var2;
    }

    public void valueChanged(ListSelectionEvent var1) {
        int var2 = this.alphabetList.getSelectedIndex();
        if (var2 > -1) {
            String var3 = (String) this.alphabet.get(var2);
            if (this.isStandardSymbol(var3)) {
                this.removeButton.setEnabled(false);
            } else {
                this.removeButton.setEnabled(true);
            }
        }

        if (this.alphabet.size() > 4) {
            if (!this.removeAllButton.isEnabled()) {
                this.removeAllButton.setEnabled(true);
            }
        } else if (this.removeAllButton.isEnabled()) {
            this.removeAllButton.setEnabled(false);
        }
    }
}
