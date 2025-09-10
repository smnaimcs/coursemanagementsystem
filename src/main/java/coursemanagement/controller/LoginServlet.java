package com.coursemanagement.controller;

import com.coursemanagement.dao.UserDAO;
import com.coursemanagement.model.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            switch (user.getRole()) {
                case "admin":
                    response.sendRedirect("admin/dashboard");
                    break;
                case "teacher":
                    response.sendRedirect("teacher/dashboard");
                    break;
                case "student":
                    response.sendRedirect("student/dashboard");
                    break;
                default:
                    request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                    break;
            }
        } else {
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        User user = userDAO.authenticateUser(username, password);
        
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            
            switch (user.getRole()) {
                case "admin":
                    response.sendRedirect("admin/dashboard");
                    break;
                case "teacher":
                    response.sendRedirect("teacher/dashboard");
                    break;
                case "student":
                    response.sendRedirect("student/dashboard");
                    break;
                default:
                    response.sendRedirect("login");
                    break;
            }
        } else {
            request.setAttribute("error", "Invalid username or password");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}
