package com.superstore;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

/**
 * Server class representing the central server for the Superstore Management System.
 * Manages all data and provides access based on user privileges.
 */
public class Server {
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    private Map<String, User> users;
    private Map<String, Warehouse> warehouses;
    private Map<String, Store> stores;
    private List<Order> orders;
    private List<Alert> alerts;
    private List<Message> messages;

    public Server() {
        this.users = new HashMap<>();
        this.warehouses = new HashMap<>();
        this.stores = new HashMap<>();
        this.orders = new ArrayList<>();
        this.alerts = new ArrayList<>();
        this.messages = new ArrayList<>();
        loadData();
    }

    // Load data from files on startup
    private void loadData() {
        try {
            users = FileHandler.loadMap("users.dat");
            warehouses = FileHandler.loadMap("warehouses.dat");
            stores = FileHandler.loadMap("stores.dat");
            orders = FileHandler.loadList("orders.dat");
            alerts = FileHandler.loadList("alerts.dat");
            messages = FileHandler.loadList("messages.dat");
        } catch (IOException | ClassNotFoundException e) {
            com.superstore.Logger.logException(e);
            logger.warning("Failed to load data: " + e.getMessage());
            // Initialize empty if load fails
        }
    }

    // Save data to files
    public void saveData() {
        try {
            FileHandler.saveMap("users.dat", users);
            FileHandler.saveMap("warehouses.dat", warehouses);
            FileHandler.saveMap("stores.dat", stores);
            FileHandler.saveList("orders.dat", orders);
            FileHandler.saveList("alerts.dat", alerts);
            FileHandler.saveList("messages.dat", messages);
        } catch (IOException e) {
            com.superstore.Logger.logException(e);
            logger.severe("Failed to save data: " + e.getMessage());
        }
    }

    // User management
    public void addUser(User user) {
        users.put(user.getLoginId(), user);
    }

    public User getUser(String loginId) {
        return users.get(loginId);
    }

    public boolean authenticate(String loginId, String password) {
        User user = getUser(loginId);
        return user != null && user.getPassword().equals(password);
    }

    // Warehouse management
    public void addWarehouse(Warehouse warehouse) {
        warehouses.put(warehouse.getId(), warehouse);
    }

    public Warehouse getWarehouse(String id) {
        return warehouses.get(id);
    }

    // Store management
    public void addStore(Store store) {
        stores.put(store.getId(), store);
    }

    public Store getStore(String id) {
        return stores.get(id);
    }

    // Order management
    public void addOrder(Order order) {
        orders.add(order);
    }

    // Alert management
    public List<Alert> getAlertsForEntity(String entityId) {
        return alerts.stream()
                .filter(alert -> alert.getEntityId().equals(entityId))
                .sorted()
                .collect(java.util.stream.Collectors.toList());
    }

    public void addAlert(Alert alert) {
        alerts.add(alert);
    }

    // Message management
    public List<Message> getMessagesForWarehouse(String warehouseId) {
        return messages.stream()
                .filter(msg -> msg.getToEntityId().equals(warehouseId))
                .collect(java.util.stream.Collectors.toList());
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    // Generate alerts for low stock
    public void checkAndGenerateAlerts() {
        for (Warehouse warehouse : warehouses.values()) {
            for (Category cat : warehouse.getCategories()) {
                for (Subcategory sub : cat.getSubcategories()) {
                    for (Item item : sub.getItems()) {
                        if (item.getQuantity() <= calculateReorderPoint(item)) {
                            Alert alert = new Alert(
                                UUID.randomUUID().toString(),
                                item.getId(),
                                calculateReorderPoint(item),
                                calculateEOQ(item),
                                warehouse.getId()
                            );
                            addAlert(alert);
                        }
                    }
                }
            }
        }
        // Similar for stores, but reorder point is 0
        for (Store store : stores.values()) {
            for (Category cat : store.getCategories()) {
                for (Subcategory sub : cat.getSubcategories()) {
                    for (Item item : sub.getItems()) {
                        if (item.getQuantity() == 0) {
                            Alert alert = new Alert(
                                UUID.randomUUID().toString(),
                                item.getId(),
                                0,
                                calculateEOQ(item),
                                store.getId()
                            );
                            addAlert(alert);
                        }
                    }
                }
            }
        }
    }

    private double calculateEOQ(Item item) {
        return InventoryCalculator.calculateEOQ(
            item.getFixedCostPerQuarter(),
            item.getDemandPerQuarter(),
            item.getCarryingCostPerUnitPerQuarter()
        );
    }

    private double calculateSafetyStock(Item item) {
        return InventoryCalculator.calculateSafetyStock(
            item.getMaxDailyUsage(),
            item.getMaxLeadTimeDays(),
            item.getAvgDailyUsage(),
            item.getAvgLeadTimeDays()
        );
    }

    private double calculateReorderPoint(Item item) {
        double safetyStock = calculateSafetyStock(item);
        return InventoryCalculator.calculateReorderPoint(
            item.getAvgLeadTimeDays(),
            item.getAvgDailyUsage(),
            safetyStock
        );
    }

    // Getters for collections (with privilege checks in full implementation)
    public Map<String, User> getUsers() { return users; }
    public Map<String, Warehouse> getWarehouses() { return warehouses; }
    public Map<String, Store> getStores() { return stores; }
    public List<Order> getOrders() { return orders; }
    public List<Alert> getAlerts() { return alerts; }
    public List<Message> getMessages() { return messages; }
}
