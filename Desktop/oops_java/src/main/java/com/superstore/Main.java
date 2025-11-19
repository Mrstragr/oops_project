package com.superstore;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main class to start the Superstore Management System with Swing GUI.
 */
public class Main {
    private static Server server;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
                e.printStackTrace();
            }

            server = new Server();

            // Initialize super user if not exists
            if (server.getUser("super") == null) {
                SuperUser superUser = new SuperUser("super", "password", "Super User");
                server.addUser(superUser);
            }

            // Initialize sample data
            initializeSampleData();

            // Start GUI login
            new LoginGUI(server);
        });
    }

    private static void initializeSampleData() {
        // Create sample warehouse
        Warehouse warehouse = new Warehouse("WH001", "Main Warehouse");
        Category electronics = new Category("CAT001", "Electronics");
        Subcategory laptops = new Subcategory("SUB001", "Laptops");
        Item laptop = new Item("ITEM001", "Dell Laptop", 50, 100.0, 10.0, 5.0, 20.0, 10.0, 5.0, 15.0, 8.0);
        laptops.addItem(laptop);
        electronics.addSubcategory(laptops);
        warehouse.addCategory(electronics);
        server.addWarehouse(warehouse);

        // Create sample store linked to warehouse
        Store store = new Store("ST001", "Downtown Store", "WH001");
        Category electronicsStore = new Category("CAT001", "Electronics");
        Subcategory laptopsStore = new Subcategory("SUB001", "Laptops");
        Item laptopStore = new Item("ITEM001", "Dell Laptop", 10, 100.0, 10.0, 5.0, 20.0, 10.0, 5.0, 15.0, 8.0);
        laptopsStore.addItem(laptopStore);
        electronicsStore.addSubcategory(laptopsStore);
        store.addCategory(electronicsStore);
        server.addStore(store);

        // Create sample users
        WarehouseAdmin whAdmin = new WarehouseAdmin("whadmin", "pass", "Warehouse Admin", "WH001");
        server.addUser(whAdmin);
        StoreAdmin stAdmin = new StoreAdmin("stadmin", "pass", "Store Admin", "ST001");
        server.addUser(stAdmin);
        WarehouseKeeper whKeeper = new WarehouseKeeper("whkeeper", "pass", "Warehouse Keeper", "WH001");
        server.addUser(whKeeper);
        StoreKeeper stKeeper = new StoreKeeper("stkeeper", "pass", "Store Keeper", "ST001");
        server.addUser(stKeeper);
        EndUser endUser = new EndUser("enduser", "pass", "End User");
        server.addUser(endUser);

        // Generate initial alerts
        server.checkAndGenerateAlerts();
    }
}
