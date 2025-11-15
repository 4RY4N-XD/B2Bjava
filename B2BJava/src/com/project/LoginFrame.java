package com.project;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField txtName, txtEmail;
    private JPasswordField txtPass;
    private JRadioButton rBuyer, rSeller, rAdmin;

    public LoginFrame() {
        setTitle("Marketplace Login");
        setSize(420, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel title = new JLabel("Alibaba Marketplace", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(70, 10, 280, 30);
        add(title);

        JLabel lName = new JLabel("Name:"); lName.setBounds(30, 60, 80, 25); add(lName);
        txtName = new JTextField(); txtName.setBounds(120, 60, 250, 25); add(txtName);

        JLabel lEmail = new JLabel("Email:"); lEmail.setBounds(30, 100, 80, 25); add(lEmail);
        txtEmail = new JTextField(); txtEmail.setBounds(120, 100, 250, 25); add(txtEmail);

        JLabel lPass = new JLabel("Password:"); lPass.setBounds(30, 140, 80, 25); add(lPass);
        txtPass = new JPasswordField(); txtPass.setBounds(120, 140, 250, 25); add(txtPass);

        rBuyer = new JRadioButton("Buyer"); rSeller = new JRadioButton("Seller"); rAdmin = new JRadioButton("Admin");
        ButtonGroup bg = new ButtonGroup(); bg.add(rBuyer); bg.add(rSeller); bg.add(rAdmin);
        rBuyer.setBounds(60, 180, 90, 25); rSeller.setBounds(160, 180, 90, 25); rAdmin.setBounds(260, 180, 90, 25);
        add(rBuyer); add(rSeller); add(rAdmin);

        JButton btnLogin = new JButton("Login"); btnLogin.setBounds(90, 230, 100, 30); add(btnLogin);
        JButton btnRegister = new JButton("Register"); btnRegister.setBounds(220, 230, 100, 30); add(btnRegister);

        btnLogin.addActionListener(e -> onLogin());
        btnRegister.addActionListener(e -> onRegister());

        setVisible(true);
    }

    private void onLogin() {
        String email = txtEmail.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();

        if (rBuyer.isSelected()) {
            Buyer b = MarketplaceService.loginBuyer(email, pass);
            if (b != null) { dispose(); new BuyerDashboard(b); }
            else JOptionPane.showMessageDialog(this, "Invalid buyer credentials");
        } else if (rSeller.isSelected()) {
            Seller s = MarketplaceService.loginSeller(email, pass);
            if (s != null) { dispose(); new SellerDashboard(s); }
            else JOptionPane.showMessageDialog(this, "Invalid seller credentials");
        } else if (rAdmin.isSelected()) {
            if (email.equals("admin") && pass.equals("admin")) { dispose(); new AdminDashboard(); }
            else JOptionPane.showMessageDialog(this, "Invalid admin credentials (admin/admin)");
        } else {
            JOptionPane.showMessageDialog(this, "Select a role");
        }
    }

    private void onRegister() {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();

        if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) { JOptionPane.showMessageDialog(this, "All fields are required"); return; }

        if (rBuyer.isSelected()) {
            if (MarketplaceService.registerBuyer(name, email, pass)) JOptionPane.showMessageDialog(this, "Buyer registered! Please login.");
            else JOptionPane.showMessageDialog(this, "Registration failed. Email may already exist.");
        } else if (rSeller.isSelected()) {
            if (MarketplaceService.registerSeller(name, email, pass)) JOptionPane.showMessageDialog(this, "Seller registered! Please login.");
            else JOptionPane.showMessageDialog(this, "Registration failed. Email may already exist.");
        } else {
            JOptionPane.showMessageDialog(this, "Select Buyer or Seller to register");
        }
    }
<<<<<<< HEAD
    
}
=======
}
>>>>>>> 02c19d79d0bf45a1171d01223e2431952f19dfbf
