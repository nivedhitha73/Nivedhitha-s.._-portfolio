package com.example.shop.dao;

import com.example.shop.model.*;
import java.sql.*;
import java.util.*;

public class OrderDAO {

    /** Saves an order and its lines in ONE transaction: all rows are saved, or none. */
    public int place(String customer, Cart cart) throws SQLException {
        try (Connection c = DBUtil.getConnection()) {
            c.setAutoCommit(false);
            try {
                int orderId;
                try (PreparedStatement ps = c.prepareStatement("INSERT INTO orders(customer,total) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, customer); ps.setBigDecimal(2, cart.getTotal()); ps.executeUpdate();
                    try (ResultSet k = ps.getGeneratedKeys()) { k.next(); orderId = k.getInt(1); }
                }
                try (PreparedStatement ps = c.prepareStatement("INSERT INTO order_items(order_id,item_name,unit_price,qty) VALUES(?,?,?,?)")) {
                    for (Cart.Line l : cart.getLines()) {
                        ps.setInt(1, orderId); ps.setString(2, l.getItem().getName());
                        ps.setBigDecimal(3, l.getItem().getPrice()); ps.setInt(4, l.getQty()); ps.addBatch();
                    }
                    ps.executeBatch();
                }
                c.commit();
                return orderId;
            } catch (SQLException e) { c.rollback(); throw e; }
        }
    }

    public List<Order> recent(int limit) throws SQLException {
        List<Order> list = new ArrayList<>();
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM orders ORDER BY id DESC LIMIT ?")) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) list.add(read(c, rs)); }
        }
        return list;
    }

    public Order findById(int id) throws SQLException {
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM orders WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? read(c, rs) : null; }
        }
    }

    private Order read(Connection c, ResultSet rs) throws SQLException {
        Order o = new Order(rs.getInt("id"), rs.getString("customer"), rs.getBigDecimal("total"), rs.getTimestamp("created_at"));
        try (PreparedStatement ps = c.prepareStatement("SELECT item_name, unit_price, qty FROM order_items WHERE order_id = ? ORDER BY id")) {
            ps.setInt(1, o.getId());
            try (ResultSet r = ps.executeQuery()) {
                while (r.next()) o.getLines().add(new Order.Line(r.getString(1), r.getBigDecimal(2), r.getInt(3)));
            }
        }
        return o;
    }
}
