package com.example.shop.dao;

import com.example.shop.model.MenuItem;
import java.sql.*;
import java.util.*;

public class MenuDAO {
    private MenuItem map(ResultSet rs) throws SQLException {
        return new MenuItem(rs.getInt("id"), rs.getString("name"), rs.getString("category"),
                rs.getString("description"), rs.getBigDecimal("price"));
    }

    /** Available items grouped by category, in menu order. */
    public Map<String, List<MenuItem>> findGrouped() throws SQLException {
        Map<String, List<MenuItem>> out = new LinkedHashMap<>();
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT * FROM menu_items WHERE available = TRUE ORDER BY category, name")) {
            while (rs.next()) { MenuItem m = map(rs); out.computeIfAbsent(m.getCategory(), k -> new ArrayList<>()).add(m); }
        }
        return out;
    }

    public MenuItem findAvailable(int id) throws SQLException {
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM menu_items WHERE id = ? AND available = TRUE")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? map(rs) : null; }
        }
    }
}
