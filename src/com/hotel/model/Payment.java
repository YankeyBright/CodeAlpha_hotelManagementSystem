package com.hotel.model;

import java.time.LocalDateTime;

/**
 * Tracks payment transactions associated with a reservation.
 */
public class Payment {
    private String paymentId;
    private String reservationId;
    private double amount;
    private PaymentMethod method;
    private LocalDateTime timestamp;
    private String transactionRef;
    private boolean successful;

    public Payment(String paymentId, String reservationId, double amount, PaymentMethod method, String transactionRef) {
        this.paymentId = paymentId;
        this.reservationId = reservationId;
        this.amount = amount;
        this.method = method;
        this.timestamp = LocalDateTime.now();
        this.transactionRef = transactionRef;
        this.successful = true;
    }

    public Payment(String paymentId, String reservationId, double amount, PaymentMethod method,
                   LocalDateTime timestamp, String transactionRef, boolean successful) {
        this.paymentId = paymentId;
        this.reservationId = reservationId;
        this.amount = amount;
        this.method = method;
        this.timestamp = timestamp;
        this.transactionRef = transactionRef;
        this.successful = successful;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public double getAmount() {
        return amount;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String toCsv() {
        return paymentId + "," + reservationId + "," + amount + "," + method.name() + "," +
                timestamp + "," + transactionRef + "," + successful;
    }
}
