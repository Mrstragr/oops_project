package com.superstore;

import java.io.Serializable;
import java.util.Date;

/**
 * Alert class representing reorder alerts for items.
 */
public class Alert implements Serializable, Comparable<Alert> {
    private static final long serialVersionUID = 1L;

    private String id;
    private String itemId;
    private double reorderPoint;
    private double eoq;
    private Date alertDate;
    private String entityId; // Warehouse or Store ID

    public Alert(String id, String itemId, double reorderPoint, double eoq, String entityId) {
        this.id = id;
        this.itemId = itemId;
        this.reorderPoint = reorderPoint;
        this.eoq = eoq;
        this.entityId = entityId;
        this.alertDate = new Date();
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public double getReorderPoint() { return reorderPoint; }
    public void setReorderPoint(double reorderPoint) { this.reorderPoint = reorderPoint; }

    public double getEoq() { return eoq; }
    public void setEoq(double eoq) { this.eoq = eoq; }

    public Date getAlertDate() { return alertDate; }
    public void setAlertDate(Date alertDate) { this.alertDate = alertDate; }

    public String getEntityId() { return entityId; }
    public void setEntityId(String entityId) { this.entityId = entityId; }

    @Override
    public int compareTo(Alert other) {
        // Sort by reorder point ascending (0 first)
        return Double.compare(this.reorderPoint, other.reorderPoint);
    }

    @Override
    public String toString() {
        return "Alert{" +
                "id='" + id + '\'' +
                ", itemId='" + itemId + '\'' +
                ", reorderPoint=" + reorderPoint +
                ", eoq=" + eoq +
                '}';
    }
}
