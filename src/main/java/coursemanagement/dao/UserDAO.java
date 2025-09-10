package com.coursemanagement.dao;

import com.coursemanagement.model.User;
import com.coursemanagement.model.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private Connection connection;
    
    public UserDAO() {
        connection = DatabaseConnection.getConnection();
    }
    
    public User authenticateUser(String username, String password) {
        User user = null;
        try {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    
    public List<User> getAllTeachers() {
        List<User> teachers = new ArrayList<>();
        try {
            String query = "SELECT * FROM users WHERE role = 'teacher'";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                User teacher = new User();
                teacher.setId(rs.getInt("id"));
                teacher.setUsername(rs.getString("username"));
                teacher.setName(rs.getString("name"));
                teacher.setEmail(rs.getString("email"));
                teachers.add(teacher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teachers;
    }
    
    public User getUserById(int id) {
        User user = null;
        try {
            String query = "SELECT * FROM users WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    
    public int getTotalUsersByRole(String role) {
        int count = 0;
        try {
            String query = "SELECT COUNT(*) as total FROM users WHERE role = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, role);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}
