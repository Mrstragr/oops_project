package com.superstore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Warehouse class representing a warehouse in the system.
 */
public class Warehouse implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private List<Category> categories;
    private List<String> linkedStoreIds;

    public Warehouse(String id, String name) {
        this.id = id;
        this.name = name;
        this.categories = new ArrayList<>();
        this.linkedStoreIds = new ArrayList<>();
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Category> getCategories() { return categories; }

    public List<String> getLinkedStoreIds() { return linkedStoreIds; }

    // Methods to manage categories and stores
    public void addCategory(Category category) {
        categories.add(category);
    }

    public void removeCategory(String categoryId) {
        categories.removeIf(cat -> cat.getId().equals(categoryId));
    }

    public void linkStore(String storeId) {
        if (!linkedStoreIds.contains(storeId)) {
            linkedStoreIds.add(storeId);
        }
    }

    public void unlinkStore(String storeId) {
        linkedStoreIds.remove(storeId);
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", categories=" + categories.size() +
                ", linkedStores=" + linkedStoreIds.size() +
                '}';
    }
}
