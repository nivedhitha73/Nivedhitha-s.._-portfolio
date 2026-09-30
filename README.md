# Scoop Street: Ice Cream Shop (Java, JDBC, MVC)

Browse the menu by category, add items with a quantity, edit the cart, place an order,
and see recent orders. Light and dark mode included.

## Structure (MVC)
- Model:       model/MenuItem, Cart, Order
- View:        WEB-INF/views/*.jsp (+ css/style.css, js/theme.js)
- Controller:  controller/MenuController, CartController, OrderController
- JDBC layer:  dao/DBUtil (connection, tables, sample menu), MenuDAO, OrderDAO (order saved in one transaction)

Database tables: menu_items, orders, order_items. Created and filled automatically on first run (embedded H2 file DB).

## Run
Requires JDK 11+ and Maven 3.6+.

    mvn tomcat7:run

Open http://localhost:8080
Or run `mvn package` and copy target/icecream-shop.war into Tomcat 8/9 webapps.

## MySQL instead of H2
Edit URL/USER/PASS in dao/DBUtil.java and replace the H2 dependency in pom.xml with mysql-connector-j.
