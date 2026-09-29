package com.hotel.model;

/**
 * Lifecycle status of a reservation.
 */
public enum ReservationStatus {
    CONFIRMED("Confirmed"),
    CANCELLED("Cancelled");

    private final String label;

    ReservationStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
