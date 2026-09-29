package com.hotel;

import com.hotel.ui.MainFrame;

import javax.swing.*;

/**
 * Application entry point for Lumina Hotel Reservation System.
 */
public class Main {
    public static void main(String[] args) {
        // Set cross-platform clean Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}