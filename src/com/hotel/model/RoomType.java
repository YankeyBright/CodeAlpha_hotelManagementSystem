package com.hotel.model;

/**
 * Enum defining the room categories available in the hotel,
 * including base rate in Ghana Cedis (GH₵), max occupancy, and description.
 */
public enum RoomType {
    STANDARD("Standard Room", 850.00, 2, "Cozy room with queen bed, ensuite bath, and high-speed Wi-Fi"),
    DELUXE("Deluxe Room", 1450.00, 3, "Spacious room with king bed, city view, mini-bar, and work desk"),
    SUITE("Executive Suite", 2600.00, 4, "Luxury suite with separate living area, panoramic view, and jacuzzi"),
    FAMILY("Family Suite", 3200.00, 5, "Two interconnected bedrooms, kitchenette, and lounge area");

    private final String displayName;
    private final double basePrice;
    private final int capacity;
    private final String description;

    RoomType(String displayName, double basePrice, int capacity, String description) {
        this.displayName = displayName;
        this.basePrice = basePrice;
        this.capacity = capacity;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return displayName + " (GH₵ " + String.format("%,.2f", basePrice) + "/night)";
    }
}