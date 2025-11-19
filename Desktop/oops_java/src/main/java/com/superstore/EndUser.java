package com.superstore;

/**
 * EndUser class representing end users (buyers) who can browse and search items.
 */
public class EndUser extends User {

    public EndUser(String loginId, String password, String name) {
        super(loginId, password, name, UserType.END_USER);
    }

    @Override
    public void performAction() {
        // Implement end user actions like browsing items
        System.out.println("EndUser performing action: Browsing items.");
    }

    // Methods for browsing, searching items
    public void browseItems() {
        System.out.println("Browsing items.");
    }

    public void searchItems(String query) {
        System.out.println("Searching for items: " + query);
    }
}
