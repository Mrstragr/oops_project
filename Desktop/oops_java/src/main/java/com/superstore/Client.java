package com.superstore;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Client class for remote access to the server.
 * This is a simple console-based client; in full implementation, use RMI or sockets.
 */
public class Client {
    private Server server;
    private User currentUser;

    public Client(Server server) {
        this.server = server;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Superstore Management System");

        while (true) {
            System.out.print("Enter login ID: ");
            String loginId = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            if (server.authenticate(loginId, password)) {
                currentUser = server.getUser(loginId);
                System.out.println("Login successful. Welcome, " + currentUser.getName());
                showMenu();
                break;
            } else {
                System.out.println("Invalid credentials. Try again.");
            }
        }
        scanner.close();
    }

    private void showMenu() {
        switch (currentUser.getUserType()) {
            case SUPER_USER:
                showSuperUserMenu();
                break;
            case WAREHOUSE_ADMIN:
                showWarehouseAdminMenu();
                break;
            case STORE_ADMIN:
                showStoreAdminMenu();
                break;
            case WAREHOUSE_KEEPER:
                showWarehouseKeeperMenu();
                break;
            case STORE_KEEPER:
                showStoreKeeperMenu();
                break;
            case END_USER:
                showEndUserMenu();
                break;
        }
    }

    private void showSuperUserMenu() {
        System.out.println("Super User Menu:");
        System.out.println("1. Create Warehouse");
        System.out.println("2. Create Store");
        System.out.println("3. Create Warehouse Admin");
        // Implement options
    }

    private void showWarehouseAdminMenu() {
        System.out.println("Warehouse Admin Menu:");
        System.out.println("1. Manage Categories");
        System.out.println("2. Handle Orders");
        // Check for alerts
        List<Alert> alerts = server.getAlertsForEntity(((WarehouseAdmin) currentUser).getWarehouseId());
        if (!alerts.isEmpty()) {
            System.out.println("Alerts:");
            for (Alert alert : alerts) {
                System.out.println(alert);
            }
        }
        // Implement options
    }

    private void showStoreAdminMenu() {
        System.out.println("Store Admin Menu:");
        System.out.println("1. Manage Categories");
        System.out.println("2. Place Order");
        // Check for alerts
        List<Alert> alerts = server.getAlertsForEntity(((StoreAdmin) currentUser).getStoreId());
        if (!alerts.isEmpty()) {
            System.out.println("Alerts:");
            for (Alert alert : alerts) {
                System.out.println(alert);
            }
        }
        // Implement options
    }

    private void showWarehouseKeeperMenu() {
        System.out.println("Warehouse Keeper Menu:");
        System.out.println("1. Manage Inventory");
        // Implement options
    }

    private void showStoreKeeperMenu() {
        System.out.println("Store Keeper Menu:");
        System.out.println("1. Manage Inventory");
        // Implement options
    }

    private void showEndUserMenu() {
        System.out.println("End User Menu:");
        System.out.println("1. Browse Items");
        System.out.println("2. Search Items");
        // Implement options
    }
}
