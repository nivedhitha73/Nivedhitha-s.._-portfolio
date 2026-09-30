package com.example.shop.controller;

import com.example.shop.dao.MenuDAO;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/** CONTROLLER: shows the menu grouped by category. */
@WebServlet(urlPatterns = {"/menu"})
public class MenuController extends HttpServlet {
    private final MenuDAO menu = new MenuDAO();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            req.setAttribute("menu", menu.findGrouped());
            req.getRequestDispatcher("/WEB-INF/views/menu.jsp").forward(req, res);
        } catch (Exception e) { throw new ServletException(e); }
    }
}
