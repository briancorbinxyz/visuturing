package VisuTuring.gui.dialogs;

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

  public SplashWindow(ImageIcon var1, final int var2) {
    super(new Frame());
    Container var3 = this.getContentPane();
    JLabel var4 = new JLabel(var1);
    Dimension var5 = var4.getPreferredSize();
    ++var5.height;
    JLayeredPane var6 = new JLayeredPane();
    var6.setPreferredSize(var5);
    var3.add(var6);
    var4.setVerticalAlignment(0);
    var4.setHorizontalAlignment(0);
    var4.setBounds(0, 0, (int)var4.getPreferredSize().getWidth(), (int)var4.getPreferredSize().getHeight());
    var6.add(var4, JLayeredPane.DEFAULT_LAYER);
    this.progressText = new JTextArea(" ");
    this.progressText.setForeground(Color.WHITE);
    this.progressText.setFont(new Font("Helvetica", 0, 11));
    this.progressText.setBounds(13, 314, 376, 30);
    this.progressText.setOpaque(false);
    var6.add(this.progressText, JLayeredPane.POPUP_LAYER);
    this.pack();
    Dimension var7 = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension var8 = var4.getPreferredSize();
    this.setLocation(var7.width / 2 - var8.width / 2, var7.height / 2 - var8.height / 2);
    this.closerRunner = new Runnable() {
      public void run() {
        SplashWindow.this.close();
      }
    };
    this.waitRunner = new Runnable() {
      public void run() {
        try {
          if (var2 > 0) {
            Thread.sleep((long)var2);
          }

          SwingUtilities.invokeAndWait(SplashWindow.this.closerRunner);
        } catch (Exception var2x) {
          var2x.printStackTrace();
        }

      }
    };
    this.setVisible(true);
  }

  public void setProgressText(String var1) {
    this.progressText.setText(var1);
  }

  public void complete() {
    Thread var1 = new Thread(this.waitRunner, "SplashThread");
    var1.start();
  }

  private void close() {
    this.setVisible(false);
    this.dispose();
  }
}
