package com.hotel.ui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Luxury branding emblem for Lumina Hotel & Residences.
 * Loads the ultra-high resolution assets/logo.png asset with procedural vector fallback.
 */
public final class HotelLogo {
    private static final String LOGO_PATH = "assets/logo.png";
    private static BufferedImage cachedLogoFile = null;

    private HotelLogo() {}

    public static final Color OBSIDIAN_DEEP = new Color(0x07, 0x0A, 0x12);
    public static final Color OBSIDIAN_MID = new Color(0x13, 0x1A, 0x2A);
    public static final Color GOLD_BRIGHT = new Color(0xF6, 0xE0, 0x8C);
    public static final Color GOLD_MED = new Color(0xD4, 0xAF, 0x37);
    public static final Color GOLD_DEEP = new Color(0x9E, 0x78, 0x1E);

    /**
     * Renders or scales the luxury Lumina crest at any square pixel size.
     */
    public static BufferedImage renderMark(int size) {
        // 1. Try loading from assets/logo.png
        try {
            if (cachedLogoFile == null) {
                File f = new File(LOGO_PATH);
                if (f.exists()) {
                    cachedLogoFile = ImageIO.read(f);
                }
            }

            if (cachedLogoFile != null) {
                BufferedImage scaled = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = scaled.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Clip to luxury rounded rect
                RoundRectangle2D clip = new RoundRectangle2D.Float(0, 0, size, size, size * 0.22f, size * 0.22f);
                g.setClip(clip);
                g.drawImage(cachedLogoFile, 0, 0, size, size, null);

                // Add 1px subtle gold outline
                g.setClip(null);
                g.setColor(new Color(0xD4, 0xAF, 0x37, 160));
                g.setStroke(new BasicStroke(Math.max(1f, size * 0.02f)));
                g.draw(clip);

                g.dispose();
                return scaled;
            }
        } catch (Exception ignored) {}

        // 2. Procedural Vector Fallback
        return renderProceduralVector(size);
    }

    private static BufferedImage renderProceduralVector(int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        float pad = size * 0.05f;
        float innerSize = size - (2 * pad);
        float radius = size * 0.24f;

        RoundRectangle2D bg = new RoundRectangle2D.Float(pad, pad, innerSize, innerSize, radius, radius);
        GradientPaint bgGrad = new GradientPaint(0, 0, OBSIDIAN_DEEP, size, size, OBSIDIAN_MID);
        g2.setPaint(bgGrad);
        g2.fill(bg);

        GradientPaint goldGrad = new GradientPaint(0, 0, GOLD_BRIGHT, size, size, GOLD_DEEP);
        g2.setPaint(goldGrad);
        g2.setStroke(new BasicStroke(Math.max(1.5f, size * 0.024f)));
        g2.draw(bg);

        float cx = size / 2.0f;
        float cy = size / 2.0f;
        g2.setFont(new Font("Serif", Font.BOLD, (int)(size * 0.44f)));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString("L", (int)(cx - fm.stringWidth("L")/2.0f), (int)(cy + fm.getAscent()/2.8f));

        g2.dispose();
        return img;
    }
}