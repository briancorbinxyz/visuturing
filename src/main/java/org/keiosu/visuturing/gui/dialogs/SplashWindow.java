package org.keiosu.visuturing.gui.dialogs;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTextArea;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

public class SplashWindow extends JWindow {
  private JTextArea progressText;
  private Runnable waitRunner;
  private final Runnable closerRunner;

  public SplashWindow(ImageIcon imageIcon, final long closeDelaySecs) {
    super(new Frame());
    Container contentPane = this.getContentPane();
    JLabel label = new JLabel(imageIcon);
    Dimension size = label.getPreferredSize();
    ++size.height;
    JLayeredPane pane = new JLayeredPane();
    pane.setPreferredSize(size);
    contentPane.add(pane);
    label.setVerticalAlignment(0);
    label.setHorizontalAlignment(0);
    label.setBounds(0, 0, (int)label.getPreferredSize().getWidth(), (int)label.getPreferredSize().getHeight());
    pane.add(label, JLayeredPane.DEFAULT_LAYER);
    this.progressText = new JTextArea(" ");
    this.progressText.setForeground(Color.WHITE);
    this.progressText.setFont(new Font("Helvetica", Font.PLAIN, 11));
    this.progressText.setBounds(13, 314, 376, 30);
    this.progressText.setOpaque(false);
    pane.add(this.progressText, JLayeredPane.POPUP_LAYER);
    this.pack();
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension labelSize = label.getPreferredSize();
    this.setLocation(screenSize.width / 2 - labelSize.width / 2, screenSize.height / 2 - labelSize.height / 2);
    this.closerRunner = SplashWindow.this::close;
    this.waitRunner = () -> {
      try {
        if (closeDelaySecs > 0) {
          Thread.sleep(closeDelaySecs);
        }

        SwingUtilities.invokeAndWait(SplashWindow.this.closerRunner);
      } catch (Exception var2x) {
        var2x.printStackTrace();
      }

    };
    this.setVisible(true);
  }

  public void setProgressText(String var1) {
    this.progressText.setText(var1);
  }

  public void complete() {
    new Thread(this.waitRunner, "SplashThread").start();
  }

  private void close() {
    this.setVisible(false);
    this.dispose();
  }
}
