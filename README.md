<div align="center">

# LUMINA HOTEL & RESIDENCES
### Enterprise Hotel Reservation & Guest Services Management System

[![Java](https://img.shields.io/badge/Java-8%2B%20%7C%2011%20%7C%2017%20%7C%2021-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Platform](https://img.shields.io/badge/Platform-Windows%20%7C%20macOS%20%7C%20Linux-0284C7?style=for-the-badge)]()
[![Internship](https://img.shields.io/badge/CodeAlpha-Task%204%20Completed-059669?style=for-the-badge)]()
[![Architecture](https://img.shields.io/badge/Architecture-4--Layer%20OOP-0F172A?style=for-the-badge)]()
[![Currency](https://img.shields.io/badge/Currency-Ghana%20Cedis%20(GH₵)-D4AF37?style=for-the-badge)]()

<br/>

<img src="assets/logo.png" alt="Lumina Hotel Logo" width="160" style="border-radius: 20px; box-shadow: 0 10px 25px rgba(0,0,0,0.2);" />

<br/>
<p align="center">
  <b>A luxury, boutique hotel desktop management suite engineered in pure Java SE with modern Swing aesthetics, zero-dependency persistence, and printable digital invoicing.</b>
</p>

</div>

---

## 🏛️ Executive Overview

**Lumina Hotel & Residences** is an enterprise-grade desktop management application designed for boutique and luxury hospitality operators. Built following rigorous 4-layer Object-Oriented Design principles, it simplifies room discovery, availability resolution, guest checkout, payment settlement, and customer invoicing with zero external framework overhead.

All financial calculations and billing summaries are natively localized in **Ghana Cedis (GH₵)**.

---

## 🌟 Key Capabilities

### 1. Minimalist Obsidian & Gold Interface
- Engineered using a tailored luxury design system (`#0A0E1A`, `#F8FAFC`, `#C59B27`).
- Anti-aliased custom button renderers eliminate OS theme discrepancies and ensure maximum legibility.
- Responsive, uncluttered navigation tabs for front-desk staff: **"Find & Book Rooms"** and **"Manage Reservations"**.

### 2. Algorithmic Date-Range Conflict Engine
- Unlike standard demo projects that rely on static boolean flags, Lumina Hotel employs a mathematical interval intersection algorithm:
  $$\text{Conflict} \iff (\text{RequestedCheckIn} < \text{ExistingCheckOut}) \land (\text{RequestedCheckOut} > \text{ExistingCheckIn})$$
- Ensures rooms can be reserved across multiple distinct, non-overlapping calendar windows without false unavailability.

### 3. Dynamic Real-Time Price Estimator
- Instant duration calculation and price estimation in **Ghana Cedis (GH₵)** right inside the inventory table and checkout modal.
- Active keystroke listeners update the stay duration, rate breakdown, and total cost on the fly as dates are adjusted.

### 4. Official Printable Invoices & Billing
- One-click generation of branded, luxury HTML invoices formatted for **Accra, Ghana** hospitality compliance (incorporating Room Subtotal, 15% VAT / GETFund / NHIL, and digital verification codes).
- Automatic browser preview with print-to-PDF support via `InvoiceGenerator`.

### 5. Returning Guest Detection
- Automatically identifies returning guests by phone number, auto-populating contact details and guest history.

### 6. Zero-Setup Portable CSV Persistence
- Out-of-the-box storage engine requiring **no external database installations** or network dependencies.
- Safely maintains `data/rooms.csv`, `data/reservations.csv`, `data/guests.csv`, and `data/payments.csv`.

---

## 📐 System Architecture

```
src/com/hotel/
├── model/                  # Domain Entities & Business Invariants
│   ├── Room.java           # Physical room metadata, pricing, floor, amenities
│   ├── RoomType.java       # Category enum (Standard, Deluxe, Executive Suite, Family Suite)
│   ├── Guest.java          # Guest profile and contact verification
│   ├── Reservation.java    # Booking entity with interval intersection logic
│   ├── ReservationStatus.java  # Lifecycle state (Confirmed, Cancelled)
│   ├── Payment.java        # Settlement transaction record
│   └── PaymentMethod.java  # Payment channel enum (Card, MoMo, Cash)
│
├── storage/                # Persistence & Data Access Layer
│   └── FileStorage.java    # Multi-format CSV engine with auto-healing seed data
│
├── service/                # Business Logic Layer
│   └── HotelService.java   # Availability scheduling, reservations, and settlements
│
├── ui/                     # Presentation Layer (Pure Java Swing)
│   ├── UITheme.java        # Central design tokens, currency formatting (GH₵), custom UI
│   ├── HotelLogo.java      # Image loader with vector fallback
│   ├── HeaderPanel.java    # Luxury branding header & reception status
│   ├── RoomTableModel.java # Dynamic room table model with live estimates
│   ├── ReservationTableModel.java # Searchable reservation table model
│   ├── SearchBookPanel.java# Room discovery & booking workflow panel
│   ├── BookingDialog.java  # Checkout modal with live keystroke estimate calculation
│   ├── PaymentDialog.java  # Payment settlement dialog
│   ├── ManageReservationsPanel.java # Reservation management & cancellation
│   ├── InvoiceGenerator.java # HTML invoice renderer with browser preview
│   └── MainFrame.java      # Main application window
│
└── Main.java               # Application bootstrap
```

---

## 🏨 Room Categories & Standard Rates

| Category | Base Rate (GH₵ / night) | Max Capacity | Features & Inclusions |
|---|---|---|---|
| **Standard Room** | **GH₵ 850.00** | 2 Guests | Queen Bed, Ensuite Bath, High-Speed Wi-Fi, 43" Smart TV |
| **Deluxe Room** | **GH₵ 1,450.00** | 3 Guests | King Bed, City Skyline View, Mini-Bar, Work Desk, Nespresso |
| **Executive Suite** | **GH₵ 2,600.00** | 4 Guests | Master Bedroom, Living Lounge, Jacuzzi Tub, Panoramic Terrace |
| **Family Suite** | **GH₵ 3,200.00** | 5 Guests | Two Interconnected Bedrooms, Kitchenette, Dining Area, 2 Baths |

---

## 🚀 Quick Start Guide

### Prerequisites
- **Java Development Kit (JDK) 8 or higher**
- Any operating system (Windows, macOS, Linux)

### 1-Click Launch (Windows)
Double-click **`run.bat`** in the root directory.

### Command Line Launch (All Platforms)

1. **Compile the source tree:**
   ```bash
   javac -d out src/com/hotel/model/*.java src/com/hotel/storage/*.java src/com/hotel/service/*.java src/com/hotel/ui/*.java src/com/hotel/Main.java
   ```

2. **Run the application:**
   ```bash
   java -cp out com.hotel.Main
   ```

---

## 🔒 Engineering Standards & Best Practices

| Standard | Implementation |
|---|---|
| **Object Encapsulation** | All entity fields are private; state mutations are governed by verified methods |
| **Localization** | Centralized `UITheme.formatCurrency()` ensuring uniform Ghana Cedi formatting |
| **Thread Safety** | Synchronization on shared reservation scheduling and transaction settlement methods |
| **Fault-Tolerant Persistence** | Auto-initializes default room catalog if CSV data files are missing or modified |
| **Zero Dependencies** | Runs natively on standard Java SE with no Maven, Gradle, or third-party JARs |

---

## 👨‍💻 Author & Credits

Developed by **Bright Yankey** for the **CodeAlpha Java Programming Internship** (Task 4: Hotel Reservation System).