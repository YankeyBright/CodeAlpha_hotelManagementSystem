package com.hotel.ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

/**
 * Clean, modern, minimalist UI theme design system.
 * Custom paints buttons to ensure 100% visible, high-contrast, crisp text across all operating systems.
 */
public final class UITheme {
    private UITheme() {}

    // Minimalist Palette (Slate & Neutral)
    public static final Color BG_APP = new Color(0xF8, 0xFA, 0xFC);      // Slate 50
    public static final Color BG_CARD = Color.WHITE;
    public static final Color BG_CARD_HOVER = new Color(0xF1, 0xF5, 0xF9); // Slate 100

    public static final Color PRIMARY = new Color(0x0F, 0x17, 0x2A);      // Slate 900 (Deep Navy/Black)
    public static final Color PRIMARY_HOVER = new Color(0x33, 0x41, 0x55);// Slate 700
    public static final Color PRIMARY_ACTIVE = new Color(0x02, 0x06, 0x17);// Slate 950

    public static final Color ACCENT = new Color(0x02, 0x84, 0xC7);       // Sky 600
    public static final Color ACCENT_LIGHT = new Color(0xE0, 0xF2, 0xFE); // Sky 100

    public static final Color TEXT_MAIN = new Color(0x0F, 0x17, 0x2A);
    public static final Color TEXT_MUTED = new Color(0x64, 0x74, 0x8B);   // Slate 500
    public static final Color TEXT_INVERTED = Color.WHITE;

    public static final Color BORDER = new Color(0xCB, 0xD5, 0xE1);       // Slate 300
    public static final Color BORDER_FOCUS = new Color(0x02, 0x84, 0xC7); // Sky 600

    public static final Color SUCCESS = new Color(0x05, 0x96, 0x69);      // Emerald 600
    public static final Color SUCCESS_BG = new Color(0xEC, 0xFD, 0xF5);   // Emerald 50
    public static final Color DANGER = new Color(0xDC, 0x26, 0x26);       // Rose/Red 600
    public static final Color DANGER_HOVER = new Color(0xB9, 0x1C, 0x1C); // Red 700

    // Typography
    public static final String FONT_FAMILY = "Segoe UI";
    public static final Font FONT_HEADER = new Font(FONT_FAMILY, Font.BOLD, 18);
    public static final Font FONT_TITLE = new Font(FONT_FAMILY, Font.BOLD, 15);
    public static final Font FONT_SUBTITLE = new Font(FONT_FAMILY, Font.PLAIN, 13);
    public static final Font FONT_BODY = new Font(FONT_FAMILY, Font.PLAIN, 13);
    public static final Font FONT_BODY_BOLD = new Font(FONT_FAMILY, Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font(FONT_FAMILY, Font.PLAIN, 11);
    public static final Font FONT_BADGE = new Font(FONT_FAMILY, Font.BOLD, 11);

    /**
     * Primary CTA Button: Deep Slate background with crisp White text.
     * Custom painted so Windows Look & Feel cannot wash out the background.
     */
    public static JButton primaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BODY_BOLD);
        btn.setForeground(TEXT_INVERTED);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                AbstractButton b = (AbstractButton) c;
                ButtonModel m = b.getModel();

                Color bg = PRIMARY;
                if (m.isPressed()) {
                    bg = PRIMARY_ACTIVE;
                } else if (m.isRollover()) {
                    bg = PRIMARY_HOVER;
                }

                g2.setColor(bg);
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 6, 6);

                // Draw Text
                FontMetrics fm = g2.getFontMetrics(b.getFont());
                int textX = (c.getWidth() - fm.stringWidth(b.getText())) / 2;
                int textY = (c.getHeight() + fm.getAscent() - fm.getDescent()) / 2;

                g2.setFont(b.getFont());
                g2.setColor(TEXT_INVERTED);
                g2.drawString(b.getText(), textX, textY);

                g2.dispose();
            }
        });

        return btn;
    }

    /**
     * Secondary Button: Clean White background with Slate Border and Dark text.
     */
    public static JButton secondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BODY_BOLD);
        btn.setForeground(TEXT_MAIN);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setBorder(new EmptyBorder(10, 18, 10, 18));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                AbstractButton b = (AbstractButton) c;
                ButtonModel m = b.getModel();

                Color bg = Color.WHITE;
                if (m.isPressed()) {
                    bg = new Color(0xE2, 0xE8, 0xF0);
                } else if (m.isRollover()) {
                    bg = BG_CARD_HOVER;
                }

                g2.setColor(bg);
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 6, 6);

                // 1px Border
                g2.setColor(BORDER);
                g2.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 6, 6);

                // Draw Text
                FontMetrics fm = g2.getFontMetrics(b.getFont());
                int textX = (c.getWidth() - fm.stringWidth(b.getText())) / 2;
                int textY = (c.getHeight() + fm.getAscent() - fm.getDescent()) / 2;

                g2.setFont(b.getFont());
                g2.setColor(TEXT_MAIN);
                g2.drawString(b.getText(), textX, textY);

                g2.dispose();
            }
        });

        return btn;
    }

    /**
     * Danger Button: Rose Red background with crisp White text.
     */
    public static JButton dangerButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BODY_BOLD);
        btn.setForeground(TEXT_INVERTED);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setBorder(new EmptyBorder(10, 18, 10, 18));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                AbstractButton b = (AbstractButton) c;
                ButtonModel m = b.getModel();

                Color bg = DANGER;
                if (m.isPressed()) {
                    bg = new Color(0x99, 0x1B, 0x1B);
                } else if (m.isRollover()) {
                    bg = DANGER_HOVER;
                }

                g2.setColor(bg);
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 6, 6);

                FontMetrics fm = g2.getFontMetrics(b.getFont());
                int textX = (c.getWidth() - fm.stringWidth(b.getText())) / 2;
                int textY = (c.getHeight() + fm.getAscent() - fm.getDescent()) / 2;

                g2.setFont(b.getFont());
                g2.setColor(TEXT_INVERTED);
                g2.drawString(b.getText(), textX, textY);

                g2.dispose();
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
        tf.setCaretColor(PRIMARY);
        tf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
        return tf;
    }
}