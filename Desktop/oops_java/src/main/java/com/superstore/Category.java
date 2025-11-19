package com.superstore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Category class representing categories like grocery, apparels, etc.
 */
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private List<Subcategory> subcategories;

    public Category(String id, String name) {
        this.id = id;
        this.name = name;
        this.subcategories = new ArrayList<>();
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Subcategory> getSubcategories() { return subcategories; }

    // Methods to manage subcategories
    public void addSubcategory(Subcategory subcategory) {
        subcategories.add(subcategory);
    }

    public void removeSubcategory(String subcategoryId) {
        subcategories.removeIf(sub -> sub.getId().equals(subcategoryId));
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", subcategories=" + subcategories.size() +
                '}';
    }
}
