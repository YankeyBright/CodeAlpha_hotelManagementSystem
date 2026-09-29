# Hotel Reservation System

A **console-based Java application** to search, book, and manage hotel rooms with room categorization, payment simulation, and file I/O persistence.

> **CodeAlpha Java Programming Internship — Task 4**

---

## Features

- **Search Available Rooms** — Filter by room type (Standard, Deluxe, Suite) or view all
- **Book a Room** — Select room, enter guest details, confirm booking with date validation
- **Cancel a Reservation** — Cancel by reservation ID with confirmation prompt
- **View Booking Details** — Look up any reservation and its payment receipt
- **View All Rooms** — See all rooms with real-time availability status
- **View All Reservations** — Summary of all bookings with status
- **Payment Simulation** — Simulated payment processing with Credit Card, Debit Card, or Cash
- **Data Persistence** — All data saved to CSV files (rooms, guests, reservations, payments)
- **Returning Guest Detection** — Automatically recognizes returning guests by phone number

---

## Room Categories

| Type     | Price Range     | Rooms Available |
|----------|-----------------|-----------------|
| Standard | $80 - $85/night | 3 rooms         |
| Deluxe   | $150 - $160/night | 3 rooms       |
| Suite    | $280 - $350/night | 4 rooms       |

---

## Project Structure

```
HotelReservationSystem/
├── src/
│   ├── model/                  # Domain model classes
│   │   ├── Room.java           # Hotel room with type and pricing
│   │   ├── RoomType.java       # Enum: STANDARD, DELUXE, SUITE
│   │   ├── Guest.java          # Guest with contact information
│   │   ├── Reservation.java    # Reservation linking guest to room
│   │   ├── ReservationStatus.java  # Enum: CONFIRMED, CANCELLED, COMPLETED
│   │   ├── Payment.java        # Payment with receipt generation
│   │   └── PaymentMethod.java  # Enum: CREDIT_CARD, DEBIT_CARD, CASH
│   ├── service/                # Business logic layer
│   │   └── HotelManager.java   # Core operations (search, book, cancel)
│   ├── util/                   # Utility classes
│   │   ├── FileManager.java    # CSV file I/O for all entities
│   │   ├── IdGenerator.java    # Unique ID generation
│   │   └── InputValidator.java # Input validation helpers
│   └── HotelApp.java           # Main application entry point
├── data/                       # Auto-generated data files
│   ├── rooms.csv
│   ├── guests.csv
│   ├── reservations.csv
│   └── payments.csv
└── README.md
```

---

## How to Compile and Run

### Prerequisites
- **Java JDK 8** or higher installed
- Terminal or Command Prompt

### Step 1: Navigate to the project directory 
```bash
cd HotelReservationSystem
```

### Step 2: Compile all Java files
```bash
javac -d out src/model/*.java src/util/*.java src/service/*.java src/HotelApp.java
```

### Step 3: Run the application
```bash
java -cp out HotelApp
```

> **Note:** The `data/` directory with default rooms will be created automatically on first run.

---

## OOP Concepts Used

| Concept | Where Used |
|---------|------------|
| **Encapsulation** | All fields are `private` with getters; state changes through methods only |
| **Enums** | `RoomType`, `ReservationStatus`, `PaymentMethod` — type-safe constants |
| **Constructors** | Overloaded constructors for creation vs. file loading |
| **Composition** | `Reservation` contains `Guest` and `Room`; `Payment` contains `Reservation` |
| **Collections** | `ArrayList` used to manage rooms, guests, reservations, and payments |
| **Validation** | Invariant checks in constructors and business methods |
| **Separation of Concerns** | `model/` (data), `service/` (logic), `util/` (helpers), `HotelApp` (UI) |

---

## Data Persistence

All data is stored in CSV files under the `data/` directory:

- **rooms.csv** — Room ID, type, price, availability
- **guests.csv** — Guest ID, name, phone, email
- **reservations.csv** — Reservation ID, guest ID, room ID, dates, status, timestamp
- **payments.csv** — Payment ID, reservation ID, amount, method, paid status

Data is loaded on startup and saved automatically after every booking, cancellation, or payment.

---

## Sample Usage

```
+==============================================+
|       HOTEL RESERVATION SYSTEM               |
+==============================================+
|  1. Search Available Rooms                   |
|  2. Book a Room                              |
|  3. Cancel a Reservation                     |
|  4. View Booking Details                     |
|  5. View All Rooms                           |
|  6. View All Reservations                    |
|  7. Exit                                     |
+==============================================+
  Enter your choice: 2

  --- Book a Room ---
  Select room type:
  0. All Types
  1. Standard  ($80 - $85/night)
  2. Deluxe    ($150 - $160/night)
  3. Suite     ($280 - $350/night)
  Choice: 2

  Available Rooms:
  1. Room R004  | Deluxe     | $ 150.00/night | AVAILABLE
  2. Room R005  | Deluxe     | $ 150.00/night | AVAILABLE
  3. Room R006  | Deluxe     | $ 160.00/night | AVAILABLE
  Select room number (1-3): 1
  Enter check-in date (yyyy-MM-dd): 2026-10-01
  Enter check-out date (yyyy-MM-dd): 2026-10-05
  Enter guest phone number: 0551234567
  Enter guest name: John Doe
  Enter guest email (optional, press Enter to skip): john@email.com

  --- Booking Summary ---
  Room      : R004 (Deluxe)
  Guest     : John Doe
  Check-in  : 2026-10-01
  Check-out : 2026-10-05
  Nights    : 4
  Total     : $600.00
  Confirm booking? (yes/no): yes

  Booking confirmed!
```

---

## Author

Built as part of the **CodeAlpha Java Programming Internship** program.
