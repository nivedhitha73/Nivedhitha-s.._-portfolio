package com.example.shop.controller;

import com.example.shop.dao.MenuDAO;
import com.example.shop.model.*;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/** CONTROLLER: view the cart, add, change quantity, remove, clear. */
@WebServlet("/cart")
public class CartController extends HttpServlet {
    private final MenuDAO menu = new MenuDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Cart cart = Cart.of(req.getSession());
        String action = req.getParameter("action");
        try {
            int id = req.getParameter("id") == null ? 0 : Integer.parseInt(req.getParameter("id"));
            int qty = req.getParameter("qty") == null ? 1 : Math.max(0, Math.min(20, Integer.parseInt(req.getParameter("qty"))));
            switch (action == null ? "" : action) {
                case "add": {
                    MenuItem item = menu.findAvailable(id);
                    if (item != null && qty > 0) cart.add(item, qty);
                    res.sendRedirect("menu?added=" + java.net.URLEncoder.encode(item == null ? "" : item.getName(), "UTF-8"));
                    return;
                }
                case "update": cart.setQty(id, qty); break;
                case "remove": cart.remove(id); break;
                case "clear": cart.clear(); break;
                default: break;
            }
            res.sendRedirect("cart");
        } catch (Exception e) { throw new ServletException(e); }
    }
}
