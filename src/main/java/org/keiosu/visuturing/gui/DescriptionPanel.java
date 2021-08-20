package org.keiosu.visuturing.gui;

import org.keiosu.visuturing.core.TuringMachine;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

public class DescriptionPanel extends JPanel {

  private final TuringMachine machine;

  private AttributedCharacterIterator paragraph;

  public DescriptionPanel(TuringMachine machine) {
    this.machine = machine;
  }

  public void paintComponent(Graphics graphics) {
    Graphics2D canvas = (Graphics2D)graphics;
    canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    applyPaint(canvas);
    Rectangle clipBounds = canvas.getClipBounds();
    canvas.fill(clipBounds);
    canvas.setFont(new Font("Helvetica", Font.PLAIN, 100));
    canvas.setColor(new Color(237, 242, 250));
    TextLayout largeMachineName = new TextLayout(this.machine.getName(), new Font("Helvetica", Font.PLAIN, 100), canvas.getFontRenderContext());
    canvas.setClip(0, 0, (int) clipBounds.getWidth(), 75);
    AffineTransform transform = new AffineTransform();
    transform.setToTranslation((float) this.getWidth() - (float) largeMachineName.getBounds().getWidth(), 90.0D);
    transform.rotate(Math.toRadians(-3.0D));
    canvas.fill(largeMachineName.getOutline(transform));
    canvas.setClip(clipBounds);
    canvas.setStroke(new BasicStroke(2.0F));
    canvas.drawLine(0, 75, (int)clipBounds.getWidth(), 75);
    canvas.setColor(new Color(177, 194, 217));
    transform.setToTranslation((float) this.getWidth() - (float) new TextLayout(this.machine.getName(), new Font("Helvetica", Font.PLAIN, 50), canvas.getFontRenderContext()).getBounds().getWidth() - 50.0F, 50.0D);
    canvas.fill(new TextLayout(this.machine.getName(), new Font("Helvetica", Font.PLAIN, 50), canvas.getFontRenderContext()).getOutline(transform));
    canvas.setColor(Color.BLACK);
    this.paragraph = asAttributedString(machine.getDescription()).getIterator();
    LineBreakMeasurer measurer = new LineBreakMeasurer(this.paragraph, canvas.getFontRenderContext());
    measurer.setPosition(this.paragraph.getBeginIndex());
    applyParagraph(canvas, measurer, (float) (this.getWidth() - 200));
  }

  private void applyParagraph(Graphics2D canvas, LineBreakMeasurer measurer, float wrappingWidth) {
    TextLayout nextLine;
    float yOffset = 100.0f;
    for(int endIndex = this.paragraph.getEndIndex(); measurer.getPosition() < endIndex; yOffset += nextLine.getDescent() + nextLine.getLeading()) {
      nextLine = measurer.nextLayout(wrappingWidth);
      yOffset += nextLine.getAscent() * 2.0F;
      nextLine.draw(canvas, nextLine.isLeftToRight() ? (float) 200.0 : wrappingWidth - nextLine.getAdvance(), yOffset);
    }
  }

  private void applyPaint(Graphics2D canvas) {
    canvas.setPaint(new GradientPaint(0.0F, 0.0F, new Color(204, 218, 234), 165.0F, 0.0F, Color.white));
  }

  private AttributedString asAttributedString(String text) {
    AttributedString attributedText = new AttributedString(text);
    attributedText.addAttribute(TextAttribute.FONT, new Font("Helvetica", Font.PLAIN, 15));
    return attributedText;
  }
}
