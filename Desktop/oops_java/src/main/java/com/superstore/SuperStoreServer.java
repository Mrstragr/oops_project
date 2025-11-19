package com.superstore;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

/**
 * RMI Server implementation for the Superstore Management System.
 * Wraps the existing Server class to provide remote access.
 */
public class SuperStoreServer extends UnicastRemoteObject implements SuperStoreRemote {
    
    private Server localServer;
    
    protected SuperStoreServer() throws RemoteException {
        super();
        this.localServer = new Server();
    }
    
    @Override
    public boolean authenticate(String loginId, String password) throws RemoteException {
        return localServer.authenticate(loginId, password);
    }
    
    @Override
    public User getUser(String loginId) throws RemoteException {
        return localServer.getUser(loginId);
    }
    
    @Override
    public void addUser(User user) throws RemoteException {
        localServer.addUser(user);
    }
    
    @Override
    public void addWarehouse(Warehouse warehouse) throws RemoteException {
        localServer.addWarehouse(warehouse);
    }
    
    @Override
    public Warehouse getWarehouse(String id) throws RemoteException {
        return localServer.getWarehouse(id);
    }
    
    @Override
    public Map<String, Warehouse> getWarehouses() throws RemoteException {
        return localServer.getWarehouses();
    }
    
    @Override
    public void addStore(Store store) throws RemoteException {
        localServer.addStore(store);
    }
    
    @Override
    public Store getStore(String id) throws RemoteException {
        return localServer.getStore(id);
    }
    
    @Override
    public Map<String, Store> getStores() throws RemoteException {
        return localServer.getStores();
    }
    
    @Override
    public void addOrder(Order order) throws RemoteException {
        localServer.addOrder(order);
    }
    
    @Override
    public List<Order> getOrders() throws RemoteException {
        return localServer.getOrders();
    }
    
    @Override
    public List<Alert> getAlertsForEntity(String entityId) throws RemoteException {
        return localServer.getAlertsForEntity(entityId);
    }
    
    @Override
    public void addAlert(Alert alert) throws RemoteException {
        localServer.addAlert(alert);
    }
    
    @Override
    public List<Alert> getAlerts() throws RemoteException {
        return localServer.getAlerts();
    }
    
    @Override
    public List<Message> getMessagesForWarehouse(String warehouseId) throws RemoteException {
        return localServer.getMessagesForWarehouse(warehouseId);
    }
    
    @Override
    public void addMessage(Message message) throws RemoteException {
        localServer.addMessage(message);
    }
    
    @Override
    public List<Message> getMessages() throws RemoteException {
        return localServer.getMessages();
    }
    
    @Override
    public void checkAndGenerateAlerts() throws RemoteException {
        localServer.checkAndGenerateAlerts();
    }
    
    @Override
    public void saveData() throws RemoteException {
        localServer.saveData();
    }
}
