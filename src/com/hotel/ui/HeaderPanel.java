package com.hotel.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

/**
 * Minimalist luxury top header bar with procedural gold crest, branding, and reception status.
 */
public class HeaderPanel extends JPanel {
    public HeaderPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, UITheme.BORDER),
                new EmptyBorder(14, 24, 14, 24)
        ));

        // Left brand & logo box
        JPanel leftBrandBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        leftBrandBox.setOpaque(false);

        // Procedural Luxury Hotel Crest Icon (46x46)
        JLabel logoLabel = new JLabel(new ImageIcon(HotelLogo.renderMark(48)));
        leftBrandBox.add(logoLabel);

        // Titles
        JPanel titleBox = new JPanel();
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        titleBox.setOpaque(false);

        JLabel brandTitle = new JLabel("LUMINA HOTEL & RESIDENCES");
        brandTitle.setFont(UITheme.FONT_HEADER);
        brandTitle.setForeground(UITheme.PRIMARY);

        JLabel brandSubtitle = new JLabel("Luxury Hospitality & Guest Services • Accra, Ghana");
        brandSubtitle.setFont(UITheme.FONT_SUBTITLE);
        brandSubtitle.setForeground(UITheme.TEXT_MUTED);

        titleBox.add(brandTitle);
        titleBox.add(Box.createVerticalStrut(2));
        titleBox.add(brandSubtitle);

        leftBrandBox.add(titleBox);
        add(leftBrandBox, BorderLayout.WEST);

        // Right status indicator
        JPanel rightBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        rightBox.setOpaque(false);

        JLabel statusDot = new JLabel("● SYSTEM ACTIVE");
        statusDot.setFont(UITheme.FONT_BADGE);
        statusDot.setForeground(UITheme.SUCCESS);

        rightBox.add(statusDot);
        add(rightBox, BorderLayout.EAST);
    }
}