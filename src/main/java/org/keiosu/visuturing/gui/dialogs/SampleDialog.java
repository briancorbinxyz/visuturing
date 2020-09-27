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
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import static java.util.Objects.requireNonNull;

public class SampleDialog extends AbstractDialog implements ListSelectionListener, MouseListener {
  private JList<String> sampleNames;
  private String selectedFile = null;
  private static final String SAMPLE_DIR = "samples/";

  public SampleDialog() {
    super(new Frame(), "Select a Sample Turing Machine");
    JPanel panel = new JPanel(new BorderLayout());
    String[] fileList = getFileList();
    if (fileList != null && fileList.length > 0) {
      sampleNames = new JList<>(fileList);
    } else {
      sampleNames = new JList<>();
    }

    sampleNames.setSelectionMode(0);
    sampleNames.setVisibleRowCount(-1);
    sampleNames.setCellRenderer(new SampleListCellRenderer());
    sampleNames.addMouseListener(this);
    sampleNames.setFont(new Font("arial", Font.PLAIN, 12));
    sampleNames.setLayoutOrientation(1);
    sampleNames.setFixedCellWidth(128);
    sampleNames.setFixedCellHeight(128);
    JScrollPane scrollPane = new JScrollPane(sampleNames);
    scrollPane.setPreferredSize(new Dimension(256, 300));
    scrollPane.setBorder(BorderFactory.createBevelBorder(1));
    panel.add(scrollPane, "Center");
    panel.add(new JLabel(createImageIcon("bitmaps/samples.gif")), "West");
    sampleNames.addListSelectionListener(this);
    panel.setBorder(BorderFactory.createEtchedBorder(0));
    init(panel);
  }

  private String[] getFileList() {
    VisuTuringFileFilter fileFilter = new VisuTuringFileFilter();
    fileFilter.addExtension("vt");

    try {
      URL sampleUrl = requireNonNull(SampleDialog.class.getClassLoader().getResource(SampleDialog.SAMPLE_DIR));
      Path path = Paths.get(sampleUrl.toURI());
      File directory = path.toFile();
      return directory.isDirectory() ? directory.list(fileFilter) : new String []{};
    } catch (Exception ignore) {
      return new String[]{};
    }
  }

  public void mouseClicked(MouseEvent event) {
    if (event.getClickCount() == 2) {
      var sampleListIndex = sampleNames.locationToIndex(event.getPoint());
      sampleNames.setSelectedIndex(sampleListIndex);
      selectedFile = sampleNames.getSelectedValue();
      cancelled = false;
      setVisible(false);
    }

  }

  public File getSelectedFile() {
    try {
      URL sampleUrl = requireNonNull(SampleDialog.class.getClassLoader().getResource(SampleDialog.SAMPLE_DIR));
      return Paths.get(sampleUrl.toURI()).resolve(selectedFile).toFile();
    } catch (URISyntaxException e) {
      return null;
    }
  }

  public void valueChanged(ListSelectionEvent event) {
    if (event.getValueIsAdjusting()) {
      return;
    }

    @SuppressWarnings("unchecked")
    var list = (JList<String>) event.getSource();
    int selectedIndex = list.getSelectedIndex();
    if (selectedIndex != -1) {
      selectedFile = list.getSelectedValue();
    }
  }

  public ImageIcon createImageIcon(String imageUri) {
    return new ImageIcon(requireNonNull(getClass().getClassLoader().getResource(imageUri)));
  }

  public void mousePressed(MouseEvent event) {
  }

  public void mouseReleased(MouseEvent event) {
  }

  public void mouseEntered(MouseEvent event) {
  }

  public void mouseExited(MouseEvent event) {
  }

  class SampleListCellRenderer extends JLabel implements ListCellRenderer<String> {
    ImageIcon icon = SampleDialog.this.createImageIcon("bitmaps/vt.gif");

    public Component getListCellRendererComponent(JList list, String text, int index, boolean selected, boolean cellHasFocus) {
      setText(text);
      setIcon(icon);
      setHorizontalAlignment(0);
      setHorizontalTextPosition(0);
      setVerticalTextPosition(3);
      if (selected) {
        setBackground(list.getSelectionBackground());
        setForeground(Color.RED);
      } else {
        setBackground(list.getBackground());
        setForeground(list.getForeground());
      }
      setEnabled(list.isEnabled());
      setFont(list.getFont());
      return this;
    }
  }
}
