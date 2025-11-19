package com.superstore;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/**
 * RMI Server Launcher for the Superstore Management System.
 * Starts the RMI registry and binds the server instance.
 */
public class ServerLauncher {

    public static void main(String[] args) {
        try {
            // Start RMI registry on default port 1099
            LocateRegistry.createRegistry(1099);
            System.out.println("RMI Registry started on port 1099");

            // Create and bind server instance
            SuperStoreServer server = new SuperStoreServer();
            Naming.rebind("SuperStoreServer", server);
            System.out.println("Superstore Server bound to RMI registry as 'SuperStoreServer'");
            System.out.println("Server is ready to accept remote connections...");

            // Keep server running
            System.out.println("Press Ctrl+C to stop the server");
            Thread.currentThread().join();

        } catch (Exception e) {
            Logger.logException(e);
            System.err.println("Server startup failed: " + e.getMessage());
            System.exit(1);
        }
    }
}
