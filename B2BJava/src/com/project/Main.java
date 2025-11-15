package com.project;


import javax.swing.SwingUtilities;

public class Main {
 public static void main(String[] args) {
     Creator.create();
     SwingUtilities.invokeLater(() -> new LoginFrame());
 }
}

