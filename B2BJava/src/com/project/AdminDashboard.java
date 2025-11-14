package com.project;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.StringJoiner;

public class AdminDashboard extends JFrame {
    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTextArea txt = new JTextArea();
        txt.setEditable(false);
        add(new JScrollPane(txt), BorderLayout.CENTER);

        StringBuilder sb = new StringBuilder();
        sb.append("=== Buyers ===\n");
        try (Connection c = DBConnection.getConnection(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT name,email FROM buyers")) {
            while (rs.next()) sb.append(rs.getString("name")).append(" - ").append(rs.getString("email")).append("\n");
        } catch (SQLException e) { e.printStackTrace(); }

        sb.append("\n=== Sellers ===\n");
        try (Connection c = DBConnection.getConnection(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT name,email FROM sellers")) {
            while (rs.next()) sb.append(rs.getString("name")).append(" - ").append(rs.getString("email")).append("\n");
        } catch (SQLException e) { e.printStackTrace(); }

        sb.append("\n=== Products ===\n");
        try (Connection c = DBConnection.getConnection(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT id,name,category,price,seller_email FROM products")) {
            while (rs.next()) sb.append("[").append(rs.getInt("id")).append("] ").append(rs.getString("name")).append(" - ").append(rs.getString("category")).append(" - $").append(rs.getDouble("price")).append(" | ").append(rs.getString("seller_email")).append("\n");
        } catch (SQLException e) { e.printStackTrace(); }

        txt.setText(sb.toString());
        setVisible(true);
    }
}

