package com.github.igorjsm.controledealuguel.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.github.igorjsm.controledealuguel.model.Reservation;

public class ReservationDAO {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public void save(Reservation reservation, String[] itemQuantities) {
        String sql = "INSERT INTO reservations (renter_name, delivery_date, pickup_date, address, is_paid, is_picked_up, items) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, reservation.getRenterName());
            pstmt.setString(2, reservation.getDeliveryDate().format(DATE_FORMATTER));
            pstmt.setString(3, reservation.getPickupDate().format(DATE_FORMATTER));
            pstmt.setString(4, reservation.getAddress());
            pstmt.setBoolean(5, reservation.isPaid());
            pstmt.setBoolean(6, reservation.isPickedUp());

            // Limpar IDs dos itens
            String cleanedItems = String.join(",", cleanItemIds(itemQuantities));
            pstmt.setString(7, cleanedItems);
            pstmt.executeUpdate();

            // Atualizar quantidades disponíveis dos itens
            updateItemQuantities(itemQuantities, false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String[] cleanItemIds(String[] itemQuantities) {
        String[] cleanedItemQuantities = new String[itemQuantities.length];
        for (int i = 0; i < itemQuantities.length; i++) {
            String[] parts = itemQuantities[i].split("=");
            String numericPart = parts[0].replaceAll("\\D+", "");
            cleanedItemQuantities[i] = numericPart + "=" + parts[1];
        }
        return cleanedItemQuantities;
    }

    public List<Reservation> getAll() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations";
        try (Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(rs.getInt("id"));
                reservation.setRenterName(rs.getString("renter_name"));
                reservation.setDeliveryDate(LocalDate.parse(rs.getString("delivery_date"), DATE_FORMATTER));
                reservation.setPickupDate(LocalDate.parse(rs.getString("pickup_date"), DATE_FORMATTER));
                reservation.setAddress(rs.getString("address"));
                reservation.setPaid(rs.getBoolean("is_paid"));
                reservation.setPickedUp(rs.getBoolean("is_picked_up"));
                reservation.setItems(rs.getString("items"));
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public Reservation getById(int id) {
        String sql = "SELECT * FROM reservations WHERE id = ?";
        Reservation reservation = null;
        try (Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    reservation = new Reservation();
                    reservation.setId(rs.getInt("id"));
                    reservation.setRenterName(rs.getString("renter_name"));
                    reservation.setDeliveryDate(LocalDate.parse(rs.getString("delivery_date"), DATE_FORMATTER));
                    reservation.setPickupDate(LocalDate.parse(rs.getString("pickup_date"), DATE_FORMATTER));
                    reservation.setAddress(rs.getString("address"));
                    reservation.setPaid(rs.getBoolean("is_paid"));
                    reservation.setPickedUp(rs.getBoolean("is_picked_up"));
                    reservation.setItems(rs.getString("items"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservation;
    }

    public void update(Reservation reservation, String[] itemQuantities) {
        String sql = "UPDATE reservations SET renter_name = ?, delivery_date = ?, pickup_date = ?, address = ?, is_paid = ?, is_picked_up = ?, items = ? WHERE id = ?";
        try (Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Reverter as quantidades de itens antes de atualizar a reserva
            revertItemQuantities(reservation.getId());

            pstmt.setString(1, reservation.getRenterName());
            pstmt.setString(2, reservation.getDeliveryDate().format(DATE_FORMATTER));
            pstmt.setString(3, reservation.getPickupDate().format(DATE_FORMATTER));
            pstmt.setString(4, reservation.getAddress());
            pstmt.setBoolean(5, reservation.isPaid());
            pstmt.setBoolean(6, reservation.isPickedUp());
            pstmt.setString(7, String.join(",", itemQuantities));
            pstmt.setInt(8, reservation.getId());
            pstmt.executeUpdate();

            // Atualizar quantidades disponíveis dos itens após a atualização da reserva
            updateItemQuantities(itemQuantities, false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM reservations WHERE id = ?";
        try (Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Reverter as quantidades de itens antes de deletar a reserva
            revertItemQuantities(id);

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateItemQuantities(String[] itemQuantities, boolean revert) {
        String sql = "UPDATE items SET available_quantity = available_quantity + ? WHERE id = ?";
        try (Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (String itemQuantity : itemQuantities) {
                String[] parts = itemQuantity.split("=");

                // Remover caracteres não numéricos do ID
                String numericPart = parts[0].replaceAll("\\D+", "");
                int itemId = Integer.parseInt(numericPart.trim());
                int quantity = Integer.parseInt(parts[1].trim());

                int quantityUpdate = revert ? quantity : -quantity;

                pstmt.setInt(1, quantityUpdate);
                pstmt.setInt(2, itemId);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void revertItemQuantities(int reservationId) {
        Reservation reservation = getById(reservationId);
        if (reservation != null) {
            String[] itemQuantities = reservation.getItems().split(",");
            updateItemQuantities(itemQuantities, true);
        }
    }

    public boolean areItemsAvailable(String itemsText) {
        String sql = "SELECT available_quantity FROM items WHERE id = ? AND available_quantity >= ?";
        try (Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String[] itemsArray = itemsText.split(",");
            for (String itemQuantity : itemsArray) {
                String[] parts = itemQuantity.split("=");

                // Remova quaisquer caracteres não numéricos dos IDs
                int itemId = Integer.parseInt(parts[0].trim().replaceAll("\\D+", ""));
                int quantity = Integer.parseInt(parts[1].trim());

                pstmt.setInt(1, itemId);
                pstmt.setInt(2, quantity);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (!rs.next() || rs.getInt("available_quantity") < quantity) {
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
}
