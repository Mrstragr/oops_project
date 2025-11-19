package com.superstore;

/**
 * StoreAdmin class representing store administrators.
 */
public class StoreAdmin extends User {
    private String storeId;

    public StoreAdmin(String loginId, String password, String name, String storeId) {
        super(loginId, password, name, UserType.STORE_ADMIN);
        this.storeId = storeId;
    }

    public String getStoreId() { return storeId; }
    public void setStoreId(String storeId) { this.storeId = storeId; }

    @Override
    public void performAction() {
        // Implement store admin actions
        System.out.println("StoreAdmin performing action for store: " + storeId);
    }

    // Methods for managing categories, subcategories, inventory, placing orders
    public void manageCategories() {
        System.out.println("Managing categories in store: " + storeId);
    }

    public void placeOrder() {
        System.out.println("Placing order from store: " + storeId);
    }
}
