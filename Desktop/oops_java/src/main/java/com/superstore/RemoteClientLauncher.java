package com.superstore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Launcher for remote clients.
 * Allows users to specify server host and connect to RMI server.
 */
public class RemoteClientLauncher extends JFrame {
    private JTextField serverHostField;
    private JButton connectButton;
    private JLabel statusLabel;

    public RemoteClientLauncher() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Superstore Management System - Remote Client");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("Connect to Server", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Connection form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Server host
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Server Host:"), gbc);
        gbc.gridx = 1;
        serverHostField = new JTextField("localhost", 15);
        formPanel.add(serverHostField, gbc);

        // Connect button
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        connectButton = new JButton("Connect to Server");
        connectButton.setBackground(new Color(100, 149, 237));
        connectButton.setForeground(Color.WHITE);
        connectButton.addActionListener(new ConnectAction());
        formPanel.add(connectButton, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Status bar
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        statusLabel = new JLabel("Enter server host and click Connect");
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        statusPanel.add(statusLabel, BorderLayout.WEST);
        add(statusPanel, BorderLayout.SOUTH);

        // Enter key support
        getRootPane().setDefaultButton(connectButton);

        setVisible(true);
    }

    private class ConnectAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String serverHost = serverHostField.getText().trim();

            if (serverHost.isEmpty()) {
                statusLabel.setText("Please enter server host");
                return;
            }

            connectButton.setEnabled(false);
            statusLabel.setText("Connecting to server...");

            // Connect in background thread
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                private SuperStoreClient client;

                @Override
                protected Void doInBackground() throws Exception {
                    client = new SuperStoreClient(serverHost);
                    return null;
                }

                @Override
                protected void done() {
                    try {
                        get(); // Check for exceptions
                        statusLabel.setText("Connected successfully! Opening login...");
                        // Close launcher and open login
                        SwingUtilities.invokeLater(() -> {
                            dispose();
                            new RemoteLoginGUI(client);
                        });
                    } catch (Exception ex) {
                        Logger.logException(ex);
                        statusLabel.setText("Connection failed: " + ex.getMessage());
                        connectButton.setEnabled(true);
                    }
                }
            };
            worker.execute();
        }
    }

    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            Logger.logException(e);
        }

        SwingUtilities.invokeLater(() -> new RemoteClientLauncher());
    }
}
