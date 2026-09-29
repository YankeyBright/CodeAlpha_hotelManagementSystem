package com.hotel.service;

import com.hotel.model.*;
import com.hotel.storage.FileStorage;

import java.time.LocalDate;
import java.util.*;

/**
 * Core business service orchestrating room inventory, dynamic availability checks,
 * booking workflows, guest profiles, and payment simulations.
 */
public class HotelService {
    private final FileStorage storage;
    private final List<Room> rooms = new ArrayList<>();
    private final List<Guest> guests = new ArrayList<>();
    private final List<Reservation> reservations = new ArrayList<>();
    private final List<Payment> payments = new ArrayList<>();

    private int nextGuestId = 1001;
    private int nextReservationId = 2001;
    private int nextPaymentId = 3001;

    public HotelService() {
        this.storage = new FileStorage();
        loadAllData();
    }

    private void loadAllData() {
        rooms.clear();
        rooms.addAll(storage.loadRooms());

        guests.clear();
        guests.addAll(storage.loadGuests());

        Map<String, Guest> guestMap = new HashMap<>();
        for (Guest g : guests) {
            guestMap.put(g.getGuestId(), g);
            try {
                int idNum = Integer.parseInt(g.getGuestId().replaceAll("\\D", ""));
                if (idNum >= nextGuestId) nextGuestId = idNum + 1;
            } catch (Exception ignored) {}
        }

        Map<String, Room> roomMap = new HashMap<>();
        for (Room r : rooms) {
            roomMap.put(r.getRoomId(), r);
        }

        reservations.clear();
        reservations.addAll(storage.loadReservations(guestMap, roomMap));
        for (Reservation res : reservations) {
            try {
                int idNum = Integer.parseInt(res.getReservationId().replaceAll("\\D", ""));
                if (idNum >= nextReservationId) nextReservationId = idNum + 1;
            } catch (Exception ignored) {}
        }

        payments.clear();
        payments.addAll(storage.loadPayments());
        for (Payment p : payments) {
            try {
                int idNum = Integer.parseInt(p.getPaymentId().replaceAll("\\D", ""));
                if (idNum >= nextPaymentId) nextPaymentId = idNum + 1;
            } catch (Exception ignored) {}
        }
    }

    public synchronized void persist() {
        storage.saveRooms(rooms);
        storage.saveGuests(guests);
        storage.saveReservations(reservations);
        storage.savePayments(payments);
    }

    // ==================== ROOM & AVAILABILITY SEARCH ====================

    public List<Room> getAllRooms() {
        return Collections.unmodifiableList(rooms);
    }

    public Room findRoomById(String roomId) {
        for (Room r : rooms) {
            if (r.getRoomId().equalsIgnoreCase(roomId)) {
                return r;
            }
        }
        return null;
    }

    /**
     * Checks if a specific room is available for the given date window.
     */
    public boolean isRoomAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {
        for (Reservation res : reservations) {
            if (res.getStatus() == ReservationStatus.CONFIRMED &&
                res.getRoom().getRoomId().equalsIgnoreCase(room.getRoomId())) {
                if (res.overlapsWith(checkIn, checkOut)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Finds available rooms filtered by room category and date range.
     */
    public List<Room> searchAvailableRooms(RoomType categoryFilter, LocalDate checkIn, LocalDate checkOut) {
        List<Room> available = new ArrayList<>();
        for (Room r : rooms) {
            if (categoryFilter != null && r.getType() != categoryFilter) {
                continue;
            }
            if (isRoomAvailable(r, checkIn, checkOut)) {
                available.add(r);
            }
        }
        return available;
    }

    // ==================== GUEST MANAGEMENT ====================

    public List<Guest> getAllGuests() {
        return Collections.unmodifiableList(guests);
    }

    public Guest findGuestByPhone(String phone) {
        if (phone == null) return null;
        String cleanPhone = phone.replaceAll("[^0-9]", "");
        for (Guest g : guests) {
            if (g.getPhone().replaceAll("[^0-9]", "").equals(cleanPhone)) {
                return g;
            }
        }
        return null;
    }

    public Guest registerOrUpdateGuest(String fullName, String phone, String email) {
        Guest existing = findGuestByPhone(phone);
        if (existing != null) {
            existing.setFullName(fullName);
            existing.setEmail(email);
            persist();
            return existing;
        }

        String guestId = "G" + (nextGuestId++);
        Guest newGuest = new Guest(guestId, fullName, phone, email);
        guests.add(newGuest);
        persist();
        return newGuest;
    }

    // ==================== RESERVATIONS ====================

    public List<Reservation> getAllReservations() {
        return Collections.unmodifiableList(reservations);
    }

    public Reservation findReservationById(String reservationId) {
        for (Reservation res : reservations) {
            if (res.getReservationId().equalsIgnoreCase(reservationId)) {
                return res;
            }
        }
        return null;
    }

    public synchronized Reservation createReservation(Guest guest, Room room, LocalDate checkIn, LocalDate checkOut, String notes) {
        if (!isRoomAvailable(room, checkIn, checkOut)) {
            throw new IllegalStateException("Room " + room.getRoomId() + " is already booked for the selected dates.");
        }

        String resId = "RES-" + (nextReservationId++);
        Reservation reservation = new Reservation(resId, guest, room, checkIn, checkOut);
        if (notes != null && !notes.trim().isEmpty()) {
            reservation.setSpecialNotes(notes.trim());
        }
        reservations.add(reservation);
        persist();
        return reservation;
    }

    public synchronized boolean cancelReservation(String reservationId) {
        Reservation res = findReservationById(reservationId);
        if (res == null || res.getStatus() == ReservationStatus.CANCELLED) {
            return false;
        }
        res.cancel();
        persist();
        return true;
    }

    // ==================== PAYMENTS ====================

    public List<Payment> getPaymentsForReservation(String reservationId) {
        List<Payment> list = new ArrayList<>();
        for (Payment p : payments) {
            if (p.getReservationId().equalsIgnoreCase(reservationId)) {
                list.add(p);
            }
        }
        return list;
    }

    public synchronized Payment processPayment(String reservationId, double amount, PaymentMethod method) {
        Reservation res = findReservationById(reservationId);
        if (res == null) {
            throw new IllegalArgumentException("Reservation not found: " + reservationId);
        }

        String payId = "PAY-" + (nextPaymentId++);
        String txRef = "TXN-" + System.currentTimeMillis();
        Payment payment = new Payment(payId, reservationId, amount, method, txRef);
        payments.add(payment);
        persist();
        return payment;
    }
}