package com.hotel.ui;

import com.hotel.service.HotelService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

/**
 * Main application window for Lumina Hotel Reservation System.
 * Features a minimalist, uncluttered dual-tab layout.
 */
public class MainFrame extends JFrame {
    private final HotelService service;
    private final SearchBookPanel searchBookPanel;
    private final ManageReservationsPanel managePanel;

    public MainFrame() {
        super("Lumina Hotel - Reservation Management System");
        this.service = new HotelService();

        // Set procedural high-DPI application icon
        setIconImage(HotelLogo.renderMark(64));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 720);
        setMinimumSize(new Dimension(880, 580));
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(UITheme.BG_APP);
        setContentPane(contentPane);

        // 1. Top Header with Logo
        HeaderPanel header = new HeaderPanel();
        contentPane.add(header, BorderLayout.NORTH);

        // 2. Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UITheme.FONT_BODY_BOLD);
        tabbedPane.setBackground(UITheme.BG_APP);
        tabbedPane.setBorder(new EmptyBorder(8, 12, 8, 12));

        searchBookPanel = new SearchBookPanel(service, this::refreshAllTabs);
        managePanel = new ManageReservationsPanel(service, this::refreshAllTabs);

        tabbedPane.addTab("  Find & Book Rooms  ", searchBookPanel);
        tabbedPane.addTab("  Manage Reservations  ", managePanel);

        contentPane.add(tabbedPane, BorderLayout.CENTER);

        // 3. Status Bar
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(Color.WHITE);
        statusBar.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 0, 0, 0, UITheme.BORDER),
                new EmptyBorder(8, 20, 8, 20)
        ));

        JLabel info = new JLabel("Lumina Hotel Management v1.0 • File-based Data Storage (CSV)");
        info.setFont(UITheme.FONT_SMALL);
        info.setForeground(UITheme.TEXT_MUTED);

        JLabel copyright = new JLabel("CodeAlpha Internship Task 4");
        copyright.setFont(UITheme.FONT_SMALL);
        copyright.setForeground(UITheme.TEXT_MUTED);

        statusBar.add(info, BorderLayout.WEST);
        statusBar.add(copyright, BorderLayout.EAST);
        contentPane.add(statusBar, BorderLayout.SOUTH);
    }

    private void refreshAllTabs() {
        searchBookPanel.performSearch();
        managePanel.reloadTable();
    }
}