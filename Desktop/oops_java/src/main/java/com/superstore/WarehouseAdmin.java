package com.superstore;

/**
 * WarehouseAdmin class representing warehouse administrators.
 */
public class WarehouseAdmin extends User {
    private String warehouseId;

    public WarehouseAdmin(String loginId, String password, String name, String warehouseId) {
        super(loginId, password, name, UserType.WAREHOUSE_ADMIN);
        this.warehouseId = warehouseId;
    }

    public String getWarehouseId() { return warehouseId; }
    public void setWarehouseId(String warehouseId) { this.warehouseId = warehouseId; }

    @Override
    public void performAction() {
        // Implement warehouse admin actions
        System.out.println("WarehouseAdmin performing action for warehouse: " + warehouseId);
    }

    // Methods for managing categories, subcategories, inventory, etc.
    public void manageCategories() {
        System.out.println("Managing categories in warehouse: " + warehouseId);
    }

    public void handleOrders() {
        System.out.println("Handling orders for warehouse: " + warehouseId);
    }
}
