package com.example.app;

import java.sql.*;
import model.User;

public class UserDAO {

    private final String URL = "jdbc:mysql://localhost:3306/testdb";
    private final String USER = "root";
    private final String PASS = "seki@0715";

    // =========================
    // ログイン
    // =========================
    public User login(String id, String password) {

        String sql = "SELECT * FROM users WHERE id=? AND password=?";
    	
    
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setId(rs.getString("id"));
                u.setPassword(rs.getString("password"));
                u.setRole(rs.getString("role"));
                u.setName(rs.getString("name"));
                return u;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // =========================
    // 登録
    // =========================
    public void insertUser(String id,String name, String password, String role) {

        String sql = "INSERT INTO users(id, name, password, role) VALUES(?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.setString(2, name);
            ps.setString(3, password);
            ps.setString(4, role);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}