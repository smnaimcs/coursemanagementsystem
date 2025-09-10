package com.coursemanagement.dao;

import com.coursemanagement.model.User;
import com.coursemanagement.model.Course;
import com.coursemanagement.model.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {
    private Connection connection;
    
    public EnrollmentDAO() {
        connection = DatabaseConnection.getConnection();
    }
    
    public boolean enrollStudent(int studentId, int courseId) {
        try {
            String query = "INSERT INTO enrollments (student_id, course_id) VALUES (?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Course> getEnrolledCourses(int studentId) {
        List<Course> courses = new ArrayList<>();
        try {
            String query = "SELECT c.*, u.name as teacher_name FROM courses c " +
                          "JOIN enrollments e ON c.id = e.course_id " +
                          "LEFT JOIN users u ON c.teacher_id = u.id " +
                          "WHERE e.student_id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Course course = new Course();
                course.setId(rs.getInt("id"));
                course.setTitle(rs.getString("title"));
                course.setDescription(rs.getString("description"));
                course.setTeacherId(rs.getInt("teacher_id"));
                course.setTeacherName(rs.getString("teacher_name"));
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }
    
    public List<User> getEnrolledStudents(int courseId) {
        List<User> students = new ArrayList<>();
        try {
            String query = "SELECT u.* FROM users u " +
                          "JOIN enrollments e ON u.id = e.student_id " +
                          "WHERE e.course_id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                User student = new User();
                student.setId(rs.getInt("id"));
                student.setUsername(rs.getString("username"));
                student.setName(rs.getString("name"));
                student.setEmail(rs.getString("email"));
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
    
    public boolean isStudentEnrolled(int studentId, int courseId) {
        try {
            String query = "SELECT * FROM enrollments WHERE student_id = ? AND course_id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
