package com.hotel.storage;

import com.hotel.model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Handles persistent storage using CSV files for portability and zero external database dependencies.
 */
public class FileStorage {
    private static final String DATA_DIR = "data";
    private static final String ROOMS_FILE = DATA_DIR + File.separator + "rooms.csv";
    private static final String GUESTS_FILE = DATA_DIR + File.separator + "guests.csv";
    private static final String RESERVATIONS_FILE = DATA_DIR + File.separator + "reservations.csv";
    private static final String PAYMENTS_FILE = DATA_DIR + File.separator + "payments.csv";

    public FileStorage() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        initDefaultRoomsIfEmpty();
    }

    // ==================== ROOMS ====================

    public List<Room> loadRooms() {
        List<Room> list = new ArrayList<>();
        File file = new File(ROOMS_FILE);
        if (!file.exists()) {
            initDefaultRoomsIfEmpty();
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (parts.length >= 3) {
                    try {
                        String roomId = parts[0].trim();
                        RoomType type = RoomType.valueOf(parts[1].trim());
                        double price = Double.parseDouble(parts[2].trim());
                        int floor = (parts.length >= 4 && parts[3].trim().matches("\\d+")) ? Integer.parseInt(parts[3].trim()) : 1;
                        String amenities = parts.length >= 5 ? parts[4].replace("\"", "").trim() : type.getDescription();
                        list.add(new Room(roomId, type, price, floor, amenities));
                    } catch (Exception ex) {
                        System.err.println("Skipping malformed room row: " + line);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Warning loading rooms: " + e.getMessage());
        }

        if (list.isEmpty()) {
            initDefaultRoomsIfEmpty();
            return loadRooms();
        }

        return list;
    }

    public void saveRooms(List<Room> rooms) {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(ROOMS_FILE), StandardCharsets.UTF_8))) {
            for (Room r : rooms) {
                writer.println(r.toCsv());
            }
        } catch (IOException e) {
            System.err.println("Error saving rooms: " + e.getMessage());
        }
    }

    // ==================== GUESTS ====================

    public List<Guest> loadGuests() {
        List<Guest> list = new ArrayList<>();
        File file = new File(GUESTS_FILE);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (parts.length >= 3) {
                    String guestId = parts[0].trim();
                    String name = parts[1].replace("\"", "").trim();
                    String phone = parts[2].trim();
                    String email = parts.length > 3 ? parts[3].trim() : "";
                    list.add(new Guest(guestId, name, phone, email));
                }
            }
        } catch (Exception e) {
            System.err.println("Warning loading guests: " + e.getMessage());
        }
        return list;
    }

    public void saveGuests(List<Guest> guests) {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(GUESTS_FILE), StandardCharsets.UTF_8))) {
            for (Guest g : guests) {
                writer.println(g.toCsv());
            }
        } catch (IOException e) {
            System.err.println("Error saving guests: " + e.getMessage());
        }
    }

    // ==================== RESERVATIONS ====================

    public List<Reservation> loadReservations(Map<String, Guest> guestMap, Map<String, Room> roomMap) {
        List<Reservation> list = new ArrayList<>();
        File file = new File(RESERVATIONS_FILE);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (parts.length >= 8) {
                    try {
                        String resId = parts[0].trim();
                        String guestId = parts[1].trim();
                        String roomId = parts[2].trim();
                        LocalDate checkIn = LocalDate.parse(parts[3].trim());
                        LocalDate checkOut = LocalDate.parse(parts[4].trim());
                        ReservationStatus status = ReservationStatus.valueOf(parts[5].trim());
                        double totalPrice = Double.parseDouble(parts[6].trim());
                        LocalDateTime bookingTime = LocalDateTime.parse(parts[7].trim());
                        String notes = parts.length > 8 ? parts[8].replace("\"", "").trim() : "";

                        Guest guest = guestMap.get(guestId);
                        Room room = roomMap.get(roomId);

                        if (guest != null && room != null) {
                            list.add(new Reservation(resId, guest, room, checkIn, checkOut, status, totalPrice, bookingTime, notes));
                        }
                    } catch (Exception ex) {
                        System.err.println("Skipping malformed reservation: " + line);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Warning loading reservations: " + e.getMessage());
        }
        return list;
    }

    public void saveReservations(List<Reservation> reservations) {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(RESERVATIONS_FILE), StandardCharsets.UTF_8))) {
            for (Reservation res : reservations) {
                writer.println(res.toCsv());
            }
        } catch (IOException e) {
            System.err.println("Error saving reservations: " + e.getMessage());
        }
    }

    // ==================== PAYMENTS ====================

    public List<Payment> loadPayments() {
        List<Payment> list = new ArrayList<>();
        File file = new File(PAYMENTS_FILE);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    try {
                        String payId = parts[0].trim();
                        String resId = parts[1].trim();
                        double amount = Double.parseDouble(parts[2].trim());
                        PaymentMethod method = PaymentMethod.valueOf(parts[3].trim());
                        LocalDateTime timestamp = LocalDateTime.parse(parts[4].trim());
                        String ref = parts[5].trim();
                        boolean success = Boolean.parseBoolean(parts[6].trim());
                        list.add(new Payment(payId, resId, amount, method, timestamp, ref, success));
                    } catch (Exception ex) {
                        System.err.println("Skipping malformed payment: " + line);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Warning loading payments: " + e.getMessage());
        }
        return list;
    }

    public void savePayments(List<Payment> payments) {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(PAYMENTS_FILE), StandardCharsets.UTF_8))) {
            for (Payment p : payments) {
                writer.println(p.toCsv());
            }
        } catch (IOException e) {
            System.err.println("Error saving payments: " + e.getMessage());
        }
    }

    // ==================== DEFAULT SEED ROOMS ====================

    private void initDefaultRoomsIfEmpty() {
        File file = new File(ROOMS_FILE);
        if (!file.exists() || file.length() == 0) {
            List<Room> defaults = Arrays.asList(
                    new Room("101", RoomType.STANDARD, 850.00, 1, "Queen Bed, Ensuite Bath, High-Speed Wi-Fi, 43-inch Smart TV"),
                    new Room("102", RoomType.STANDARD, 850.00, 1, "Queen Bed, Ensuite Bath, High-Speed Wi-Fi, Coffee Maker"),
                    new Room("103", RoomType.STANDARD, 900.00, 1, "Two Twin Beds, Garden View, High-Speed Wi-Fi, Ensuite Bath"),
                    new Room("201", RoomType.DELUXE, 1450.00, 2, "King Bed, Skyline View, Mini-Bar, Work Desk, Nespresso"),
                    new Room("202", RoomType.DELUXE, 1450.00, 2, "King Bed, City View, Marble Bath, Mini-Bar, Smart TV"),
                    new Room("203", RoomType.DELUXE, 1550.00, 2, "King Bed, Private Balcony, Espresso Machine, City View"),
                    new Room("301", RoomType.SUITE, 2600.00, 3, "Master Bedroom, Living Area, Deep Soaking Tub, Panoramic View"),
                    new Room("302", RoomType.SUITE, 2800.00, 3, "Penthouse Suite, Private Terrace, Butler Service, Kitchenette"),
                    new Room("401", RoomType.FAMILY, 3200.00, 4, "Two Interconnected Bedrooms, Kitchenette, Dining Area, 2 Baths"),
                    new Room("402", RoomType.FAMILY, 3400.00, 4, "Two King Bedrooms, Large Balcony, Kitchenette, Lounge Space")
            );
            saveRooms(defaults);
        }
    }
}