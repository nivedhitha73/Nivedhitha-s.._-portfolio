package com.example.shop.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/** MODEL: a placed order and its lines. */
public class Order {
    public static class Line {
        private final String name; private final BigDecimal price; private final int qty;
        public Line(String name, BigDecimal price, int qty) { this.name = name; this.price = price; this.qty = qty; }
        public String getName() { return name; }
        public BigDecimal getPrice() { return price; }
        public int getQty() { return qty; }
        public BigDecimal getSubtotal() { return price.multiply(BigDecimal.valueOf(qty)); }
    }
    private final int id; private final String customer; private final BigDecimal total; private final Timestamp createdAt;
    private final List<Line> lines = new ArrayList<>();
    public Order(int id, String customer, BigDecimal total, Timestamp createdAt) {
        this.id = id; this.customer = customer; this.total = total; this.createdAt = createdAt;
    }
    public int getId() { return id; }
    public String getCustomer() { return customer; }
    public BigDecimal getTotal() { return total; }
    public Timestamp getCreatedAt() { return createdAt; }
    public List<Line> getLines() { return lines; }
}
