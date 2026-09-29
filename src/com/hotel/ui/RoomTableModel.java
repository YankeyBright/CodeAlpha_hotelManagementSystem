package com.hotel.ui;

import com.hotel.model.Room;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class RoomTableModel extends AbstractTableModel {
    private final String[] columns = {"Room #", "Category", "Rate / Night", "Floor", "Amenities"};
    private final List<Room> rooms = new ArrayList<>();

    public void setRooms(List<Room> newRooms) {
        this.rooms.clear();
        if (newRooms != null) {
            this.rooms.addAll(newRooms);
        }
        fireTableDataChanged();
    }

    public Room getRoomAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < rooms.size()) {
            return rooms.get(rowIndex);
        }
        return null;
    }

    @Override
    public int getRowCount() {
        return rooms.size();
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
        Room r = rooms.get(rowIndex);
        switch (columnIndex) {
            case 0: return r.getRoomId();
            case 1: return r.getType().getDisplayName();
            case 2: return String.format("$%.2f", r.getPricePerNight());
            case 3: return "Floor " + r.getFloor();
            case 4: return r.getAmenities();
            default: return "";
        }
    }
}