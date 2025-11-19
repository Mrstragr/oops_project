package com.superstore;

/**
 * Utility class for calculating EOQ, Safety Stock, and Reorder Point.
 */
public class InventoryCalculator {

    /**
     * Calculate Economic Order Quantity (EOQ).
     * EOQ = sqrt(2 * D * K / H)
     * @param fixedCostPerQuarter D
     * @param demandPerQuarter K
     * @param carryingCostPerUnitPerQuarter H
     * @return EOQ
     */
    public static double calculateEOQ(double fixedCostPerQuarter, double demandPerQuarter, double carryingCostPerUnitPerQuarter) {
        if (carryingCostPerUnitPerQuarter == 0) return 0;
        return Math.sqrt((2 * fixedCostPerQuarter * demandPerQuarter) / carryingCostPerUnitPerQuarter);
    }

    /**
     * Calculate Safety Stock.
     * Safety Stock = (max daily usage * max lead time) - (avg daily usage * avg lead time)
     * @param maxDailyUsage
     * @param maxLeadTimeDays
     * @param avgDailyUsage
     * @param avgLeadTimeDays
     * @return Safety Stock
     */
    public static double calculateSafetyStock(double maxDailyUsage, double maxLeadTimeDays, double avgDailyUsage, double avgLeadTimeDays) {
        return (maxDailyUsage * maxLeadTimeDays) - (avgDailyUsage * avgLeadTimeDays);
    }

    /**
     * Calculate Reorder Point.
     * Reorder Point = (avg lead time * avg daily usage) + Safety Stock
     * @param avgLeadTimeDays
     * @param avgDailyUsage
     * @param safetyStock
     * @return Reorder Point
     */
    public static double calculateReorderPoint(double avgLeadTimeDays, double avgDailyUsage, double safetyStock) {
        return (avgLeadTimeDays * avgDailyUsage) + safetyStock;
    }
}
