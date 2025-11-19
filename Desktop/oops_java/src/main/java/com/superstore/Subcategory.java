package com.superstore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Subcategory class representing subcategories like beverages, pulses, etc.
 */
public class Subcategory implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private List<Item> items;

    public Subcategory(String id, String name) {
        this.id = id;
        this.name = name;
        this.items = new ArrayList<>();
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Item> getItems() { return items; }

    // Methods to manage items
    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(String itemId) {
        items.removeIf(item -> item.getId().equals(itemId));
    }

    @Override
    public String toString() {
        return "Subcategory{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", items=" + items.size() +
                '}';
    }
}
