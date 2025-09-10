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

public class TeacherServlet extends HttpServlet {
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
        if (!"teacher".equals(user.getRole())) {
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
                case "/viewStudents":
                    viewStudents(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void showDashboard(HttpServletRequest request, HttpServletResponse response, int teacherId) throws ServletException, IOException {
        List<Course> assignedCourses = courseDAO.getCoursesByTeacherId(teacherId);
        request.setAttribute("assignedCourses", assignedCourses);
        request.getRequestDispatcher("/WEB-INF/views/teacher/dashboard.jsp").forward(request, response);
    }
    
    private void viewStudents(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int courseId = Integer.parseInt(request.getParameter("courseId"));
        Course course = courseDAO.getCourseById(courseId);
        
        if (course != null) {
            List<User> students = enrollmentDAO.getEnrolledStudents(courseId);
            request.setAttribute("course", course);
            request.setAttribute("students", students);
            request.getRequestDispatcher("/WEB-INF/views/teacher/viewStudents.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
