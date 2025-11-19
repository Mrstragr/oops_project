package com.superstore;

import java.io.Serializable;
import java.util.Date;

/**
 * Message class representing messages sent between stores and warehouses.
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String fromEntityId; // Store ID
    private String toEntityId; // Warehouse ID
    private String itemName;
    private String itemCode;
    private int quantity;
    private Date expectedArrivalDate;
    private Date sentDate;

    public Message(String id, String fromEntityId, String toEntityId, String itemName, String itemCode, int quantity, Date expectedArrivalDate) {
        this.id = id;
        this.fromEntityId = fromEntityId;
        this.toEntityId = toEntityId;
        this.itemName = itemName;
        this.itemCode = itemCode;
        this.quantity = quantity;
        this.expectedArrivalDate = expectedArrivalDate;
        this.sentDate = new Date();
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFromEntityId() { return fromEntityId; }
    public void setFromEntityId(String fromEntityId) { this.fromEntityId = fromEntityId; }

    public String getToEntityId() { return toEntityId; }
    public void setToEntityId(String toEntityId) { this.toEntityId = toEntityId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Date getExpectedArrivalDate() { return expectedArrivalDate; }
    public void setExpectedArrivalDate(Date expectedArrivalDate) { this.expectedArrivalDate = expectedArrivalDate; }

    public Date getSentDate() { return sentDate; }
    public void setSentDate(Date sentDate) { this.sentDate = sentDate; }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", itemName='" + itemName + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
