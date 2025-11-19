package com.superstore;

import java.io.Serializable;
import java.util.Date;

/**
 * Order class representing orders placed by stores to warehouses or warehouses to suppliers.
 */
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String itemId;
    private int quantity;
    private Date expectedArrivalDate;
    private String fromEntityId; // Store or Warehouse ID
    private String toEntityId; // Warehouse or Supplier ID
    private OrderStatus status;

    public enum OrderStatus {
        PENDING, SHIPPED, RECEIVED, CANCELLED
    }

    public Order(String id, String itemId, int quantity, Date expectedArrivalDate, String fromEntityId, String toEntityId) {
        this.id = id;
        this.itemId = itemId;
        this.quantity = quantity;
        this.expectedArrivalDate = expectedArrivalDate;
        this.fromEntityId = fromEntityId;
        this.toEntityId = toEntityId;
        this.status = OrderStatus.PENDING;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Date getExpectedArrivalDate() { return expectedArrivalDate; }
    public void setExpectedArrivalDate(Date expectedArrivalDate) { this.expectedArrivalDate = expectedArrivalDate; }

    public String getFromEntityId() { return fromEntityId; }
    public void setFromEntityId(String fromEntityId) { this.fromEntityId = fromEntityId; }

    public String getToEntityId() { return toEntityId; }
    public void setToEntityId(String toEntityId) { this.toEntityId = toEntityId; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", itemId='" + itemId + '\'' +
                ", quantity=" + quantity +
                ", status=" + status +
                '}';
    }
}
