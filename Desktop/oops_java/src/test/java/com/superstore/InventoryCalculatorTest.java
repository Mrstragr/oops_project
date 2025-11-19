package com.superstore;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InventoryCalculatorTest {

    @Test
    public void testCalculateEOQ() {
        double eoq = InventoryCalculator.calculateEOQ(100, 1000, 10);
        assertEquals(141.42, eoq, 0.01);
    }

    @Test
    public void testCalculateSafetyStock() {
        double safetyStock = InventoryCalculator.calculateSafetyStock(10, 5, 8, 4);
        assertEquals(18, safetyStock, 0.01);
    }

    @Test
    public void testCalculateReorderPoint() {
        double reorderPoint = InventoryCalculator.calculateReorderPoint(4, 8, 12);
        assertEquals(44, reorderPoint, 0.01);
    }
}
