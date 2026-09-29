# Lumina Hotel Reservation System

[![Java](https://img.shields.io/badge/Java-8%2B-ED8B00?style=flat&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Platform](https://img.shields.io/badge/Platform-Windows%20%7C%20macOS%20%7C%20Linux-blue?style=flat)]()
[![Internship](https://img.shields.io/badge/CodeAlpha-Java%20Internship%20Task%204-green?style=flat)]()
[![Dependencies](https://img.shields.io/badge/Dependencies-Zero%20(Pure%20Java%20SE)-success?style=flat)]()

A modern, minimalist desktop application for hotel room reservations, guest management, payment simulation, and printable invoice generation. Built with a clean 4-layer Object-Oriented Programming (OOP) architecture and pure Java Swing.

Developed as part of the **CodeAlpha Java Programming Internship** (Task 4).

---

## 📸 Key Features

- **Minimalist Aesthetic**: Clean Slate & Neutral UI (`#0F172A`, `#F8FAFC`) with crisp typography, flat cards, and subtle 1px dividers.
- **Room Search & Categorization**: Filter by room category (**Standard**, **Deluxe**, **Executive Suite**, **Family Suite**) and specific check-in / check-out dates.
- **Dynamic Date Overlap Engine**: Evaluates true availability across requested calendar dates rather than simple binary flags.
- **Returning Guest Detection**: Automatically fills guest name and email when an existing phone number is entered.
- **Reservation Management**: View all active and past bookings, search by guest name or reservation ID, and cancel bookings with instant room release.
- **Payment Simulation**: Process payments using Credit Card, Debit Card, Cash, or Mobile Money with automatic transaction reference generation.
- **Printable Invoices**: Generates a clean, branded HTML invoice complete with itemized charges, taxes, and barcode simulation that opens directly in your browser with one click.
- **Zero-Setup File Persistence**: Uses lightweight CSV files (`data/rooms.csv`, `data/reservations.csv`, `data/guests.csv`, `data/payments.csv`). Runs anywhere without installing or configuring a MySQL server.

---

## 🏗️ 4-Layer Architecture

```
src/com/hotel/
├── model/                  # Domain entities & business rules
│   ├── Room.java           # Room details, pricing, floor, amenities
│   ├── RoomType.java       # Category enum (Standard, Deluxe, Suite, Family)
│   ├── Guest.java          # Guest contact and identification
│   ├── Reservation.java    # Booking with date overlap calculation
│   ├── ReservationStatus.java  # Confirmed / Cancelled lifecycle
│   ├── Payment.java        # Transaction record
│   └── PaymentMethod.java  # Payment options enum
│
├── storage/                # File persistence layer
│   └── FileStorage.java    # CSV reading, writing, and seed data initialization
│
├── service/                # Core business logic
│   └── HotelService.java   # Availability check, booking, cancellations, payments
│
├── ui/                     # Presentation layer (Pure Java Swing)
│   ├── UITheme.java        # Minimalist design system (colors, fonts, borders)
│   ├── HeaderPanel.java    # Top brand header bar
│   ├── RoomTableModel.java # Table model for room inventory
│   ├── ReservationTableModel.java # Table model for reservations
│   ├── SearchBookPanel.java # Room discovery & booking tab
│   ├── BookingDialog.java  # Interactive guest checkout modal
│   ├── PaymentDialog.java  # Payment simulation modal
│   ├── ManageReservationsPanel.java # Reservation management tab
│   ├── InvoiceGenerator.java # Styled HTML invoice generator & browser preview
│   └── MainFrame.java      # Main application window
│
└── Main.java               # Application entry point
```

---

## 🚀 How to Run

### Option 1: 1-Click Run (Windows)
Double-click **`run.bat`** in the project root folder.

### Option 2: Command Line (Any OS)

1. **Compile all source files:**
   ```bash
   javac -d out src/com/hotel/model/*.java src/com/hotel/storage/*.java src/com/hotel/service/*.java src/com/hotel/ui/*.java src/com/hotel/Main.java
   ```

2. **Launch the application:**
   ```bash
   java -cp out com.hotel.Main
   ```

---

## 🔒 Business Invariants & OOP Principles

| Principle | Implementation |
|---|---|
| **Encapsulation** | Strict private fields with immutable IDs and validated mutators |
| **Date Range Calculation** | `overlapsWith(start, end)`: mathematical interval intersection `(start < res.end) && (end > res.start)` |
| **Separation of Concerns** | UI never touches file I/O directly; all data routes through `HotelService` |
| **Fault Tolerance** | Missing data files are automatically initialized with default seed rooms |
| **Portability** | Pure Java SE standard library — zero Maven/Gradle/external JAR dependencies |

---

## 📄 License & Credits
Built for the **CodeAlpha Java Programming Internship** by **Bright Yankey**.