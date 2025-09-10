package com.coursemanagement.dao;

import com.coursemanagement.model.Course;
import com.coursemanagement.model.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    private Connection connection;
    
    public CourseDAO() {
        connection = DatabaseConnection.getConnection();
    }
    
    public boolean addCourse(Course course) {
        try {
            String query = "INSERT INTO courses (title, description, teacher_id) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, course.getTitle());
            ps.setString(2, course.getDescription());
            ps.setInt(3, course.getTeacherId());
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        try {
            String query = "SELECT c.*, u.name as teacher_name FROM courses c LEFT JOIN users u ON c.teacher_id = u.id";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
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
    
    public List<Course> getCoursesByTeacherId(int teacherId) {
        List<Course> courses = new ArrayList<>();
        try {
            String query = "SELECT c.*, u.name as teacher_name FROM courses c LEFT JOIN users u ON c.teacher_id = u.id WHERE c.teacher_id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, teacherId);
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
    
    public Course getCourseById(int id) {
        Course course = null;
        try {
            String query = "SELECT c.*, u.name as teacher_name FROM courses c LEFT JOIN users u ON c.teacher_id = u.id WHERE c.id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                course = new Course();
                course.setId(rs.getInt("id"));
                course.setTitle(rs.getString("title"));
                course.setDescription(rs.getString("description"));
                course.setTeacherId(rs.getInt("teacher_id"));
                course.setTeacherName(rs.getString("teacher_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return course;
    }
    
    public int getTotalCourses() {
        int count = 0;
        try {
            String query = "SELECT COUNT(*) as total FROM courses";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            if (rs.next()) {
                count = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}
