package com.superstore;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/**
 * Remote interface for the Superstore Management System.
 * Defines methods that can be called remotely via RMI.
 */
public interface SuperStoreRemote extends Remote {
    
    // User management
    boolean authenticate(String loginId, String password) throws RemoteException;
    User getUser(String loginId) throws RemoteException;
    void addUser(User user) throws RemoteException;
    
    // Warehouse management
    void addWarehouse(Warehouse warehouse) throws RemoteException;
    Warehouse getWarehouse(String id) throws RemoteException;
    Map<String, Warehouse> getWarehouses() throws RemoteException;
    
    // Store management
    void addStore(Store store) throws RemoteException;
    Store getStore(String id) throws RemoteException;
    Map<String, Store> getStores() throws RemoteException;
    
    // Order management
    void addOrder(Order order) throws RemoteException;
    List<Order> getOrders() throws RemoteException;
    
    // Alert management
    List<Alert> getAlertsForEntity(String entityId) throws RemoteException;
    void addAlert(Alert alert) throws RemoteException;
    List<Alert> getAlerts() throws RemoteException;
    
    // Message management
    List<Message> getMessagesForWarehouse(String warehouseId) throws RemoteException;
    void addMessage(Message message) throws RemoteException;
    List<Message> getMessages() throws RemoteException;
    
    // Inventory operations
    void checkAndGenerateAlerts() throws RemoteException;
    
    // Save data
    void saveData() throws RemoteException;
}
