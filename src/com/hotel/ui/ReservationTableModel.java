package com.hotel.ui;

import com.hotel.model.Reservation;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ReservationTableModel extends AbstractTableModel {
    private final String[] columns = {"Res ID", "Guest Name", "Phone", "Room #", "Check-In", "Check-Out", "Nights", "Total Price", "Status"};
    private final List<Reservation> reservations = new ArrayList<>();

    public void setReservations(List<Reservation> newReservations) {
        this.reservations.clear();
        if (newReservations != null) {
            this.reservations.addAll(newReservations);
        }
        fireTableDataChanged();
    }

    public Reservation getReservationAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < reservations.size()) {
            return reservations.get(rowIndex);
        }
        return null;
    }

    @Override
    public int getRowCount() {
        return reservations.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Reservation res = reservations.get(rowIndex);
        switch (columnIndex) {
            case 0: return res.getReservationId();
            case 1: return res.getGuest().getFullName();
            case 2: return res.getGuest().getPhone();
            case 3: return res.getRoom().getRoomId() + " (" + res.getRoom().getType().name() + ")";
            case 4: return res.getCheckInDate().toString();
            case 5: return res.getCheckOutDate().toString();
            case 6: return res.getNumberOfNights() + " nights";
            case 7: return String.format("$%.2f", res.getTotalPrice());
            case 8: return res.getStatus().getLabel();
            default: return "";
        }
    }
}