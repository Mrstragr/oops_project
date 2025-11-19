package com.superstore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * LoginGUI class for user authentication using Swing.
 */
public class LoginGUI extends JFrame {
    private JTextField loginIdField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private Server server;

    public LoginGUI(Server server) {
        this.server = server;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Superstore Management System - Login");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180)); // Steel Blue
        JLabel titleLabel = new JLabel("Welcome to Superstore Management", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Center Panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(245, 245, 245));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        JLabel loginLabel = new JLabel("Login ID:");
        loginLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        centerPanel.add(loginLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        loginIdField = new JTextField(15);
        loginIdField.setFont(new Font("Arial", Font.PLAIN, 14));
        loginIdField.setToolTipText("Enter your login ID");
        centerPanel.add(loginIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        centerPanel.add(passwordLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setToolTipText("Enter your password");
        centerPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(34, 139, 34)); // Forest Green
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setToolTipText("Click to login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        centerPanel.add(loginButton, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(70, 130, 180));
        JLabel footerLabel = new JLabel("© 2023 Superstore Management System", JLabel.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        footerLabel.setForeground(Color.WHITE);
        footerPanel.add(footerLabel);
        add(footerPanel, BorderLayout.SOUTH);

        // Set focus to login ID field
        loginIdField.requestFocusInWindow();

        setVisible(true);
    }

    private void handleLogin() {
        String loginId = loginIdField.getText();
        String password = new String(passwordField.getPassword());

        if (server.authenticate(loginId, password)) {
            User user = server.getUser(loginId);
            JOptionPane.showMessageDialog(this, "Welcome, " + user.getName(), "Login Successful", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Close login window
            openUserGUI(user);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials. Try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openUserGUI(User user) {
        switch (user.getUserType()) {
            case SUPER_USER:
                new SuperUserGUI(server, (SuperUser) user);
                break;
            case WAREHOUSE_ADMIN:
                new WarehouseAdminGUI(server, (WarehouseAdmin) user);
                break;
            case STORE_ADMIN:
                new StoreAdminGUI(server, (StoreAdmin) user);
                break;
            case WAREHOUSE_KEEPER:
                new WarehouseKeeperGUI(server, (WarehouseKeeper) user);
                break;
            case STORE_KEEPER:
                new StoreKeeperGUI(server, (StoreKeeper) user);
                break;
            case END_USER:
                new EndUserGUI(server, (EndUser) user);
                break;
        }
    }
}
