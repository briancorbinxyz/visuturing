package VisuTuring.gui;

import VisuTuring.core.TuringMachine;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import javax.swing.JPanel;

public class DescriptionPanel extends JPanel {
  TuringMachine machine;
  AttributedCharacterIterator paragraph;

  public DescriptionPanel(TuringMachine var1) {
    this.machine = var1;
    String var2 = var1.getDescription();
    Font var3 = new Font("Helvetica", 0, 15);
    AttributedString var4 = new AttributedString(var2);
    var4.addAttribute(TextAttribute.FONT, var3);
    this.paragraph = var4.getIterator();
  }

  public void paintComponent(Graphics var1) {
    Graphics2D var2 = (Graphics2D)var1;
    var2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    GradientPaint var3 = new GradientPaint(0.0F, 0.0F, new Color(204, 218, 234), 165.0F, 0.0F, Color.white);
    int var4 = this.getWidth();
    var2.setPaint(var3);
    Rectangle var5 = var2.getClipBounds();
    var2.fill(var5);
    String var6 = this.machine.getName();
    String var7 = this.machine.getDescription();
    Font var8 = new Font("Helvetica", 0, 100);
    var2.setFont(var8);
    FontRenderContext var9 = var2.getFontRenderContext();
    var2.setColor(new Color(237, 242, 250));
    String var10 = new String(var6);
    TextLayout var11 = new TextLayout(var10, var8, var9);
    var2.setClip(0, 0, (int)var5.getWidth(), 75);
    float var12 = (float)var11.getBounds().getWidth();
    AffineTransform var13 = new AffineTransform();
    var13.setToTranslation((double)((float)var4 - var12), 90.0D);
    var13.rotate(Math.toRadians(-3.0D));
    Shape var14 = var11.getOutline(var13);
    var2.fill(var14);
    var2.setClip(var5);
    var2.setStroke(new BasicStroke(2.0F));
    var2.drawLine(0, 75, (int)var5.getWidth(), 75);
    var8 = new Font("Helvetica", 0, 50);
    var2.setColor(new Color(177, 194, 217));
    var10 = new String(var6);
    var11 = new TextLayout(var10, var8, var9);
    var12 = (float)var11.getBounds().getWidth();
    var13.setToTranslation((double)((float)var4 - var12 - 50.0F), 50.0D);
    var14 = var11.getOutline(var13);
    var2.fill(var14);
    var2.setColor(Color.BLACK);
    var9 = var2.getFontRenderContext();
    var8 = new Font("Helvetica", 0, 15);
    AttributedString var15 = new AttributedString(var7);
    var15.addAttribute(TextAttribute.FONT, var8);
    this.paragraph = var15.getIterator();
    LineBreakMeasurer var16 = new LineBreakMeasurer(this.paragraph, var9);
    var16.setPosition(this.paragraph.getBeginIndex());
    float var17 = 100.0F;
    short var18 = 200;
    int var19 = this.getWidth() - var18;

    TextLayout var21;
    for(int var20 = this.paragraph.getEndIndex(); var16.getPosition() < var20; var17 += var21.getDescent() + var21.getLeading()) {
      var21 = var16.nextLayout((float)var19);
      var17 += var21.getAscent() * 2.0F;
      float var22;
      if (var21.isLeftToRight()) {
        var22 = (float)var18;
      } else {
        var22 = (float)var19 - var21.getAdvance();
      }

      var21.draw(var2, var22, var17);
    }

  }
}
