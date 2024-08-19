package com.github.igorjsm.controledealuguel.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.github.igorjsm.controledealuguel.model.Item;

public class ItemDAO {
    public void save(Item item) {
        String sql = "INSERT INTO items(description, total_quantity, available_quantity) VALUES(?, ?, ?)";
        try (Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.getDescription());
            pstmt.setInt(2, item.getTotalQuantity());
            pstmt.setInt(3, item.getTotalQuantity()); // Inicializa disponível com total
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Item> getAll() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items";
        try (Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Item item = new Item();
                item.setId(rs.getInt("id"));
                item.setDescription(rs.getString("description"));
                item.setTotalQuantity(rs.getInt("total_quantity"));
                item.setAvailableQuantity(rs.getInt("available_quantity")); // Usar quantidade disponível

                items.add(item);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return items;
    }

    public Item getById(int id) {
        String sql = "SELECT * FROM items WHERE id = ?";
        Item item = null;
        try (Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    item = new Item();
                    item.setId(rs.getInt("id"));
                    item.setDescription(rs.getString("description"));
                    item.setTotalQuantity(rs.getInt("total_quantity"));
                    item.setAvailableQuantity(rs.getInt("available_quantity")); // Usar quantidade disponível
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return item;
    }

    public void update(Item item) {
        String sql = "UPDATE items SET description = ?, total_quantity = ?, available_quantity = ? WHERE id = ?";
        try (Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.getDescription());
            pstmt.setInt(2, item.getTotalQuantity());
            pstmt.setInt(3, item.getAvailableQuantity()); // Atualiza quantidade disponível
            pstmt.setInt(4, item.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM items WHERE id = ?";
        try (Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
