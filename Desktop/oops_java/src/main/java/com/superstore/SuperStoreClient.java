package com.superstore;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/**
 * RMI Client for remote access to the Superstore Management System.
 * Connects to the RMI server and provides GUI-based remote access.
 */
public class SuperStoreClient {
    
    private SuperStoreRemote remoteServer;
    private User currentUser;
    
    public SuperStoreClient(String serverHost) throws Exception {
        // Connect to RMI server
        String url = "rmi://" + serverHost + "/SuperStoreServer";
        remoteServer = (SuperStoreRemote) Naming.lookup(url);
        System.out.println("Connected to Superstore Server at " + serverHost);
    }
    
    public boolean authenticate(String loginId, String password) throws RemoteException {
        boolean result = remoteServer.authenticate(loginId, password);
        if (result) {
            currentUser = remoteServer.getUser(loginId);
        }
        return result;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    // Delegate all server methods to remote server
    public void addUser(User user) throws RemoteException {
        remoteServer.addUser(user);
    }
    
    public void addWarehouse(Warehouse warehouse) throws RemoteException {
        remoteServer.addWarehouse(warehouse);
    }
    
    public Warehouse getWarehouse(String id) throws RemoteException {
        return remoteServer.getWarehouse(id);
    }
    
    public Map<String, Warehouse> getWarehouses() throws RemoteException {
        return remoteServer.getWarehouses();
    }
    
    public void addStore(Store store) throws RemoteException {
        remoteServer.addStore(store);
    }
    
    public Store getStore(String id) throws RemoteException {
        return remoteServer.getStore(id);
    }
    
    public Map<String, Store> getStores() throws RemoteException {
        return remoteServer.getStores();
    }
    
    public void addOrder(Order order) throws RemoteException {
        remoteServer.addOrder(order);
    }
    
    public List<Order> getOrders() throws RemoteException {
        return remoteServer.getOrders();
    }
    
    public List<Alert> getAlertsForEntity(String entityId) throws RemoteException {
        return remoteServer.getAlertsForEntity(entityId);
    }
    
    public void addAlert(Alert alert) throws RemoteException {
        remoteServer.addAlert(alert);
    }
    
    public List<Alert> getAlerts() throws RemoteException {
        return remoteServer.getAlerts();
    }
    
    public List<Message> getMessagesForWarehouse(String warehouseId) throws RemoteException {
        return remoteServer.getMessagesForWarehouse(warehouseId);
    }
    
    public void addMessage(Message message) throws RemoteException {
        remoteServer.addMessage(message);
    }
    
    public List<Message> getMessages() throws RemoteException {
        return remoteServer.getMessages();
    }
    
    public void checkAndGenerateAlerts() throws RemoteException {
        remoteServer.checkAndGenerateAlerts();
    }
    
    public void saveData() throws RemoteException {
        remoteServer.saveData();
    }
    
    // Launch appropriate GUI based on user type
    public void launchGUI() {
        if (currentUser == null) {
            System.err.println("No user authenticated. Cannot launch GUI.");
            return;
        }
        
        // Create a client wrapper that implements Server interface for GUI compatibility
        Server clientWrapper = new ClientServerWrapper(this);
        
        switch (currentUser.getUserType()) {
            case SUPER_USER:
                new SuperUserGUI(clientWrapper, (SuperUser) currentUser);
                break;
            case WAREHOUSE_ADMIN:
                new WarehouseAdminGUI(clientWrapper, (WarehouseAdmin) currentUser);
                break;
            case STORE_ADMIN:
                new StoreAdminGUI(clientWrapper, (StoreAdmin) currentUser);
                break;
            case WAREHOUSE_KEEPER:
                new WarehouseKeeperGUI(clientWrapper, (WarehouseKeeper) currentUser);
                break;
            case STORE_KEEPER:
                new StoreKeeperGUI(clientWrapper, (StoreKeeper) currentUser);
                break;
            case END_USER:
                new EndUserGUI(clientWrapper, (EndUser) currentUser);
                break;
        }
    }
    
    /**
     * Wrapper class to make SuperStoreClient compatible with Server interface
     * Used by GUI classes that expect a Server instance
     */
    private static class ClientServerWrapper extends Server {
        private SuperStoreClient client;
        
        public ClientServerWrapper(SuperStoreClient client) {
            this.client = client;
        }
        
        @Override
        public boolean authenticate(String loginId, String password) {
            try {
                return client.authenticate(loginId, password);
            } catch (RemoteException e) {
                Logger.logException(e);
                return false;
            }
        }
        
        @Override
        public User getUser(String loginId) {
            try {
                return client.remoteServer.getUser(loginId);
            } catch (RemoteException e) {
                Logger.logException(e);
                return null;
            }
        }
        
        @Override
        public void addUser(User user) {
            try {
                client.addUser(user);
            } catch (RemoteException e) {
                Logger.logException(e);
            }
        }
        
        @Override
        public void addWarehouse(Warehouse warehouse) {
            try {
                client.addWarehouse(warehouse);
            } catch (RemoteException e) {
                Logger.logException(e);
            }
        }
        
        @Override
        public Warehouse getWarehouse(String id) {
            try {
                return client.getWarehouse(id);
            } catch (RemoteException e) {
                Logger.logException(e);
                return null;
            }
        }
        
        @Override
        public Map<String, Warehouse> getWarehouses() {
            try {
                return client.getWarehouses();
            } catch (RemoteException e) {
                Logger.logException(e);
                return new java.util.HashMap<>();
            }
        }
        
        @Override
        public void addStore(Store store) {
            try {
                client.addStore(store);
            } catch (RemoteException e) {
                Logger.logException(e);
            }
        }
        
        @Override
        public Store getStore(String id) {
            try {
                return client.getStore(id);
            } catch (RemoteException e) {
                Logger.logException(e);
                return null;
            }
        }
        
        @Override
        public Map<String, Store> getStores() {
            try {
                return client.getStores();
            } catch (RemoteException e) {
                Logger.logException(e);
                return new java.util.HashMap<>();
            }
        }
        
        @Override
        public void addOrder(Order order) {
            try {
                client.addOrder(order);
            } catch (RemoteException e) {
                Logger.logException(e);
            }
        }
        
        @Override
        public List<Order> getOrders() {
            try {
                return client.getOrders();
            } catch (RemoteException e) {
                Logger.logException(e);
                return new java.util.ArrayList<>();
            }
        }
        
        @Override
        public List<Alert> getAlertsForEntity(String entityId) {
            try {
                return client.getAlertsForEntity(entityId);
            } catch (RemoteException e) {
                Logger.logException(e);
                return new java.util.ArrayList<>();
            }
        }
        
        @Override
        public void addAlert(Alert alert) {
            try {
                client.addAlert(alert);
            } catch (RemoteException e) {
                Logger.logException(e);
            }
        }
        
        @Override
        public List<Alert> getAlerts() {
            try {
                return client.getAlerts();
            } catch (RemoteException e) {
                Logger.logException(e);
                return new java.util.ArrayList<>();
            }
        }
        
        @Override
        public List<Message> getMessagesForWarehouse(String warehouseId) {
            try {
                return client.getMessagesForWarehouse(warehouseId);
            } catch (RemoteException e) {
                Logger.logException(e);
                return new java.util.ArrayList<>();
            }
        }
        
        @Override
        public void addMessage(Message message) {
            try {
                client.addMessage(message);
            } catch (RemoteException e) {
                Logger.logException(e);
            }
        }
        
        @Override
        public List<Message> getMessages() {
            try {
                return client.getMessages();
            } catch (RemoteException e) {
                Logger.logException(e);
                return new java.util.ArrayList<>();
            }
        }
        
        @Override
        public void checkAndGenerateAlerts() {
            try {
                client.checkAndGenerateAlerts();
            } catch (RemoteException e) {
                Logger.logException(e);
            }
        }
        
        @Override
        public void saveData() {
            try {
                client.saveData();
            } catch (RemoteException e) {
                Logger.logException(e);
            }
        }
    }
}
