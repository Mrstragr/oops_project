package com.superstore;

import java.io.Serializable;

/**
 * Item class representing individual items in the inventory.
 */
public class Item implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String description;
    private double cost;
    private int quantity;

    // For EOQ, Safety Stock, Reorder Point calculations
    private double fixedCostPerQuarter; // D
    private double carryingCostPerUnitPerQuarter; // H
    private double demandPerQuarter; // K
    private double maxDailyUsage;
    private double minDailyUsage;
    private double avgDailyUsage;
    private double maxLeadTimeDays;
    private double avgLeadTimeDays;

    public Item(String id, String name, String description, double cost, int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.quantity = quantity;
    }

    public Item(String id, String name, int quantity, double cost, double fixedCostPerQuarter, double carryingCostPerUnitPerQuarter, double demandPerQuarter, double maxDailyUsage, double avgDailyUsage, double maxLeadTimeDays, double avgLeadTimeDays) {
        this.id = id;
        this.name = name;
        this.description = "";
        this.cost = cost;
        this.quantity = quantity;
        this.fixedCostPerQuarter = fixedCostPerQuarter;
        this.carryingCostPerUnitPerQuarter = carryingCostPerUnitPerQuarter;
        this.demandPerQuarter = demandPerQuarter;
        this.maxDailyUsage = maxDailyUsage;
        this.avgDailyUsage = avgDailyUsage;
        this.maxLeadTimeDays = maxLeadTimeDays;
        this.avgLeadTimeDays = avgLeadTimeDays;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getFixedCostPerQuarter() { return fixedCostPerQuarter; }
    public void setFixedCostPerQuarter(double fixedCostPerQuarter) { this.fixedCostPerQuarter = fixedCostPerQuarter; }

    public double getCarryingCostPerUnitPerQuarter() { return carryingCostPerUnitPerQuarter; }
    public void setCarryingCostPerUnitPerQuarter(double carryingCostPerUnitPerQuarter) { this.carryingCostPerUnitPerQuarter = carryingCostPerUnitPerQuarter; }

    public double getDemandPerQuarter() { return demandPerQuarter; }
    public void setDemandPerQuarter(double demandPerQuarter) { this.demandPerQuarter = demandPerQuarter; }

    public double getMaxDailyUsage() { return maxDailyUsage; }
    public void setMaxDailyUsage(double maxDailyUsage) { this.maxDailyUsage = maxDailyUsage; }

    public double getMinDailyUsage() { return minDailyUsage; }
    public void setMinDailyUsage(double minDailyUsage) { this.minDailyUsage = minDailyUsage; }

    public double getAvgDailyUsage() { return avgDailyUsage; }
    public void setAvgDailyUsage(double avgDailyUsage) { this.avgDailyUsage = avgDailyUsage; }

    public double getMaxLeadTimeDays() { return maxLeadTimeDays; }
    public void setMaxLeadTimeDays(double maxLeadTimeDays) { this.maxLeadTimeDays = maxLeadTimeDays; }

    public double getAvgLeadTimeDays() { return avgLeadTimeDays; }
    public void setAvgLeadTimeDays(double avgLeadTimeDays) { this.avgLeadTimeDays = avgLeadTimeDays; }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
