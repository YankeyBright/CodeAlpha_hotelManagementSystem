package com.hotel.ui;

import com.hotel.model.*;
import com.hotel.service.HotelService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Minimalist modal dialog for completing a room reservation.
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
    private final JLabel summaryLabel;

    public BookingDialog(Window owner, HotelService service, Room room, LocalDate defaultIn, LocalDate defaultOut) {
        super(owner, "Book " + room.getRoomId() + " - " + room.getType().getDisplayName(), ModalityType.APPLICATION_MODAL);
        this.service = service;
        this.selectedRoom = room;

        setSize(480, 560);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.BG_APP);

        // Header
        JPanel header = new JPanel(new GridLayout(2, 1, 4, 4));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER),
                new EmptyBorder(16, 20, 16, 20)
        ));
        JLabel title = new JLabel("Room " + room.getRoomId() + " (" + room.getType().getDisplayName() + ")");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.PRIMARY);

        JLabel sub = new JLabel(String.format("Base Rate: $%.2f / night  |  %s", room.getPricePerNight(), room.getAmenities()));
        sub.setFont(UITheme.FONT_SMALL);
        sub.setForeground(UITheme.TEXT_MUTED);

        header.add(title);
        header.add(sub);
        add(header, BorderLayout.NORTH);

        // Form content
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(16, 20, 16, 20),
                UITheme.cardBorder()
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 6, 6, 6);

        inDateField = UITheme.styledTextField(12);
        inDateField.setText(defaultIn != null ? defaultIn.toString() : LocalDate.now().toString());

        outDateField = UITheme.styledTextField(12);
        outDateField.setText(defaultOut != null ? defaultOut.toString() : LocalDate.now().plusDays(1).toString());

        nameField = UITheme.styledTextField(16);
        phoneField = UITheme.styledTextField(16);
        emailField = UITheme.styledTextField(16);
        notesField = UITheme.styledTextField(16);

        summaryLabel = new JLabel(" ");
        summaryLabel.setFont(UITheme.FONT_BODY_BOLD);
        summaryLabel.setForeground(UITheme.ACCENT);

        // Auto-check returning guest on phone change
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
        addFormRow(form, gbc, 0, "Check-In Date (YYYY-MM-DD):", inDateField);
        addFormRow(form, gbc, 1, "Check-Out Date (YYYY-MM-DD):", outDateField);
        addFormRow(form, gbc, 2, "Guest Full Name:", nameField);
        addFormRow(form, gbc, 3, "Phone Number:", phoneField);
        addFormRow(form, gbc, 4, "Email Address:", emailField);
        addFormRow(form, gbc, 5, "Special Requests:", notesField);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        form.add(summaryLabel, gbc);

        add(form, BorderLayout.CENTER);

        // Footer buttons
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

        updateSummary();
    }

    private void addFormRow(JPanel p, GridBagConstraints gbc, int row, String labelText, JComponent comp) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0.35;
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(UITheme.FONT_BODY);
        lbl.setForeground(UITheme.TEXT_MAIN);
        p.add(lbl, gbc);

        gbc.gridx = 1; gbc.weightx = 0.65;
        p.add(comp, gbc);
    }

    private void updateSummary() {
        try {
            LocalDate in = LocalDate.parse(inDateField.getText().trim());
            LocalDate out = LocalDate.parse(outDateField.getText().trim());
            long nights = ChronoUnit.DAYS.between(in, out);
            if (nights > 0) {
                double total = nights * selectedRoom.getPricePerNight();
                summaryLabel.setText(String.format("Stay: %d night(s)  •  Estimated Total: $%.2f", nights, total));
            } else {
                summaryLabel.setText("Check-out must be after check-in");
            }
        } catch (Exception ignored) {
            summaryLabel.setText(" ");
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
                JOptionPane.showMessageDialog(this, "Guest name and phone are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!out.isAfter(in)) {
                JOptionPane.showMessageDialog(this, "Check-out date must be after check-in date.", "Invalid Dates", JOptionPane.WARNING_MESSAGE);
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