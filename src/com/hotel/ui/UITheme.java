package com.hotel.ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Clean, modern, minimalist UI theme design system.
 * Uses a refined slate & crisp white palette with sleek typography and subtle flat borders.
 */
public final class UITheme {
    private UITheme() {}

    // Minimalist Palette (Slate & Neutral)
    public static final Color BG_APP = new Color(0xF8, 0xFA, 0xFC);      // Slate 50
    public static final Color BG_CARD = Color.WHITE;
    public static final Color BG_CARD_HOVER = new Color(0xF1, 0xF5, 0xF9); // Slate 100
    public static final Color BG_SIDEBAR = new Color(0x0F, 0x17, 0x2A);   // Midnight Slate 900

    public static final Color PRIMARY = new Color(0x0F, 0x17, 0x2A);      // Slate 900
    public static final Color PRIMARY_HOVER = new Color(0x33, 0x41, 0x55);// Slate 700
    public static final Color ACCENT = new Color(0x02, 0x84, 0xC7);       // Sky 600
    public static final Color ACCENT_LIGHT = new Color(0xE0, 0xF2, 0xFE); // Sky 100

    public static final Color TEXT_MAIN = new Color(0x0F, 0x17, 0x2A);
    public static final Color TEXT_MUTED = new Color(0x64, 0x74, 0x8B);   // Slate 500
    public static final Color TEXT_INVERTED = Color.WHITE;

    public static final Color BORDER = new Color(0xE2, 0xE8, 0xF0);       // Slate 200
    public static final Color BORDER_FOCUS = new Color(0x94, 0xA3, 0xB8); // Slate 400

    public static final Color SUCCESS = new Color(0x05, 0x96, 0x69);      // Emerald 600
    public static final Color SUCCESS_BG = new Color(0xEC, 0xFD, 0xF5);   // Emerald 50
    public static final Color DANGER = new Color(0xE1, 0x1D, 0x48);       // Rose 600
    public static final Color DANGER_BG = new Color(0xFF, 0xF1, 0xF2);    // Rose 50

    // Typography
    public static final String FONT_FAMILY = "Segoe UI";
    public static final Font FONT_HEADER = new Font(FONT_FAMILY, Font.BOLD, 18);
    public static final Font FONT_TITLE = new Font(FONT_FAMILY, Font.BOLD, 15);
    public static final Font FONT_SUBTITLE = new Font(FONT_FAMILY, Font.PLAIN, 13);
    public static final Font FONT_BODY = new Font(FONT_FAMILY, Font.PLAIN, 13);
    public static final Font FONT_BODY_BOLD = new Font(FONT_FAMILY, Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font(FONT_FAMILY, Font.PLAIN, 11);
    public static final Font FONT_BADGE = new Font(FONT_FAMILY, Font.BOLD, 11);

    // Reusable Minimalist Button Factory
    public static JButton primaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BODY_BOLD);
        btn.setForeground(TEXT_INVERTED);
        btn.setBackground(PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(9, 18, 9, 18));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(PRIMARY_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(PRIMARY);
            }
        });
        return btn;
    }

    public static JButton secondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BODY_BOLD);
        btn.setForeground(TEXT_MAIN);
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(8, 16, 8, 16)
        ));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(BG_CARD_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(Color.WHITE);
            }
        });
        return btn;
    }

    public static JButton dangerButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BODY_BOLD);
        btn.setForeground(DANGER);
        btn.setBackground(DANGER_BG);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(0xFE, 0xCD, 0xD3), 1, true),
                new EmptyBorder(8, 16, 8, 16)
        ));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(0xFF, 0xE4, 0xE6));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(DANGER_BG);
            }
        });
        return btn;
    }

    // Flat Minimalist Card Border
    public static Border cardBorder() {
        return BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(16, 16, 16, 16)
        );
    }

    public static JTextField styledTextField(int columns) {
        JTextField tf = new JTextField(columns);
        tf.setFont(FONT_BODY);
        tf.setForeground(TEXT_MAIN);
        tf.setBackground(Color.WHITE);
        tf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
        return tf;
    }
}