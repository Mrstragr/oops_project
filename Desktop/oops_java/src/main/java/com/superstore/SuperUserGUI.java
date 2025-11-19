package com.superstore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * SuperUserGUI for super user operations.
 */
public class SuperUserGUI extends JFrame {
    private Server server;
    private SuperUser user;
    private JLabel statusLabel;

    public SuperUserGUI(Server server, SuperUser user) {
        this.server = server;
        this.user = user;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Super User Dashboard - " + user.getName());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // Menu Bar
        setJMenuBar(createMenuBar());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180)); // Steel Blue
        JLabel titleLabel = new JLabel("Super User Dashboard", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Center Panel with Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));

        // Creation Tab
        JPanel creationPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        creationPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        creationPanel.setBackground(new Color(245, 245, 245));

        JButton createWarehouseBtn = createStyledButton("Create Warehouse", "Create a new warehouse");
        createWarehouseBtn.addActionListener(e -> createWarehouse());
        creationPanel.add(createWarehouseBtn);

        JButton createStoreBtn = createStyledButton("Create Store", "Create a new store linked to a warehouse");
        createStoreBtn.addActionListener(e -> createStore());
        creationPanel.add(createStoreBtn);

        JButton createAdminBtn = createStyledButton("Create Warehouse Admin", "Create a warehouse admin user");
        createAdminBtn.addActionListener(e -> createWarehouseAdmin());
        creationPanel.add(createAdminBtn);

        JButton createStoreAdminBtn = createStyledButton("Create Store Admin", "Create a store admin user");
        createStoreAdminBtn.addActionListener(e -> createStoreAdmin());
        creationPanel.add(createStoreAdminBtn);

        JButton createWarehouseKeeperBtn = createStyledButton("Create Warehouse Keeper", "Create a warehouse keeper user");
        createWarehouseKeeperBtn.addActionListener(e -> createWarehouseKeeper());
        creationPanel.add(createWarehouseKeeperBtn);

        JButton createStoreKeeperBtn = createStyledButton("Create Store Keeper", "Create a store keeper user");
        createStoreKeeperBtn.addActionListener(e -> createStoreKeeper());
        creationPanel.add(createStoreKeeperBtn);

        tabbedPane.addTab("Creation", creationPanel);

        // Management Tab
        JPanel managementPanel = new JPanel(new FlowLayout());
        managementPanel.setBackground(new Color(245, 245, 245));
        JButton viewDataBtn = createStyledButton("View All Data", "View all warehouses and stores");
        viewDataBtn.addActionListener(e -> viewData());
        managementPanel.add(viewDataBtn);

        tabbedPane.addTab("Management", managementPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // Status Bar
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        statusPanel.add(statusLabel, BorderLayout.WEST);
        add(statusPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        // View Menu
        JMenu viewMenu = new JMenu("View");
        JMenuItem refreshItem = new JMenuItem("Refresh");
        refreshItem.addActionListener(e -> statusLabel.setText("Refreshed"));
        viewMenu.add(refreshItem);
        menuBar.add(viewMenu);

        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Superstore Management System v1.0\nDeveloped with Java Swing"));
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);

        return menuBar;
    }

    private JButton createStyledButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(new Color(100, 149, 237)); // Cornflower Blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setToolTipText(tooltip);
        return button;
    }

    private void createWarehouse() {
        String id = JOptionPane.showInputDialog("Warehouse ID:");
        String name = JOptionPane.showInputDialog("Warehouse Name:");
        if (id != null && name != null) {
            user.createWarehouse(id, name);
            Warehouse warehouse = new Warehouse(id, name);
            server.addWarehouse(warehouse);
            JOptionPane.showMessageDialog(this, "Warehouse created.");
        }
    }

    private void createStore() {
        String id = JOptionPane.showInputDialog("Store ID:");
        String name = JOptionPane.showInputDialog("Store Name:");
        String warehouseId = JOptionPane.showInputDialog("Linked Warehouse ID:");
        if (id != null && name != null && warehouseId != null) {
            user.createStore(id, name, warehouseId);
            Store store = new Store(id, name, warehouseId);
            server.addStore(store);
            JOptionPane.showMessageDialog(this, "Store created.");
        }
    }

    private void createWarehouseAdmin() {
        String loginId = JOptionPane.showInputDialog("Admin Login ID:");
        String password = JOptionPane.showInputDialog("Password:");
        String name = JOptionPane.showInputDialog("Name:");
        String warehouseId = JOptionPane.showInputDialog("Warehouse ID:");
        if (loginId != null && password != null && name != null && warehouseId != null) {
            user.createWarehouseAdmin(loginId, password, name, warehouseId);
            WarehouseAdmin admin = new WarehouseAdmin(loginId, password, name, warehouseId);
            server.addUser(admin);
            JOptionPane.showMessageDialog(this, "Warehouse Admin created.");
        }
    }

    private void createStoreAdmin() {
        String loginId = JOptionPane.showInputDialog("Admin Login ID:");
        String password = JOptionPane.showInputDialog("Password:");
        String name = JOptionPane.showInputDialog("Name:");
        String storeId = JOptionPane.showInputDialog("Store ID:");
        if (loginId != null && password != null && name != null && storeId != null) {
            user.createStoreAdmin(loginId, password, name, storeId);
            StoreAdmin admin = new StoreAdmin(loginId, password, name, storeId);
            server.addUser(admin);
            JOptionPane.showMessageDialog(this, "Store Admin created.");
        }
    }

    private void createWarehouseKeeper() {
        String loginId = JOptionPane.showInputDialog("Keeper Login ID:");
        String password = JOptionPane.showInputDialog("Password:");
        String name = JOptionPane.showInputDialog("Name:");
        String warehouseId = JOptionPane.showInputDialog("Warehouse ID:");
        if (loginId != null && password != null && name != null && warehouseId != null) {
            user.createWarehouseKeeper(loginId, password, name, warehouseId);
            WarehouseKeeper keeper = new WarehouseKeeper(loginId, password, name, warehouseId);
            server.addUser(keeper);
            JOptionPane.showMessageDialog(this, "Warehouse Keeper created.");
        }
    }

    private void createStoreKeeper() {
        String loginId = JOptionPane.showInputDialog("Keeper Login ID:");
        String password = JOptionPane.showInputDialog("Password:");
        String name = JOptionPane.showInputDialog("Name:");
        String storeId = JOptionPane.showInputDialog("Store ID:");
        if (loginId != null && password != null && name != null && storeId != null) {
            user.createStoreKeeper(loginId, password, name, storeId);
            StoreKeeper keeper = new StoreKeeper(loginId, password, name, storeId);
            server.addUser(keeper);
            JOptionPane.showMessageDialog(this, "Store Keeper created.");
        }
    }

    private void viewData() {
        StringBuilder data = new StringBuilder();
        data.append("Warehouses:\n");
        for (Warehouse w : server.getWarehouses().values()) {
            data.append(w.toString()).append("\n");
        }
        data.append("\nStores:\n");
        for (Store s : server.getStores().values()) {
            data.append(s.toString()).append("\n");
        }
        JOptionPane.showMessageDialog(this, data.toString());
    }
}
