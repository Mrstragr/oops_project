package com.superstore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Store class representing a store in the system.
 */
public class Store implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String linkedWarehouseId;
    private List<Category> categories;

    public Store(String id, String name, String linkedWarehouseId) {
        this.id = id;
        this.name = name;
        this.linkedWarehouseId = linkedWarehouseId;
        this.categories = new ArrayList<>();
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLinkedWarehouseId() { return linkedWarehouseId; }
    public void setLinkedWarehouseId(String linkedWarehouseId) { this.linkedWarehouseId = linkedWarehouseId; }

    public List<Category> getCategories() { return categories; }

    // Methods to manage categories
    public void addCategory(Category category) {
        categories.add(category);
    }

    public void removeCategory(String categoryId) {
        categories.removeIf(cat -> cat.getId().equals(categoryId));
    }

    @Override
    public String toString() {
        return "Store{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", linkedWarehouse='" + linkedWarehouseId + '\'' +
                ", categories=" + categories.size() +
                '}';
    }
}
