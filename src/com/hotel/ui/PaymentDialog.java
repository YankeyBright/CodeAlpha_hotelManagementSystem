package com.hotel.ui;

import com.hotel.model.*;
import com.hotel.service.HotelService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Minimalist modal dialog simulating payment processing for a booking.
 */
public class PaymentDialog extends JDialog {
    private final HotelService service;
    private final Reservation reservation;
    private Payment completedPayment = null;

    public PaymentDialog(Window owner, HotelService service, Reservation reservation) {
        super(owner, "Process Payment - " + reservation.getReservationId(), ModalityType.APPLICATION_MODAL);
        this.service = service;
        this.reservation = reservation;

        setSize(440, 480);
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
        JLabel title = new JLabel("Payment Simulation");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.PRIMARY);

        JLabel sub = new JLabel("Reservation #" + reservation.getReservationId() + "  •  Guest: " + reservation.getGuest().getFullName());
        sub.setFont(UITheme.FONT_SMALL);
        sub.setForeground(UITheme.TEXT_MUTED);

        header.add(title);
        header.add(sub);
        add(header, BorderLayout.NORTH);

        // Body
        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(Color.WHITE);
        body.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(20, 24, 20, 24),
                UITheme.cardBorder()
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        JLabel totalDueLabel = new JLabel(String.format("$%.2f", reservation.getTotalPrice()));
        totalDueLabel.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 26));
        totalDueLabel.setForeground(UITheme.PRIMARY);

        JComboBox<PaymentMethod> methodCombo = new JComboBox<>(PaymentMethod.values());
        methodCombo.setFont(UITheme.FONT_BODY);
        methodCombo.setBackground(Color.WHITE);

        JTextField cardNumField = UITheme.styledTextField(16);
        cardNumField.setText("•••• •••• •••• 4242");

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel dueTitle = new JLabel("TOTAL AMOUNT DUE");
        dueTitle.setFont(UITheme.FONT_BADGE);
        dueTitle.setForeground(UITheme.TEXT_MUTED);
        body.add(dueTitle, gbc);

        gbc.gridy = 1;
        body.add(totalDueLabel, gbc);

        gbc.gridy = 2; gbc.gridwidth = 1;
        JLabel methodTitle = new JLabel("Payment Method:");
        methodTitle.setFont(UITheme.FONT_BODY_BOLD);
        body.add(methodTitle, gbc);

        gbc.gridx = 1;
        body.add(methodCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        JLabel refTitle = new JLabel("Card / Account Ref:");
        refTitle.setFont(UITheme.FONT_BODY);
        body.add(refTitle, gbc);

        gbc.gridx = 1;
        body.add(cardNumField, gbc);

        add(body, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 16));
        footer.setBackground(UITheme.BG_APP);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.BORDER));

        JButton payBtn = UITheme.primaryButton(String.format("Pay $%.2f Now", reservation.getTotalPrice()));
        payBtn.addActionListener(e -> {
            PaymentMethod method = (PaymentMethod) methodCombo.getSelectedItem();
            Payment p = service.processPayment(reservation.getReservationId(), reservation.getTotalPrice(), method);
            this.completedPayment = p;
            JOptionPane.showMessageDialog(this,
                    "Payment of " + String.format("$%.2f", p.getAmount()) + " via " + p.getMethod().getLabel() + " succeeded!\nTransaction ID: " + p.getTransactionRef(),
                    "Payment Successful", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });

        JButton cancelBtn = UITheme.secondaryButton("Skip / Pay Later");
        cancelBtn.addActionListener(e -> dispose());

        footer.add(cancelBtn);
        footer.add(payBtn);
        add(footer, BorderLayout.SOUTH);
    }

    public Payment getCompletedPayment() {
        return completedPayment;
    }
}