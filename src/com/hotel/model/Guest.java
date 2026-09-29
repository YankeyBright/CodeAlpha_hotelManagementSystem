package com.hotel.model;

/**
 * Represents a registered hotel guest with their contact and identification details.
 */
public class Guest {
    private String guestId;
    private String fullName;
    private String phone;
    private String email;

    public Guest(String guestId, String fullName, String phone, String email) {
        this.guestId = guestId;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
    }

    public String getGuestId() {
        return guestId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Serializes guest data to CSV format.
     */
    public String toCsv() {
        return guestId + ",\"" + fullName + "\"," + phone + "," + (email == null ? "" : email);
    }

    @Override
    public String toString() {
        return fullName + " (" + phone + ")";
    }
}
