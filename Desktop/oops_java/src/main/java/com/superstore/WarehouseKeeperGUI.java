package com.superstore;

import javax.swing.*;
import java.awt.*;

/**
 * WarehouseKeeperGUI for warehouse keeper operations.
 */
public class WarehouseKeeperGUI extends JFrame {
    private Server server;
    private WarehouseKeeper user;

    public WarehouseKeeperGUI(Server server, WarehouseKeeper user) {
        this.server = server;
        this.user = user;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Warehouse Keeper Dashboard - " + user.getWarehouseId());
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton manageInventoryBtn = new JButton("Manage Inventory");
        manageInventoryBtn.addActionListener(e -> manageInventory());
        panel.add(manageInventoryBtn);

        JButton viewDataBtn = new JButton("View Warehouse Data");
        viewDataBtn.addActionListener(e -> viewData());
        panel.add(viewDataBtn);

        add(panel);
        setVisible(true);
    }

    private void manageInventory() {
        JOptionPane.showMessageDialog(this, "Inventory management not implemented yet.");
    }

    private void viewData() {
        Warehouse warehouse = server.getWarehouse(user.getWarehouseId());
        if (warehouse != null) {
            JOptionPane.showMessageDialog(this, warehouse.toString());
        } else {
            JOptionPane.showMessageDialog(this, "Warehouse not found.");
        }
    }
}
