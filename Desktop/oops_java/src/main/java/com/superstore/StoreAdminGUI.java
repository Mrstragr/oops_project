package com.superstore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * StoreAdminGUI for store admin operations.
 */
public class StoreAdminGUI extends JFrame {
    private Server server;
    private StoreAdmin user;
    private JLabel statusLabel;

    public StoreAdminGUI(Server server, StoreAdmin user) {
        this.server = server;
        this.user = user;
        initializeUI();
        showAlerts();
    }

    private void initializeUI() {
        setTitle("Store Admin Dashboard - " + user.getStoreId());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // Menu Bar
        setJMenuBar(createMenuBar());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180)); // Steel Blue
        JLabel titleLabel = new JLabel("Store Admin Dashboard - " + user.getStoreId(), JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Center Panel with Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));

        // Inventory Tab
        JPanel inventoryPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inventoryPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        inventoryPanel.setBackground(new Color(245, 245, 245));

        JButton manageCategoriesBtn = createStyledButton("Manage Categories", "Add, remove, or update categories, subcategories, and items");
        manageCategoriesBtn.addActionListener(e -> manageCategories());
        inventoryPanel.add(manageCategoriesBtn);

        JButton viewDataBtn = createStyledButton("View Store Data", "View current store inventory and details");
        viewDataBtn.addActionListener(e -> viewData());
        inventoryPanel.add(viewDataBtn);

        JButton checkAlertsBtn = createStyledButton("Check Alerts", "View out of stock alerts");
        checkAlertsBtn.addActionListener(e -> showAlerts());
        inventoryPanel.add(checkAlertsBtn);

        tabbedPane.addTab("Inventory", inventoryPanel);

        // Orders Tab
        JPanel ordersPanel = new JPanel(new FlowLayout());
        ordersPanel.setBackground(new Color(245, 245, 245));

        JButton placeOrderBtn = createStyledButton("Place Order to Warehouse", "Request items from linked warehouse");
        placeOrderBtn.addActionListener(e -> placeOrder());
        ordersPanel.add(placeOrderBtn);

        JButton sendMessageBtn = createStyledButton("Send Message to Warehouse", "Send a message to the warehouse");
        sendMessageBtn.addActionListener(e -> sendMessage());
        ordersPanel.add(sendMessageBtn);

        tabbedPane.addTab("Orders", ordersPanel);

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
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> {
            dispose();
            new LoginGUI(server);
        });
        fileMenu.add(logoutItem);
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
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Store Admin Dashboard\nManage store inventory and orders."));
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

    private void showAlerts() {
        List<Alert> alerts = server.getAlertsForEntity(user.getStoreId());
        if (!alerts.isEmpty()) {
            StringBuilder alertMsg = new StringBuilder("Alerts:\n");
            for (Alert alert : alerts) {
                alertMsg.append(alert.toString()).append("\n");
            }
            JOptionPane.showMessageDialog(this, alertMsg.toString(), "Out of Stock Alerts", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void manageCategories() {
        String[] options = {"Add Category", "Add Subcategory", "Add Item", "Remove Category", "Remove Subcategory", "Remove Item", "Update Item Quantity"};
        String choice = (String) JOptionPane.showInputDialog(this, "Choose action:", "Manage Categories", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice == null) return;

        Store store = server.getStore(user.getStoreId());
        if (store == null) {
            JOptionPane.showMessageDialog(this, "Store not found.");
            return;
        }

        switch (choice) {
            case "Add Category":
                String catId = JOptionPane.showInputDialog("Category ID:");
                String catName = JOptionPane.showInputDialog("Category Name:");
                if (catId != null && catName != null) {
                    Category cat = new Category(catId, catName);
                    store.addCategory(cat);
                    JOptionPane.showMessageDialog(this, "Category added.");
                }
                break;
            case "Add Subcategory":
                String catNameForSub = JOptionPane.showInputDialog("Category Name:");
                String subId = JOptionPane.showInputDialog("Subcategory ID:");
                String subName = JOptionPane.showInputDialog("Subcategory Name:");
                if (catNameForSub != null && subId != null && subName != null) {
                    for (Category c : store.getCategories()) {
                        if (c.getName().equals(catNameForSub)) {
                            Subcategory sub = new Subcategory(subId, subName);
                            c.addSubcategory(sub);
                            JOptionPane.showMessageDialog(this, "Subcategory added.");
                            break;
                        }
                    }
                }
                break;
            case "Add Item":
                String catNameForItem = JOptionPane.showInputDialog("Category Name:");
                String subNameForItem = JOptionPane.showInputDialog("Subcategory Name:");
                String itemId = JOptionPane.showInputDialog("Item ID:");
                String itemName = JOptionPane.showInputDialog("Item Name:");
                int qty = Integer.parseInt(JOptionPane.showInputDialog("Quantity:"));
                double cost = Double.parseDouble(JOptionPane.showInputDialog("Cost:"));
                double fixedCost = Double.parseDouble(JOptionPane.showInputDialog("Fixed Cost per Quarter:"));
                double carryingCost = Double.parseDouble(JOptionPane.showInputDialog("Carrying Cost per Unit per Quarter:"));
                double demand = Double.parseDouble(JOptionPane.showInputDialog("Demand per Quarter:"));
                double maxUsage = Double.parseDouble(JOptionPane.showInputDialog("Max Daily Usage:"));
                double avgUsage = Double.parseDouble(JOptionPane.showInputDialog("Avg Daily Usage:"));
                double maxLead = Double.parseDouble(JOptionPane.showInputDialog("Max Lead Time Days:"));
                double avgLead = Double.parseDouble(JOptionPane.showInputDialog("Avg Lead Time Days:"));
                if (itemId != null && itemName != null) {
                    Item item = new Item(itemId, itemName, qty, cost, fixedCost, carryingCost, demand, maxUsage, avgUsage, maxLead, avgLead);
                    for (Category c : store.getCategories()) {
                        if (c.getName().equals(catNameForItem)) {
                            for (Subcategory s : c.getSubcategories()) {
                                if (s.getName().equals(subNameForItem)) {
                                    s.addItem(item);
                                    JOptionPane.showMessageDialog(this, "Item added.");
                                    server.checkAndGenerateAlerts();
                                    Logger.logInfo("Item added to store: " + itemId);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
                break;
            case "Remove Category":
                String remCatName = JOptionPane.showInputDialog("Category Name:");
                if (remCatName != null) {
                    store.removeCategory(remCatName);
                    JOptionPane.showMessageDialog(this, "Category removed.");
                }
                break;
            case "Remove Subcategory":
                String remCatName2 = JOptionPane.showInputDialog("Category Name:");
                String remSubName = JOptionPane.showInputDialog("Subcategory Name:");
                if (remCatName2 != null && remSubName != null) {
                    for (Category c : store.getCategories()) {
                        if (c.getName().equals(remCatName2)) {
                            c.removeSubcategory(remSubName);
                            JOptionPane.showMessageDialog(this, "Subcategory removed.");
                            break;
                        }
                    }
                }
                break;
            case "Remove Item":
                String remCatName3 = JOptionPane.showInputDialog("Category Name:");
                String remSubName2 = JOptionPane.showInputDialog("Subcategory Name:");
                String remItemId = JOptionPane.showInputDialog("Item ID:");
                if (remCatName3 != null && remSubName2 != null && remItemId != null) {
                    for (Category c : store.getCategories()) {
                        if (c.getName().equals(remCatName3)) {
                            for (Subcategory s : c.getSubcategories()) {
                                if (s.getName().equals(remSubName2)) {
                                    s.removeItem(remItemId);
                                    JOptionPane.showMessageDialog(this, "Item removed.");
                                    server.checkAndGenerateAlerts();
                                    Logger.logInfo("Item removed from store: " + remItemId);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
                break;
            case "Update Item Quantity":
                String updCatName = JOptionPane.showInputDialog("Category Name:");
                String updSubName = JOptionPane.showInputDialog("Subcategory Name:");
                String updItemId = JOptionPane.showInputDialog("Item ID:");
                int newQty = Integer.parseInt(JOptionPane.showInputDialog("New Quantity:"));
                if (updCatName != null && updSubName != null && updItemId != null) {
                    for (Category c : store.getCategories()) {
                        if (c.getName().equals(updCatName)) {
                            for (Subcategory s : c.getSubcategories()) {
                                if (s.getName().equals(updSubName)) {
                                    for (Item i : s.getItems()) {
                                        if (i.getId().equals(updItemId)) {
                                            i.setQuantity(newQty);
                                            JOptionPane.showMessageDialog(this, "Quantity updated.");
                                            server.checkAndGenerateAlerts();
                                            Logger.logInfo("Item quantity updated: " + updItemId + " to " + newQty);
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
                break;
        }
    }

    private void placeOrder() {
        String itemId = JOptionPane.showInputDialog("Item ID:");
        int quantity = Integer.parseInt(JOptionPane.showInputDialog("Quantity:"));
        if (itemId != null) {
            // Create order from store to warehouse
            String orderId = "ORD" + System.currentTimeMillis();
            Order order = new Order(orderId, itemId, quantity, new java.util.Date(), user.getStoreId(), server.getStore(user.getStoreId()).getLinkedWarehouseId());
            server.addOrder(order);
            JOptionPane.showMessageDialog(this, "Order placed to warehouse.");
            Logger.logInfo("Order placed: " + orderId);
        }
    }

    private void viewData() {
        Store store = server.getStore(user.getStoreId());
        if (store != null) {
            JOptionPane.showMessageDialog(this, store.toString());
        } else {
            JOptionPane.showMessageDialog(this, "Store not found.");
        }
    }

    private void sendMessage() {
        String itemName = JOptionPane.showInputDialog("Item Name:");
        String itemCode = JOptionPane.showInputDialog("Item Code:");
        int quantity = Integer.parseInt(JOptionPane.showInputDialog("Quantity:"));
        java.util.Date expectedDate = new java.util.Date(); // Placeholder
        String warehouseId = JOptionPane.showInputDialog("Warehouse ID:");
        Message message = new Message(java.util.UUID.randomUUID().toString(), user.getStoreId(), warehouseId, itemName, itemCode, quantity, expectedDate);
        server.addMessage(message);
        JOptionPane.showMessageDialog(this, "Message sent.");
    }
}
