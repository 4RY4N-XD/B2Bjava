package com.project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MarketplaceService {

  
    public static boolean registerBuyer(String name, String email, String password) {
        String sql = "INSERT INTO buyers (name, email, password) VALUES (?, ?, ?)";
        try (Connection c = DBConnection.getConnection(); 
        PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.executeUpdate();
            return true;
        } 
        catch (SQLIntegrityConstraintViolationException ex) {
            return false;
        } 
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Buyer loginBuyer(String email, String password) {
        String sql = "SELECT * FROM buyers WHERE email=? AND password=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Buyer(rs.getString("name"), rs.getString("email"), rs.getString("password"));
                }
            }
        } 
        catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

   
    public static boolean registerSeller(String name, String email, String password) {
        String sql = "INSERT INTO sellers (name, email, password) VALUES (?, ?, ?)";
        try (Connection c = DBConnection.getConnection(); 
            PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.executeUpdate();
            return true;
        } 
        catch (SQLIntegrityConstraintViolationException ex) {
            return false;
        } 
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Seller loginSeller(String email, String password) {
        String sql = "SELECT * FROM sellers WHERE email=? AND password=?";
        try (Connection c = DBConnection.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Seller(rs.getString("name"), rs.getString("email"), rs.getString("password"));
                }
            }
        } 
        catch (SQLException e) { 
            e.printStackTrace();
         }
        return null;
    }

   
    public static boolean addProduct(Product p) {
        String sql = "INSERT INTO products (name, category, price, seller_email) VALUES (?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection(); 
        	PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.name);
            ps.setString(2, p.category);
            ps.setDouble(3, p.price);
            ps.setString(4, p.sellerEmail);
            ps.executeUpdate();
            return true;
        } 
        catch (SQLException e) { 
            e.printStackTrace(); 
            return false; 
        }
    }

    public static List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Connection c = DBConnection.getConnection(); 
        Statement st = c.createStatement(); 
        ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Product(rs.getInt("id"), rs.getString("name"), rs.getString("category"), rs.getDouble("price"), rs.getString("seller_email")));
            }
        } 
        catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return list;
    }

    public static List<Product> getProductsBySeller(String sellerEmail) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE seller_email=?";
        try (Connection c = DBConnection.getConnection(); 
        PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, sellerEmail);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Product(rs.getInt("id"), rs.getString("name"), rs.getString("category"), rs.getDouble("price"), rs.getString("seller_email")));
                }
            }
        } catch (SQLException e) {
             e.printStackTrace();
             }
        return list;
    }

    public static boolean updateProductPrice(int productId, double newPrice) {
        String sql = "UPDATE products SET price=? WHERE id=?";
        try (Connection c = DBConnection.getConnection(); 
        PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, newPrice);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        }
        catch (SQLException e) { 
            e.printStackTrace();
             return false;
             }
    }

    public static boolean deleteProduct(int productId) {
        String sql = "DELETE FROM products WHERE id=?";
        try (Connection c = DBConnection.getConnection(); 
        	PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;
        } 
        catch (SQLException e) { 
            e.printStackTrace(); 
            return false; 
        }
    }

    // Orders
    public static boolean purchaseProduct(String buyerEmail, int productId, int quantity) {
        String q1 = "SELECT price FROM products WHERE id=?";
        String i1 = "INSERT INTO orders (buyer_email, product_id, quantity, total) VALUES (?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection(); 
        PreparedStatement ps = c.prepareStatement(q1)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return false;
                double price = rs.getDouble("price");
                double total = price * quantity;
                try (PreparedStatement ps2 = c.prepareStatement(i1)) {
                    ps2.setString(1, buyerEmail);
                    ps2.setInt(2, productId);
                    ps2.setInt(3, quantity);
                    ps2.setDouble(4, total);
                    ps2.executeUpdate();
                    return true;
                }
            }
        } 
        catch (SQLException e) {
             e.printStackTrace();
             }
        return false;
    }
}
