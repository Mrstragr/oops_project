package com.superstore;

/**
 * StoreKeeper class representing store keepers.
 */
public class StoreKeeper extends User {
    private String storeId;
    private String[] assignedCategories;

    public StoreKeeper(String loginId, String password, String name, String storeId, String[] assignedCategories) {
        super(loginId, password, name, UserType.STORE_KEEPER);
        this.storeId = storeId;
        this.assignedCategories = assignedCategories;
    }

    public StoreKeeper(String loginId, String password, String name, String storeId) {
        super(loginId, password, name, UserType.STORE_KEEPER);
        this.storeId = storeId;
        this.assignedCategories = new String[0];
    }

    public String getStoreId() { return storeId; }
    public void setStoreId(String storeId) { this.storeId = storeId; }

    public String[] getAssignedCategories() { return assignedCategories; }
    public void setAssignedCategories(String[] assignedCategories) { this.assignedCategories = assignedCategories; }

    @Override
    public void performAction() {
        // Implement store keeper actions
        System.out.println("StoreKeeper performing action for store: " + storeId);
    }

    // Methods for managing inventory inwards/outwards for assigned categories
    public void manageInventory() {
        System.out.println("Managing inventory for assigned categories in store: " + storeId);
    }
}
