package com.hotel.ui;

import com.hotel.model.*;
import com.hotel.service.HotelService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel to manage reservations, cancel bookings, and print invoices.
 */
public class ManageReservationsPanel extends JPanel {
    private final HotelService service;
    private final ReservationTableModel tableModel;
    private final JTable table;
    private final JTextField searchField;
    private final JLabel countLabel;

    public ManageReservationsPanel(HotelService service, Runnable onDataChanged) {
        this.service = service;
        setLayout(new BorderLayout(0, 16));
        setBackground(UITheme.BG_APP);
        setBorder(new EmptyBorder(20, 24, 20, 24));

        // 1. Top Search Bar
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
        searchBar.setBackground(Color.WHITE);
        searchBar.setBorder(UITheme.cardBorder());

        searchField = UITheme.styledTextField(20);
        searchField.setToolTipText("Filter by Guest Name or Reservation ID");

        JButton searchBtn = UITheme.primaryButton("Filter");
        searchBtn.addActionListener(e -> filterData());

        JButton clearBtn = UITheme.secondaryButton("Clear Filter");
        clearBtn.addActionListener(e -> {
            searchField.setText("");
            reloadTable();
        });

        searchBar.add(new JLabel("Search Bookings:"));
        searchBar.add(searchField);
        searchBar.add(searchBtn);
        searchBar.add(clearBtn);

        add(searchBar, BorderLayout.NORTH);

        // 2. Table
        tableModel = new ReservationTableModel();
        table = new JTable(tableModel);
        table.setRowHeight(36);
        table.setFont(UITheme.FONT_BODY);
        table.getTableHeader().setFont(UITheme.FONT_BODY_BOLD);
        table.getTableHeader().setBackground(UITheme.BG_APP);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(UITheme.cardBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // 3. Footer Bar
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);

        countLabel = new JLabel("0 reservations");
        countLabel.setFont(UITheme.FONT_SUBTITLE);
        countLabel.setForeground(UITheme.TEXT_MUTED);

        JPanel btnBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        btnBox.setOpaque(false);

        JButton invoiceBtn = UITheme.secondaryButton("Print / Save Invoice");
        invoiceBtn.addActionListener(e -> handlePrintInvoice());

        JButton cancelBtn = UITheme.dangerButton("Cancel Reservation");
        cancelBtn.addActionListener(e -> handleCancelReservation(onDataChanged));

        btnBox.add(invoiceBtn);
        btnBox.add(cancelBtn);

        footer.add(countLabel, BorderLayout.WEST);
        footer.add(btnBox, BorderLayout.EAST);
        add(footer, BorderLayout.SOUTH);

        reloadTable();
    }

    public void reloadTable() {
        List<Reservation> list = service.getAllReservations();
        tableModel.setReservations(list);
        countLabel.setText("Total: " + list.size() + " reservation(s)");
    }

    private void filterData() {
        String query = searchField.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            reloadTable();
            return;
        }

        List<Reservation> filtered = new ArrayList<>();
        for (Reservation res : service.getAllReservations()) {
            if (res.getReservationId().toLowerCase().contains(query) ||
                res.getGuest().getFullName().toLowerCase().contains(query) ||
                res.getGuest().getPhone().contains(query)) {
                filtered.add(res);
            }
        }
        tableModel.setReservations(filtered);
        countLabel.setText("Found: " + filtered.size() + " matching reservation(s)");
    }

    private void handlePrintInvoice() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a reservation to generate an invoice.", "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Reservation res = tableModel.getReservationAt(row);
        if (res == null) return;

        List<Payment> payments = service.getPaymentsForReservation(res.getReservationId());
        File file = InvoiceGenerator.generateInvoiceHtml(res, payments);
        InvoiceGenerator.openInvoiceInBrowser(file);
    }

    private void handleCancelReservation(Runnable onDataChanged) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a reservation to cancel.", "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Reservation res = tableModel.getReservationAt(row);
        if (res == null) return;

        if (res.getStatus() == ReservationStatus.CANCELLED) {
            JOptionPane.showMessageDialog(this, "This reservation has already been cancelled.", "Already Cancelled", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel reservation #" + res.getReservationId() + " for " + res.getGuest().getFullName() + "?",
                "Confirm Cancellation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = service.cancelReservation(res.getReservationId());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Reservation cancelled successfully.", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                reloadTable();
                if (onDataChanged != null) onDataChanged.run();
            }
        }
    }
}