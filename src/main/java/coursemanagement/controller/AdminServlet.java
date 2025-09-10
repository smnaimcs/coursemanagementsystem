package com.coursemanagement.controller;

import com.coursemanagement.dao.UserDAO;
import com.coursemanagement.dao.CourseDAO;
import com.coursemanagement.model.User;
import com.coursemanagement.model.Course;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

public class AdminServlet extends HttpServlet {
    private UserDAO userDAO;
    private CourseDAO courseDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
        courseDAO = new CourseDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("../login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (!"admin".equals(user.getRole())) {
            response.sendRedirect("../login");
            return;
        }
        
        String action = request.getPathInfo();
        if (action == null) action = "/dashboard";
        
        try {
            switch (action) {
                case "/dashboard":
                    showDashboard(request, response);
                    break;
                case "/addCourse":
                    showAddCourseForm(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("../login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (!"admin".equals(user.getRole())) {
            response.sendRedirect("../login");
            return;
        }
        
        String action = request.getPathInfo();
        if (action == null) action = "/dashboard";
        
        try {
            if ("/addCourse".equals(action)) {
                addCourse(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void showDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int totalCourses = courseDAO.getTotalCourses();
        int totalTeachers = userDAO.getTotalUsersByRole("teacher");
        int totalStudents = userDAO.getTotalUsersByRole("student");
        
        request.setAttribute("totalCourses", totalCourses);
        request.setAttribute("totalTeachers", totalTeachers);
        request.setAttribute("totalStudents", totalStudents);
        
        request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
    }
    
    private void showAddCourseForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> teachers = userDAO.getAllTeachers();
        request.setAttribute("teachers", teachers);
        request.getRequestDispatcher("/WEB-INF/views/admin/addCourse.jsp").forward(request, response);
    }
    
    private void addCourse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        int teacherId = Integer.parseInt(request.getParameter("teacherId"));
        
        Course course = new Course();
        course.setTitle(title);
        course.setDescription(description);
        course.setTeacherId(teacherId);
        
        boolean success = courseDAO.addCourse(course);
        
        if (success) {
            request.setAttribute("message", "Course added successfully!");
        } else {
            request.setAttribute("error", "Failed to add course. Please try again.");
        }
        
        List<User> teachers = userDAO.getAllTeachers();
        request.setAttribute("teachers", teachers);
        request.getRequestDispatcher("/WEB-INF/views/admin/addCourse.jsp").forward(request, response);
    }
}
