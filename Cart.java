package com.example.shop.model;

import java.math.BigDecimal;
import java.util.*;
import javax.servlet.http.HttpSession;

/** MODEL: the shopping cart, kept in the HTTP session. */
public class Cart {
    public static class Line {
        private final MenuItem item; private int qty;
        Line(MenuItem item, int qty) { this.item = item; this.qty = qty; }
        public MenuItem getItem() { return item; }
        public int getQty() { return qty; }
        public BigDecimal getSubtotal() { return item.getPrice().multiply(BigDecimal.valueOf(qty)); }
    }

    private final Map<Integer, Line> lines = new LinkedHashMap<>();

    public static Cart of(HttpSession s) {
        Cart c = (Cart) s.getAttribute("cart");
        if (c == null) { c = new Cart(); s.setAttribute("cart", c); }
        return c;
    }
    public void add(MenuItem item, int qty) {
        Line l = lines.get(item.getId());
        if (l == null) lines.put(item.getId(), new Line(item, qty)); else l.qty += qty;
    }
    public void setQty(int id, int qty) {
        if (qty <= 0) lines.remove(id); else if (lines.containsKey(id)) lines.get(id).qty = qty;
    }
    public void remove(int id) { lines.remove(id); }
    public void clear() { lines.clear(); }
    public Collection<Line> getLines() { return lines.values(); }
    public boolean isEmpty() { return lines.isEmpty(); }
    public int getCount() { return lines.values().stream().mapToInt(Line::getQty).sum(); }
    public BigDecimal getTotal() {
        return lines.values().stream().map(Line::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
