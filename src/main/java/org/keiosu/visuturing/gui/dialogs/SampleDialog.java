package org.keiosu.visuturing.gui.dialogs;

import org.keiosu.visuturing.persistence.VisuTuringFileFilter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SampleDialog extends AbstractDialog implements ListSelectionListener, MouseListener {
  private JList<String> mylist;
  private String selectedFile = null;
  private static final String SAMPLE_DIR = "samples/";

  public SampleDialog() {
    super(new Frame(), "Select a Sample Turing Machine");
    ImageIcon[] var1 = new ImageIcon[2];
    JPanel var2 = new JPanel(new BorderLayout());
    String[] fileList = this.getFileList();
    if (fileList != null && fileList.length > 0) {
      this.mylist = new JList<>(fileList);
    } else {
      this.mylist = new JList<>();
    }

    this.mylist.setSelectionMode(0);
    this.mylist.setVisibleRowCount(-1);
    this.mylist.setCellRenderer(new SampleDialog.MyCellRenderer());
    this.mylist.addMouseListener(this);
    this.mylist.setFont(new Font("arial", 0, 12));
    this.mylist.setLayoutOrientation(1);
    this.mylist.setFixedCellWidth(128);
    this.mylist.setFixedCellHeight(128);
    JScrollPane var4 = new JScrollPane(this.mylist);
    var4.setPreferredSize(new Dimension(256, 300));
    var4.setBorder(BorderFactory.createBevelBorder(1));
    var2.add(var4, "Center");
    var2.add(new JLabel(this.createImageIcon("bitmaps/samples.gif")), "West");
    this.mylist.addListSelectionListener(this);
    var2.setBorder(BorderFactory.createEtchedBorder(0));
    this.init(var2);
  }

  private String[] getFileList() {
    VisuTuringFileFilter fileFilter = new VisuTuringFileFilter();
    fileFilter.addExtension("vt");

    try {
      URL sampleUrl = Objects.requireNonNull(SampleDialog.class.getClassLoader().getResource(SampleDialog.SAMPLE_DIR));
      Path path = Paths.get(sampleUrl.toURI());
      File directory = path.toFile();
      return directory.isDirectory() ? directory.list(fileFilter) : new String []{};
    } catch (Exception ignore) {
      return new String[]{};
    }
  }

  public void mouseClicked(MouseEvent var1) {
    if (var1.getClickCount() == 2) {
      int var2 = this.mylist.locationToIndex(var1.getPoint());
      this.mylist.setSelectedIndex(var2);
      this.selectedFile = (String)this.mylist.getSelectedValue();
      this.cancelled = false;
      this.setVisible(false);
    }

  }

  public File getSelectedFile() {
    try {
      URL sampleUrl = Objects.requireNonNull(SampleDialog.class.getClassLoader().getResource(SampleDialog.SAMPLE_DIR));
      Path path = null;
      path = Paths.get(sampleUrl.toURI()).resolve(this.selectedFile);
      return path.toFile();
    } catch (URISyntaxException e) {
      return null;
    }
  }

  public void valueChanged(ListSelectionEvent var1) {
    JList var2 = (JList)var1.getSource();
    int var3 = var2.getSelectedIndex();
    if (var3 != -1 && !var1.getValueIsAdjusting()) {
      this.selectedFile = (String)var2.getSelectedValue();
    }

  }

  public ImageIcon createImageIcon(String var1) {
    return new ImageIcon(this.getClass().getClassLoader().getResource(var1));
  }

  public void mousePressed(MouseEvent var1) {
  }

  public void mouseReleased(MouseEvent var1) {
  }

  public void mouseEntered(MouseEvent var1) {
  }

  public void mouseExited(MouseEvent var1) {
  }

  class MyCellRenderer extends JLabel implements ListCellRenderer {
    ImageIcon icon = SampleDialog.this.createImageIcon("bitmaps/vt.gif");

    MyCellRenderer() {
    }

    public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
      String var6 = var2.toString();
      this.setText(var6);
      this.setIcon(this.icon);
      this.setHorizontalAlignment(0);
      this.setHorizontalTextPosition(0);
      this.setVerticalTextPosition(3);
      if (var4) {
        this.setBackground(var1.getSelectionBackground());
        this.setForeground(Color.RED);
      } else {
        this.setBackground(var1.getBackground());
        this.setForeground(var1.getForeground());
      }

      this.setEnabled(var1.isEnabled());
      this.setFont(var1.getFont());
      return this;
    }
  }
}
