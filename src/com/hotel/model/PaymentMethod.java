package com.hotel.model;

/**
 * Payment methods accepted by the hotel reservation system.
 */
public enum PaymentMethod {
    CREDIT_CARD("Credit Card"),
    DEBIT_CARD("Debit Card"),
    CASH("Cash"),
    MOBILE_PAYMENT("Mobile Money / Online Transfer");

    private final String label;

    PaymentMethod(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
