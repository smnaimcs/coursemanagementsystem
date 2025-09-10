package com.coursemanagement.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class DatabaseConnection {
    private static Connection connection = null;
    
    static {
        try {
            Properties props = new Properties();
            InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("database.properties");
            props.load(input);
            
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() {
        return connection;
    }
}
