package com.project;

import java.sql.*;

import java.sql.Connection;

public class Creator {
	public static void create() {
	
	try (Connection con = DBConnection.getConnection();
		 Statement st = con.createStatement()){
		
		st.execute("CREATE TABLE IF NOT EXISTS buyers ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY,"
				+ "name VARCHAR(100),"
				+ "email VARCHAR(120) UNIQUE,"
				+ "password VARCHAR(200))");
		
		
		st.execute("CREATE TABLE IF NOT EXISTS sellers ("
				+ "  id INT AUTO_INCREMENT PRIMARY KEY,"
				+ "  name VARCHAR(100),"
				+ "  email VARCHAR(120) UNIQUE,"
				+ "  password VARCHAR(200)"
				+ ")");
		
		st.execute("CREATE TABLE IF NOT EXISTS products ("
				+ "  id INT AUTO_INCREMENT PRIMARY KEY,"
				+ "  name VARCHAR(150),"
				+ "  category VARCHAR(100),"
				+ "  price DOUBLE,"
				+ "  seller_email VARCHAR(120),"
				+ "  FOREIGN KEY (seller_email) REFERENCES sellers(email) ON DELETE CASCADE"
				+ ")");
		
		st.execute("CREATE TABLE IF NOT EXISTS orders ("
				+ "  id INT AUTO_INCREMENT PRIMARY KEY,"
				+ "  buyer_email VARCHAR(120),"
				+ "  product_id INT,"
				+ "  quantity INT,"
				+ "  total DOUBLE,"
				+ "  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
				+ "  FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE SET NULL"
				+ ")");
	}
	
	catch(SQLException ex) {
		ex.printStackTrace();
	}
	}
}
