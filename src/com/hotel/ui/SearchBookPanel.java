package com.hotel.ui;

import com.hotel.model.*;
import com.hotel.service.HotelService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.util.List;

/**
 * Clean room search, category filter, and booking workflow panel.
 */
public class SearchBookPanel extends JPanel {
    private final HotelService service;
    private final RoomTableModel tableModel;
    private final JTable table;

    private final JComboBox<String> typeFilterCombo;
    private final JTextField checkInField;
    private final JTextField checkOutField;
    private final JLabel resultCountLabel;

    public SearchBookPanel(HotelService service, Runnable onDataChanged) {
        this.service = service;
        setLayout(new BorderLayout(0, 16));
        setBackground(UITheme.BG_APP);
        setBorder(new EmptyBorder(20, 24, 20, 24));

        // 1. Top Search Filter Card
        JPanel filterCard = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
        filterCard.setBackground(Color.WHITE);
        filterCard.setBorder(UITheme.cardBorder());

        typeFilterCombo = new JComboBox<>(new String[]{"All Categories", "Standard Room", "Deluxe Room", "Executive Suite", "Family Suite"});
        typeFilterCombo.setFont(UITheme.FONT_BODY);
        typeFilterCombo.setBackground(Color.WHITE);
        typeFilterCombo.setPreferredSize(new Dimension(170, 36));

        checkInField = UITheme.styledTextField(10);
        checkInField.setText(LocalDate.now().toString());
        checkInField.setPreferredSize(new Dimension(120, 36));

        checkOutField = UITheme.styledTextField(10);
        checkOutField.setText(LocalDate.now().plusDays(2).toString());
        checkOutField.setPreferredSize(new Dimension(120, 36));

        JButton searchBtn = UITheme.primaryButton("Find Available");
        searchBtn.addActionListener(e -> performSearch());

        JButton resetBtn = UITheme.secondaryButton("Show All");
        resetBtn.addActionListener(e -> {
            typeFilterCombo.setSelectedIndex(0);
            checkInField.setText(LocalDate.now().toString());
            checkOutField.setText(LocalDate.now().plusDays(2).toString());
            performSearch();
        });

        filterCard.add(createLabeledGroup("Room Category:", typeFilterCombo));
        filterCard.add(createLabeledGroup("Check-In (YYYY-MM-DD):", checkInField));
        filterCard.add(createLabeledGroup("Check-Out (YYYY-MM-DD):", checkOutField));
        filterCard.add(searchBtn);
        filterCard.add(resetBtn);

        add(filterCard, BorderLayout.NORTH);

        // 2. Room Table
        tableModel = new RoomTableModel();
        table = new JTable(tableModel);
        table.setRowHeight(38);
        table.setFont(UITheme.FONT_BODY);
        table.getTableHeader().setFont(UITheme.FONT_BODY_BOLD);
        table.getTableHeader().setBackground(new Color(0xF1, 0xF5, 0xF9));
        table.getTableHeader().setForeground(UITheme.TEXT_MAIN);
        table.getTableHeader().setPreferredSize(new Dimension(0, 36));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(0xF1, 0xF5, 0xF9));
        table.setSelectionBackground(UITheme.ACCENT_LIGHT);
        table.setSelectionForeground(UITheme.TEXT_MAIN);

        // Custom Cell Padding
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean isSel, boolean hasFoc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, isSel, hasFoc, row, col);
                setBorder(new EmptyBorder(0, 12, 0, 12));
                return c;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(90);  // Room #
        table.getColumnModel().getColumn(1).setPreferredWidth(160); // Category
        table.getColumnModel().getColumn(2).setPreferredWidth(120); // Rate
        table.getColumnModel().getColumn(3).setPreferredWidth(90);  // Floor
        table.getColumnModel().getColumn(4).setPreferredWidth(350); // Amenities

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(UITheme.cardBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(scrollPane, BorderLayout.CENTER);

        // 3. Footer Bar
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);

        resultCountLabel = new JLabel("Loading inventory...");
        resultCountLabel.setFont(UITheme.FONT_SUBTITLE);
        resultCountLabel.setForeground(UITheme.TEXT_MUTED);

        JPanel btnBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        btnBox.setOpaque(false);

        JButton bookBtn = UITheme.primaryButton("Book Selected Room");
        bookBtn.addActionListener(e -> handleBookSelected(onDataChanged));

        btnBox.add(bookBtn);

        footer.add(resultCountLabel, BorderLayout.WEST);
        footer.add(btnBox, BorderLayout.EAST);
        add(footer, BorderLayout.SOUTH);

        performSearch();
    }

    private JPanel createLabeledGroup(String label, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setFont(UITheme.FONT_SMALL);
        l.setForeground(UITheme.TEXT_MUTED);
        p.add(l, BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    public void performSearch() {
        try {
            LocalDate in = LocalDate.parse(checkInField.getText().trim());
            LocalDate out = LocalDate.parse(checkOutField.getText().trim());

            if (!out.isAfter(in)) {
                JOptionPane.showMessageDialog(this, "Check-out date must be after check-in date.", "Invalid Dates", JOptionPane.WARNING_MESSAGE);
                return;
            }

            RoomType cat = null;
            int idx = typeFilterCombo.getSelectedIndex();
            if (idx == 1) cat = RoomType.STANDARD;
            else if (idx == 2) cat = RoomType.DELUXE;
            else if (idx == 3) cat = RoomType.SUITE;
            else if (idx == 4) cat = RoomType.FAMILY;

            List<Room> available = service.searchAvailableRooms(cat, in, out);
            tableModel.setRooms(available);
            resultCountLabel.setText("Found " + available.size() + " available room(s) for selected dates");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid dates in YYYY-MM-DD format.", "Date Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleBookSelected(Runnable onDataChanged) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a room from the table to proceed.", "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Room room = tableModel.getRoomAt(row);
        if (room == null) return;

        LocalDate in = LocalDate.parse(checkInField.getText().trim());
        LocalDate out = LocalDate.parse(checkOutField.getText().trim());

        Window win = SwingUtilities.getWindowAncestor(this);
        BookingDialog dlg = new BookingDialog(win, service, room, in, out);
        dlg.setVisible(true);

        Reservation res = dlg.getCreatedReservation();
        if (res != null) {
            // Prompt payment simulation
            PaymentDialog payDlg = new PaymentDialog(win, service, res);
            payDlg.setVisible(true);

            // Generate invoice
            File invoiceFile = InvoiceGenerator.generateInvoiceHtml(res, service.getPaymentsForReservation(res.getReservationId()));
            int choice = JOptionPane.showConfirmDialog(this,
                    "Reservation " + res.getReservationId() + " successfully confirmed!\nWould you like to open the printable invoice now?",
                    "Booking Complete", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {
                InvoiceGenerator.openInvoiceInBrowser(invoiceFile);
            }

            performSearch();
            if (onDataChanged != null) onDataChanged.run();
        }
    }
}