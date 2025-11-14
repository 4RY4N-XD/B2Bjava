package com.project;

//Product.java
public class Product {
 private int id;
 String name;
 String category;
 double price;
 String sellerEmail;

 public Product(int id, String name, String category, double price, String sellerEmail) {
     this.id = id;
     this.name = name;
     this.category = category;
     this.price = price;
     this.sellerEmail = sellerEmail;
 }

 public Product(String name, String category, double price, String sellerEmail) {
     this(-1, name, category, price, sellerEmail);
 }

 public int getId() { return id; }
 public String getName() { return name; }
 public String getCategory() { return category; }
 public double getPrice() { return price; }
 public String getSellerEmail() { return sellerEmail; }

 @Override
 public String toString() {
     return String.format("%s (%s) - $%.2f | Seller: %s", name, category, price, sellerEmail);
 }
}