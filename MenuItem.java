package com.example.shop.model;

import java.math.BigDecimal;

/** MODEL: one item on the menu. */
public class MenuItem {
    private final int id;
    private final String name, category, description;
    private final BigDecimal price;
    public MenuItem(int id, String name, String category, String description, BigDecimal price) {
        this.id = id; this.name = name; this.category = category; this.description = description; this.price = price;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
}
