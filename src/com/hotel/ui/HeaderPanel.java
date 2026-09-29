package com.hotel.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

/**
 * Minimalist top header bar with branding and system status indicator.
 */
public class HeaderPanel extends JPanel {
    public HeaderPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, UITheme.BORDER),
                new EmptyBorder(16, 24, 16, 24)
        ));

        // Left brand & title
        JPanel titleBox = new JPanel();
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        titleBox.setOpaque(false);

        JLabel brandTitle = new JLabel("LUMINA HOTEL");
        brandTitle.setFont(UITheme.FONT_HEADER);
        brandTitle.setForeground(UITheme.PRIMARY);

        JLabel brandSubtitle = new JLabel("Reservation Management & Guest Services System");
        brandSubtitle.setFont(UITheme.FONT_SUBTITLE);
        brandSubtitle.setForeground(UITheme.TEXT_MUTED);

        titleBox.add(brandTitle);
        titleBox.add(Box.createVerticalStrut(3));
        titleBox.add(brandSubtitle);

        add(titleBox, BorderLayout.WEST);

        // Right status indicator
        JPanel rightBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        rightBox.setOpaque(false);

        JLabel statusDot = new JLabel("● SYSTEM ACTIVE");
        statusDot.setFont(UITheme.FONT_BADGE);
        statusDot.setForeground(UITheme.SUCCESS);

        rightBox.add(statusDot);
        add(rightBox, BorderLayout.EAST);
    }
}