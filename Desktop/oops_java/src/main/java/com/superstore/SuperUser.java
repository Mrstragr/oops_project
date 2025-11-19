package com.superstore;

/**
 * SuperUser class representing the super user who can create warehouses, stores, and warehouse admins.
 */
public class SuperUser extends User {

    public SuperUser(String loginId, String password, String name) {
        super(loginId, password, name, UserType.SUPER_USER);
    }

    @Override
    public void performAction() {
        // Implement super user actions like creating warehouses, stores, etc.
        System.out.println("SuperUser performing action: Managing system-wide entities.");
    }

    // Methods specific to SuperUser
    public void createWarehouse(String id, String name) {
        // Logic to create warehouse
        System.out.println("Creating warehouse: " + name);
    }

    public void createStore(String id, String name, String linkedWarehouseId) {
        // Logic to create store linked to warehouse
        System.out.println("Creating store: " + name + " linked to warehouse: " + linkedWarehouseId);
    }

    public void createWarehouseAdmin(String loginId, String password, String name, String warehouseId) {
        // Logic to create warehouse admin
        System.out.println("Creating warehouse admin: " + name + " for warehouse: " + warehouseId);
    }

    public void createStoreAdmin(String loginId, String password, String name, String storeId) {
        // Logic to create store admin
        System.out.println("Creating store admin: " + name + " for store: " + storeId);
    }

    public void createWarehouseKeeper(String loginId, String password, String name, String warehouseId) {
        // Logic to create warehouse keeper
        System.out.println("Creating warehouse keeper: " + name + " for warehouse: " + warehouseId);
    }

    public void createStoreKeeper(String loginId, String password, String name, String storeId) {
        // Logic to create store keeper
        System.out.println("Creating store keeper: " + name + " for store: " + storeId);
    }
}
