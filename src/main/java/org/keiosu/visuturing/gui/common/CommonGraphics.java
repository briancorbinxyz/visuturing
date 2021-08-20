package org.keiosu.visuturing.gui.common;

import java.awt.*;
import javax.swing.*;

public class CommonGraphics {
    public static JButton newButtonWithHand(String name) {
        JButton button = new JButton();
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setToolTipText(name);
        button.setName(name);
        button.setOpaque(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        return button;
    }
}
