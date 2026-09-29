package com.hotel.ui;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

/**
 * Procedural vector icon generator for Lumina Hotel.
 * Renders high-DPI crisp badges and icons in pure code with zero external image asset dependencies.
 */
public final class HotelLogo {
    private HotelLogo() {}

    public static final Color SLATE_DARK = new Color(0x0F, 0x17, 0x2A);
    public static final Color SLATE_MID = new Color(0x1E, 0x29, 0x3B);
    public static final Color SKY_ACCENT = new Color(0x02, 0x84, 0xC7);
    public static final Color GOLD_ACCENT = new Color(0xF5, 0x9E, 0x0B);

    /**
     * Renders the Lumina emblem at the specified square size with full antialiasing.
     */
    public static BufferedImage renderMark(int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        float pad = size * 0.05f;
        float innerSize = size - (2 * pad);
        float radius = size * 0.22f;

        // Background rounded container with subtle gradient
        RoundRectangle2D bg = new RoundRectangle2D.Float(pad, pad, innerSize, innerSize, radius, radius);
        GradientPaint grad = new GradientPaint(0, 0, SLATE_DARK, size, size, SLATE_MID);
        g2.setPaint(grad);
        g2.fill(bg);

        // 1px Subtle Border
        g2.setColor(new Color(0x33, 0x41, 0x55));
        g2.setStroke(new BasicStroke(Math.max(1f, size * 0.02f)));
        g2.draw(bg);

        // Draw Minimalist Architectural Hotel Silhouette
        float cx = size / 2.0f;
        float bWidth = size * 0.46f;
        float bHeight = size * 0.48f;
        float bLeft = cx - (bWidth / 2.0f);
        float bBottom = size * 0.80f;
        float bTop = bBottom - bHeight;

        // Building base
        g2.setColor(Color.WHITE);
        RoundRectangle2D building = new RoundRectangle2D.Float(bLeft, bTop, bWidth, bHeight, size * 0.06f, size * 0.06f);
        g2.fill(building);

        // Archway Door
        float doorW = bWidth * 0.34f;
        float doorH = bHeight * 0.42f;
        float doorX = cx - (doorW / 2.0f);
        float doorY = bBottom - doorH;

        g2.setColor(SLATE_DARK);
        RoundRectangle2D door = new RoundRectangle2D.Float(doorX, doorY, doorW, doorH, doorW * 0.5f, doorW * 0.5f);
        g2.fill(door);

        // Elegant Windows grid
        g2.setColor(SLATE_DARK);
        float winW = bWidth * 0.16f;
        float winH = bHeight * 0.14f;
        float winSpacingX = bWidth * 0.12f;

        for (int r = 0; r < 2; r++) {
            float wy = bTop + (size * 0.08f) + (r * (winH + size * 0.04f));
            // left window
            g2.fill(new RoundRectangle2D.Float(bLeft + winSpacingX, wy, winW, winH, 2, 2));
            // right window
            g2.fill(new RoundRectangle2D.Float(bLeft + bWidth - winSpacingX - winW, wy, winW, winH, 2, 2));
        }

        // Star / Diamond Crown on top (Sky Blue / Amber)
        g2.setColor(GOLD_ACCENT);
        float starY = bTop - (size * 0.10f);
        float starR = size * 0.06f;

        Path2D.Float star = new Path2D.Float();
        star.moveTo(cx, starY - starR);
        star.lineTo(cx + (starR * 0.65f), starY);
        star.lineTo(cx, starY + starR);
        star.lineTo(cx - (starR * 0.65f), starY);
        star.closePath();
        g2.fill(star);

        g2.dispose();
        return img;
    }
}