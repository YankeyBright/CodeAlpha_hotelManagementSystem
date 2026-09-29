package com.hotel.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Represents a confirmed or cancelled hotel booking.
 * Calculates duration of stay and total pricing dynamically.
 */
public class Reservation {
    private String reservationId;
    private Guest guest;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private ReservationStatus status;
    private double totalPrice;
    private LocalDateTime bookingTime;
    private String specialNotes;

    public Reservation(String reservationId, Guest guest, Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        if (!checkOutDate.isAfter(checkInDate)) {
            throw new IllegalArgumentException("Check-out date must be strictly after check-in date.");
        }
        this.reservationId = reservationId;
        this.guest = guest;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = ReservationStatus.CONFIRMED;
        this.totalPrice = calculateTotalPrice();
        this.bookingTime = LocalDateTime.now();
        this.specialNotes = "";
    }

    public Reservation(String reservationId, Guest guest, Room room, LocalDate checkInDate, LocalDate checkOutDate,
                       ReservationStatus status, double totalPrice, LocalDateTime bookingTime, String specialNotes) {
        this.reservationId = reservationId;
        this.guest = guest;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = status;
        this.totalPrice = totalPrice;
        this.bookingTime = bookingTime;
        this.specialNotes = specialNotes != null ? specialNotes : "";
    }

    public long getNumberOfNights() {
        return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }

    public double calculateTotalPrice() {
        long nights = getNumberOfNights();
        return (nights <= 0 ? 1 : nights) * room.getPricePerNight();
    }

    public boolean overlapsWith(LocalDate start, LocalDate end) {
        if (status == ReservationStatus.CANCELLED) {
            return false;
        }
        // Date overlap algorithm: (start < this.checkOutDate) && (end > this.checkInDate)
        return start.isBefore(this.checkOutDate) && end.isAfter(this.checkInDate);
    }

    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
    }

    public String getReservationId() {
        return reservationId;
    }

    public Guest getGuest() {
        return guest;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public String getSpecialNotes() {
        return specialNotes;
    }

    public void setSpecialNotes(String specialNotes) {
        this.specialNotes = specialNotes;
    }

    /**
     * CSV serialization format.
     */
    public String toCsv() {
        return reservationId + "," + guest.getGuestId() + "," + room.getRoomId() + "," +
                checkInDate + "," + checkOutDate + "," + status.name() + "," +
                totalPrice + "," + bookingTime + ",\"" + specialNotes + "\"";
    }

    @Override
    public String toString() {
        return "Booking #" + reservationId + " - Room " + room.getRoomId() + " (" + guest.getFullName() + ") [" + status + "]";
    }
}
