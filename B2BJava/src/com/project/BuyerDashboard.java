package com.project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class BuyerDashboard extends JFrame {
    private Buyer buyer;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField;
    private JComboBox<String> sortBox;

    public BuyerDashboard(Buyer b) {
        this.buyer = b;
        setTitle("Buyer Dashboard - " + b.getName());
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel top = new JPanel();
        top.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        top.add(searchField);
        sortBox = new JComboBox<>(new String[]{"None", "Price: Low to High", "Price: High to Low"});
        top.add(sortBox);
        JButton btnApply = new JButton("Apply");
        top.add(btnApply);
        add(top, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Category", "Price", "Seller"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton btnBuy = new JButton("Buy Selected");
        bottom.add(btnBuy);
        add(bottom, BorderLayout.SOUTH);

        btnApply.addActionListener(e -> refreshTable());
        btnBuy.addActionListener(e -> buySelected());

        refreshTable();
        setVisible(true);
    }

    private void refreshTable() {
        List<Product> list = MarketplaceService.getAllProducts();
        String keyword = searchField.getText().trim().toLowerCase();
        if (!keyword.isEmpty()) {
            list = list.stream()
                    .filter(p -> p.getName().toLowerCase().contains(keyword) || p.getCategory().toLowerCase().contains(keyword))
                    .collect(Collectors.toList());
        }
        String sort = (String) sortBox.getSelectedItem();
        if ("Price: Low to High".equals(sort)) list.sort((a,b) -> Double.compare(a.getPrice(), b.getPrice()));
        else if ("Price: High to Low".equals(sort)) list.sort((a,b) -> Double.compare(b.getPrice(), a.getPrice()));

        tableModel.setRowCount(0);
        for (Product p : list) tableModel.addRow(new Object[]{p.getId(), p.getName(), p.getCategory(), p.getPrice(), p.getSellerEmail()});
    }

    private void buySelected() {
        int row = table.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a product first"); return; }
        int productId = (int) tableModel.getValueAt(row, 0);
        String qtyStr = JOptionPane.showInputDialog(this, "Enter quantity:");
        if (qtyStr == null) return;
        try {
            int qty = Integer.parseInt(qtyStr);
            if (qty <= 0) throw new NumberFormatException();
            boolean ok = MarketplaceService.purchaseProduct(buyer.getEmail(), productId, qty);
            if (ok) JOptionPane.showMessageDialog(this, "Purchase recorded!");
            else JOptionPane.showMessageDialog(this, "Purchase failed.");
        } 
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity");
        }
    }
}
package com.project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SellerDashboard extends JFrame {
    private Seller seller;
    private DefaultTableModel model;
    private JTable table;
    private JTextField txtName, txtCat, txtPrice;

    public SellerDashboard(Seller s) {
        this.seller = s;
        setTitle("Seller Dashboard - " + s.getName());
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new GridLayout(2, 4, 5, 5));
        top.add(new JLabel("Product Name:")); txtName = new JTextField(); top.add(txtName);
        top.add(new JLabel("Category:")); txtCat = new JTextField(); top.add(txtCat);
        top.add(new JLabel("Price:")); txtPrice = new JTextField(); top.add(txtPrice);
        JButton btnAdd = new JButton("Add Product"); top.add(btnAdd);
        JButton btnRefresh = new JButton("Refresh"); top.add(btnRefresh);

        add(top, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{"ID","Name","Category","Price"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton btnUpdatePrice = new JButton("Update Price");
        JButton btnDelete = new JButton("Delete Product");
        bottom.add(btnUpdatePrice); bottom.add(btnDelete);
        add(bottom, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> addProduct());
        btnRefresh.addActionListener(e -> refresh());
        btnUpdatePrice.addActionListener(e -> updatePrice());
        btnDelete.addActionListener(e -> deleteProduct());

        refresh();
        setVisible(true);
    }

    private void addProduct() {
        try {
            String name = txtName.getText().trim();
            String cat = txtCat.getText().trim();
            double price = Double.parseDouble(txtPrice.getText().trim());
            if (name.isEmpty() || cat.isEmpty()) { JOptionPane.showMessageDialog(this, "Fill all fields"); return; }
            Product p = new Product(name, cat, price, seller.getEmail());
            boolean ok = MarketplaceService.addProduct(p);
            if (ok) { JOptionPane.showMessageDialog(this, "Product added"); refresh(); }
            else JOptionPane.showMessageDialog(this, "Add failed");
        } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Invalid price"); }
    }

    private void refresh() {
        model.setRowCount(0);
        List<Product> list = MarketplaceService.getProductsBySeller(seller.getEmail());
        for (Product p : list) model.addRow(new Object[]{p.getId(), p.getName(), p.getCategory(), p.getPrice()});
    }

    private void updatePrice() {
        int r = table.getSelectedRow(); if (r == -1) { JOptionPane.showMessageDialog(this, "Select product"); return; }
        int id = (int) model.getValueAt(r, 0);
        String newPriceStr = JOptionPane.showInputDialog(this, "New price:"); if (newPriceStr == null) return;
        try {
            double np = Double.parseDouble(newPriceStr);
            boolean ok = MarketplaceService.updateProductPrice(id, np);
            if (ok) { JOptionPane.showMessageDialog(this, "Updated"); refresh(); } else JOptionPane.showMessageDialog(this, "Update failed");
        } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Invalid price"); }
    }

    private void deleteProduct() {
        int r = table.getSelectedRow(); if (r == -1) { JOptionPane.showMessageDialog(this, "Select product"); return; }
        int id = (int) model.getValueAt(r, 0);
        int c = JOptionPane.showConfirmDialog(this, "Delete this product?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) { boolean ok = MarketplaceService.deleteProduct(id); if (ok) { JOptionPane.showMessageDialog(this, "Deleted"); refresh(); } else JOptionPane.showMessageDialog(this, "Delete failed"); }
    }
}
