package com.hotel.ui;

import com.hotel.model.Room;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class RoomTableModel extends AbstractTableModel {
    private final List<Room> rooms = new ArrayList<>();
    private long currentNights = 1;

    public void setData(List<Room> newRooms, long nights) {
        this.rooms.clear();
        if (newRooms != null) {
            this.rooms.addAll(newRooms);
        }
        this.currentNights = nights > 0 ? nights : 1;
        fireTableStructureChanged();
    }

    public Room getRoomAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < rooms.size()) {
            return rooms.get(rowIndex);
        }
        return null;
    }

    public long getCurrentNights() {
        return currentNights;
    }

    @Override
    public int getRowCount() {
        return rooms.size();
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "Room #";
            case 1: return "Category";
            case 2: return "Rate / Night";
            case 3: return "Total (" + currentNights + " night" + (currentNights > 1 ? "s" : "") + ")";
            case 4: return "Floor";
            case 5: return "Amenities";
            default: return "";
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Room r = rooms.get(rowIndex);
        switch (columnIndex) {
            case 0: return r.getRoomId();
            case 1: return r.getType().getDisplayName();
            case 2: return UITheme.formatCurrency(r.getPricePerNight()) + " / night";
            case 3: return UITheme.formatCurrency(r.getPricePerNight() * currentNights);
            case 4: return "Floor " + r.getFloor();
            case 5: return r.getAmenities();
            default: return "";
        }
    }
}