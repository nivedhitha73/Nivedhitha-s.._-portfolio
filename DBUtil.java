package com.example.shop.dao;

import java.math.BigDecimal;
import java.sql.*;

/** JDBC helper: opens connections, creates tables and seeds the menu on first run (embedded H2 file DB). */
public class DBUtil {
    // For MySQL: change URL/USER/PASS and swap the H2 dependency in pom.xml for mysql-connector-j.
    private static final String URL = "jdbc:h2:~/icecream_shop_db;AUTO_SERVER=TRUE";
    private static final String USER = "sa", PASS = "";

    static {
        try {
            Class.forName("org.h2.Driver");
            try (Connection c = getConnection(); Statement s = c.createStatement()) {
                s.execute("CREATE TABLE IF NOT EXISTS menu_items(id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(80) NOT NULL," +
                        " category VARCHAR(40) NOT NULL, description VARCHAR(200), price DECIMAL(8,2) NOT NULL, available BOOLEAN DEFAULT TRUE)");
                s.execute("CREATE TABLE IF NOT EXISTS orders(id INT AUTO_INCREMENT PRIMARY KEY, customer VARCHAR(80) NOT NULL," +
                        " total DECIMAL(10,2) NOT NULL, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
                s.execute("CREATE TABLE IF NOT EXISTS order_items(id INT AUTO_INCREMENT PRIMARY KEY, order_id INT NOT NULL," +
                        " item_name VARCHAR(80) NOT NULL, unit_price DECIMAL(8,2) NOT NULL, qty INT NOT NULL," +
                        " FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE)");
                try (ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM menu_items")) { rs.next(); if (rs.getInt(1) == 0) seed(c); }
            }
        } catch (Exception e) { throw new ExceptionInInitializerError(e); }
    }

    private static void seed(Connection c) throws SQLException {
        String[][] d = {
            {"Vanilla Bean","Scoops","Madagascar vanilla, slow churned","3.50"},
            {"Chocolate Fudge","Scoops","Dark cocoa with fudge ribbons","3.75"},
            {"Strawberry Swirl","Scoops","Fresh strawberries and cream","3.75"},
            {"Mint Chip","Scoops","Cool mint with dark chocolate chips","3.75"},
            {"Pistachio","Scoops","Roasted pistachio, lightly salted","4.25"},
            {"Hot Fudge Sundae","Sundaes","Three scoops, hot fudge, whipped cream, cherry","7.25"},
            {"Banana Split","Sundaes","Banana, three flavors, pineapple and nuts","8.50"},
            {"Vanilla Shake","Shakes","Thick vanilla milkshake","5.50"},
            {"Chocolate Shake","Shakes","Thick chocolate milkshake","5.75"},
            {"Waffle Cone","Extras","Baked fresh every morning","1.25"},
            {"Rainbow Sprinkles","Extras","A generous handful","0.75"}
        };
        try (PreparedStatement ps = c.prepareStatement("INSERT INTO menu_items(name,category,description,price) VALUES(?,?,?,?)")) {
            for (String[] r : d) { ps.setString(1, r[0]); ps.setString(2, r[1]); ps.setString(3, r[2]); ps.setBigDecimal(4, new BigDecimal(r[3])); ps.addBatch(); }
            ps.executeBatch();
        }
    }

    public static Connection getConnection() throws SQLException { return DriverManager.getConnection(URL, USER, PASS); }
}
