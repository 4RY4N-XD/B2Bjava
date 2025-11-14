package com.project;

//Seller.java
public class Seller {
private String name;
private String email;
private String password;

public Seller(String name, String email, String password) {
   this.name = name;
   this.email = email;
   this.password = password;
}

public String getName() { return name; }
public String getEmail() { return email; }
public String getPassword() { return password; }
}