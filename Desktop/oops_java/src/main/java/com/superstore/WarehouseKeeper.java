package com.superstore;

/**
 * WarehouseKeeper class representing warehouse keepers.
 */
public class WarehouseKeeper extends User {
    private String warehouseId;
    private String[] assignedCategories;

    public WarehouseKeeper(String loginId, String password, String name, String warehouseId, String[] assignedCategories) {
        super(loginId, password, name, UserType.WAREHOUSE_KEEPER);
        this.warehouseId = warehouseId;
        this.assignedCategories = assignedCategories;
    }

    public WarehouseKeeper(String loginId, String password, String name, String warehouseId) {
        super(loginId, password, name, UserType.WAREHOUSE_KEEPER);
        this.warehouseId = warehouseId;
        this.assignedCategories = new String[0];
    }

    public String getWarehouseId() { return warehouseId; }
    public void setWarehouseId(String warehouseId) { this.warehouseId = warehouseId; }

    public String[] getAssignedCategories() { return assignedCategories; }
    public void setAssignedCategories(String[] assignedCategories) { this.assignedCategories = assignedCategories; }

    @Override
    public void performAction() {
        // Implement warehouse keeper actions
        System.out.println("WarehouseKeeper performing action for warehouse: " + warehouseId);
    }

    // Methods for managing inventory inwards/outwards for assigned categories
    public void manageInventory() {
        System.out.println("Managing inventory for assigned categories in warehouse: " + warehouseId);
    }
}
