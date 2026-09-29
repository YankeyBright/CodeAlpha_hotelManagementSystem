package com.hotel.ui;

import com.hotel.model.*;
import com.hotel.service.HotelService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Minimalist modal dialog for completing a room reservation.
 * Features instant real-time estimate calculation as dates are entered.
 */
public class BookingDialog extends JDialog {
    private final HotelService service;
    private final Room selectedRoom;
    private Reservation createdReservation = null;

    private final JTextField inDateField;
    private final JTextField outDateField;
    private final JTextField nameField;
    private final JTextField phoneField;
    private final JTextField emailField;
    private final JTextField notesField;

    private final JLabel estimateNightsLabel;
    private final JLabel estimateTotalLabel;
    private final JLabel estimateBreakdownLabel;

    public BookingDialog(Window owner, HotelService service, Room room, LocalDate defaultIn, LocalDate defaultOut) {
        super(owner, "Book Room " + room.getRoomId() + " - " + room.getType().getDisplayName(), ModalityType.APPLICATION_MODAL);
        this.service = service;
        this.selectedRoom = room;

        setSize(520, 620);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.BG_APP);

        // Header with Room Info
        JPanel header = new JPanel(new GridLayout(2, 1, 4, 4));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER),
                new EmptyBorder(16, 22, 16, 22)
        ));
        JLabel title = new JLabel("Room " + room.getRoomId() + " — " + room.getType().getDisplayName());
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.PRIMARY);

        JLabel sub = new JLabel(String.format("Nightly Rate: $%.2f / night  •  Floor %d  •  %s",
                room.getPricePerNight(), room.getFloor(), room.getAmenities()));
        sub.setFont(UITheme.FONT_SMALL);
        sub.setForeground(UITheme.TEXT_MUTED);

        header.add(title);
        header.add(sub);
        add(header, BorderLayout.NORTH);

        // Center Content: Form + Real-time Estimate Card
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(UITheme.BG_APP);
        centerPanel.setBorder(new EmptyBorder(16, 20, 16, 20));

        // 1. Form Card
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(UITheme.cardBorder());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 6, 6, 6);

        inDateField = UITheme.styledTextField(12);
        inDateField.setText(defaultIn != null ? defaultIn.toString() : LocalDate.now().toString());

        outDateField = UITheme.styledTextField(12);
        outDateField.setText(defaultOut != null ? defaultOut.toString() : LocalDate.now().plusDays(2).toString());

        nameField = UITheme.styledTextField(16);
        phoneField = UITheme.styledTextField(16);
        emailField = UITheme.styledTextField(16);
        notesField = UITheme.styledTextField(16);

        // Auto-fill returning guest by phone
        phoneField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                String p = phoneField.getText().trim();
                if (!p.isEmpty()) {
                    Guest existing = service.findGuestByPhone(p);
                    if (existing != null) {
                        nameField.setText(existing.getFullName());
                        emailField.setText(existing.getEmail());
                    }
                }
            }
        });

        // Add form rows
        addFormRow(form, gbc, 0, "Check-In (YYYY-MM-DD):", inDateField);
        addFormRow(form, gbc, 1, "Check-Out (YYYY-MM-DD):", outDateField);
        addFormRow(form, gbc, 2, "Guest Full Name:", nameField);
        addFormRow(form, gbc, 3, "Phone Number:", phoneField);
        addFormRow(form, gbc, 4, "Email Address:", emailField);
        addFormRow(form, gbc, 5, "Special Requests:", notesField);

        centerPanel.add(form);
        centerPanel.add(Box.createVerticalStrut(12));

        // 2. Real-time Total Estimate Card
        JPanel estimateCard = new JPanel(new BorderLayout(12, 6));
        estimateCard.setBackground(new Color(0xF1, 0xF5, 0xF9));
        estimateCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xBA, 0xE6, 0xFD), 1, true),
                new EmptyBorder(12, 16, 12, 16)
        ));

        JPanel estLeft = new JPanel(new GridLayout(2, 1, 2, 2));
        estLeft.setOpaque(false);

        JLabel estHeader = new JLabel("CALCULATED TOTAL ESTIMATE");
        estHeader.setFont(UITheme.FONT_BADGE);
        estHeader.setForeground(UITheme.ACCENT);

        estimateBreakdownLabel = new JLabel("Calculating...");
        estimateBreakdownLabel.setFont(UITheme.FONT_BODY);
        estimateBreakdownLabel.setForeground(UITheme.TEXT_MAIN);

        estLeft.add(estHeader);
        estLeft.add(estimateBreakdownLabel);

        estimateTotalLabel = new JLabel("$0.00");
        estimateTotalLabel.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 22));
        estimateTotalLabel.setForeground(UITheme.PRIMARY);
        estimateTotalLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        estimateNightsLabel = new JLabel("");
        estimateNightsLabel.setFont(UITheme.FONT_SMALL);
        estimateNightsLabel.setForeground(UITheme.TEXT_MUTED);
        estimateNightsLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel estRight = new JPanel(new GridLayout(2, 1, 2, 2));
        estRight.setOpaque(false);
        estRight.add(estimateTotalLabel);
        estRight.add(estimateNightsLabel);

        estimateCard.add(estLeft, BorderLayout.WEST);
        estimateCard.add(estRight, BorderLayout.EAST);

        centerPanel.add(estimateCard);
        add(centerPanel, BorderLayout.CENTER);

        // Footer Buttons
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 16));
        footer.setBackground(UITheme.BG_APP);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.BORDER));

        JButton cancelBtn = UITheme.secondaryButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());

        JButton confirmBtn = UITheme.primaryButton("Confirm & Pay");
        confirmBtn.addActionListener(e -> handleConfirmBooking());

        footer.add(cancelBtn);
        footer.add(confirmBtn);
        add(footer, BorderLayout.SOUTH);

        // DocumentListeners for live recalculation on every keystroke
        DocumentListener dateListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateSummary(); }
            public void removeUpdate(DocumentEvent e) { updateSummary(); }
            public void changedUpdate(DocumentEvent e) { updateSummary(); }
        };

        inDateField.getDocument().addDocumentListener(dateListener);
        outDateField.getDocument().addDocumentListener(dateListener);

        updateSummary();
    }

    private void addFormRow(JPanel p, GridBagConstraints gbc, int row, String labelText, JComponent comp) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0.38;
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(UITheme.FONT_BODY);
        lbl.setForeground(UITheme.TEXT_MAIN);
        p.add(lbl, gbc);

        gbc.gridx = 1; gbc.weightx = 0.62;
        p.add(comp, gbc);
    }

    private void updateSummary() {
        try {
            LocalDate in = LocalDate.parse(inDateField.getText().trim());
            LocalDate out = LocalDate.parse(outDateField.getText().trim());
            long nights = ChronoUnit.DAYS.between(in, out);

            if (nights > 0) {
                double total = nights * selectedRoom.getPricePerNight();
                estimateBreakdownLabel.setText(String.format("%d night(s) × $%.2f / night", nights, selectedRoom.getPricePerNight()));
                estimateTotalLabel.setText(String.format("$%.2f", total));
                estimateNightsLabel.setText(in + " to " + out);
                estimateTotalLabel.setForeground(UITheme.PRIMARY);
            } else {
                estimateBreakdownLabel.setText("Check-out must be after check-in date");
                estimateTotalLabel.setText("Invalid");
                estimateTotalLabel.setForeground(UITheme.DANGER);
                estimateNightsLabel.setText("");
            }
        } catch (Exception ex) {
            estimateBreakdownLabel.setText("Enter valid dates (YYYY-MM-DD)");
            estimateTotalLabel.setText("—");
            estimateTotalLabel.setForeground(UITheme.TEXT_MUTED);
            estimateNightsLabel.setText("");
        }
    }

    private void handleConfirmBooking() {
        try {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            LocalDate in = LocalDate.parse(inDateField.getText().trim());
            LocalDate out = LocalDate.parse(outDateField.getText().trim());

            if (name.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Guest full name and phone number are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!out.isAfter(in)) {
                JOptionPane.showMessageDialog(this, "Check-out date must be strictly after check-in date.", "Invalid Dates", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!service.isRoomAvailable(selectedRoom, in, out)) {
                JOptionPane.showMessageDialog(this, "Room " + selectedRoom.getRoomId() + " is already booked for these dates.", "Room Unavailable", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Guest guest = service.registerOrUpdateGuest(name, phone, email);
            Reservation res = service.createReservation(guest, selectedRoom, in, out, notesField.getText());

            this.createdReservation = res;
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error creating reservation: " + ex.getMessage(), "Booking Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Reservation getCreatedReservation() {
        return createdReservation;
    }
}