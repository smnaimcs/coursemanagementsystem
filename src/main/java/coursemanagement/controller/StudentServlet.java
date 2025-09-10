package com.coursemanagement.controller;

import com.coursemanagement.dao.CourseDAO;
import com.coursemanagement.dao.EnrollmentDAO;
import com.coursemanagement.model.User;
import com.coursemanagement.model.Course;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

public class StudentServlet extends HttpServlet {
    private CourseDAO courseDAO;
    private EnrollmentDAO enrollmentDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        courseDAO = new CourseDAO();
        enrollmentDAO = new EnrollmentDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("../login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (!"student".equals(user.getRole())) {
            response.sendRedirect("../login");
            return;
        }
        
        String action = request.getPathInfo();
        if (action == null) action = "/dashboard";
        
        try {
            switch (action) {
                case "/dashboard":
                    showDashboard(request, response, user.getId());
                    break;
                case "/registerCourse":
                    showRegisterCourseForm(request, response, user.getId());
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
        if (!"student".equals(user.getRole())) {
            response.sendRedirect("../login");
            return;
        }
        
        String action = request.getPathInfo();
        if (action == null) action = "/dashboard";
        
        try {
            if ("/registerCourse".equals(action)) {
                registerCourse(request, response, user.getId());
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void showDashboard(HttpServletRequest request, HttpServletResponse response, int studentId) throws ServletException, IOException {
        List<Course> enrolledCourses = enrollmentDAO.getEnrolledCourses(studentId);
        request.setAttribute("enrolledCourses", enrolledCourses);
        request.getRequestDispatcher("/WEB-INF/views/student/dashboard.jsp").forward(request, response);
    }
    
    private void showRegisterCourseForm(HttpServletRequest request, HttpServletResponse response, int studentId) throws ServletException, IOException {
        List<Course> allCourses = courseDAO.getAllCourses();
        List<Course> enrolledCourses = enrollmentDAO.getEnrolledCourses(studentId);
        
        // Remove already enrolled courses
        for (Course enrolled : enrolledCourses) {
            allCourses.removeIf(course -> course.getId() == enrolled.getId());
        }
        
        request.setAttribute("availableCourses", allCourses);
        request.getRequestDispatcher("/WEB-INF/views/student/registerCourse.jsp").forward(request, response);
    }
    
    private void registerCourse(HttpServletRequest request, HttpServletResponse response, int studentId) throws ServletException, IOException {
        int courseId = Integer.parseInt(request.getParameter("courseId"));
        
        boolean success = enrollmentDAO.enrollStudent(studentId, courseId);
        
        if (success) {
            request.setAttribute("message", "Course registered successfully!");
        } else {
            request.setAttribute("error", "Failed to register for course. Please try again.");
        }
        
        showRegisterCourseForm(request, response, studentId);
    }
}
