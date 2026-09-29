package com.hotel.model;

/**
 * Represents a physical hotel room with its category, rate, floor, and amenities.
 */
public class Room {
    private String roomId;
    private RoomType type;
    private double pricePerNight;
    private int floor;
    private String amenities;

    public Room(String roomId, RoomType type, double pricePerNight, int floor, String amenities) {
        this.roomId = roomId;
        this.type = type;
        this.pricePerNight = pricePerNight;
        this.floor = floor;
        this.amenities = amenities;
    }

    public Room(String roomId, RoomType type, int floor, String amenities) {
        this(roomId, type, type.getBasePrice(), floor, amenities);
    }

    public String getRoomId() {
        return roomId;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public int getFloor() {
        return floor;
    }

    public String getAmenities() {
        return amenities;
    }

    /**
     * Serializes room data to CSV line.
     */
    public String toCsv() {
        return roomId + "," + type.name() + "," + pricePerNight + "," + floor + ",\"" + amenities + "\"";
    }

    @Override
    public String toString() {
        return "Room " + roomId + " (" + type.getDisplayName() + ") - $" + String.format("%.2f", pricePerNight) + "/night";
    }
}
