package com.superstore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

/**
 * Remote Login GUI for connecting to RMI server.
 * Provides login interface for remote clients.
 */
public class RemoteLoginGUI extends JFrame {
    private SuperStoreClient client;
    private JTextField loginIdField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;

    public RemoteLoginGUI(SuperStoreClient client) {
        this.client = client;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Superstore Management System - Remote Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("Remote Login", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Login form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Login ID
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Login ID:"), gbc);
        gbc.gridx = 1;
        loginIdField = new JTextField(15);
        formPanel.add(loginIdField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        formPanel.add(passwordField, gbc);

        // Login button
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(100, 149, 237));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(new LoginAction());
        formPanel.add(loginButton, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Status bar
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        statusLabel = new JLabel("Connected to server. Please login.");
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        statusPanel.add(statusLabel, BorderLayout.WEST);
        add(statusPanel, BorderLayout.SOUTH);

        // Enter key support
        getRootPane().setDefaultButton(loginButton);

        setVisible(true);
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String loginId = loginIdField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (loginId.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Please enter both login ID and password");
                return;
            }

            loginButton.setEnabled(false);
            statusLabel.setText("Authenticating...");

            // Perform authentication in background thread
            SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    return client.authenticate(loginId, password);
                }

                @Override
                protected void done() {
                    try {
                        boolean success = get();
                        if (success) {
                            statusLabel.setText("Login successful! Launching application...");
                            // Close login window and launch main GUI
                            SwingUtilities.invokeLater(() -> {
                                dispose();
                                client.launchGUI();
                            });
                        } else {
                            statusLabel.setText("Invalid login credentials");
                            loginButton.setEnabled(true);
                        }
                    } catch (Exception ex) {
                        Logger.logException(ex);
                        statusLabel.setText("Connection error: " + ex.getMessage());
                        loginButton.setEnabled(true);
                    }
                }
            };
            worker.execute();
        }
    }
}
