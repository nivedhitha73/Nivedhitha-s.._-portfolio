package com.example.shop.controller;

import com.example.shop.dao.OrderDAO;
import com.example.shop.model.*;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/** CONTROLLER: place an order (POST), list recent orders, show one order. */
@WebServlet("/orders")
public class OrderController extends HttpServlet {
    private final OrderDAO orders = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            String id = req.getParameter("id");
            if (id != null) {
                Order o = orders.findById(Integer.parseInt(id));
                if (o == null) { res.sendError(404, "Order not found"); return; }
                req.setAttribute("order", o);
                req.getRequestDispatcher("/WEB-INF/views/order.jsp").forward(req, res);
            } else {
                req.setAttribute("orders", orders.recent(20));
                req.getRequestDispatcher("/WEB-INF/views/orders.jsp").forward(req, res);
            }
        } catch (Exception e) { throw new ServletException(e); }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Cart cart = Cart.of(req.getSession());
        String name = req.getParameter("customer") == null ? "" : req.getParameter("customer").trim();
        if (cart.isEmpty() || name.isEmpty()) { res.sendRedirect("cart"); return; }
        try {
            int id = orders.place(name, cart);
            cart.clear();
            res.sendRedirect("orders?id=" + id + "&placed=1");
        } catch (Exception e) { throw new ServletException(e); }
    }
}
