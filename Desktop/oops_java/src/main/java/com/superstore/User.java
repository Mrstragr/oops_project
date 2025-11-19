package com.superstore;

import java.io.Serializable;

/**
 * Abstract base class for all users in the Superstore Management System.
 */
public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String loginId;
    protected String password;
    protected String name;
    protected UserType userType;

    public enum UserType {
        SUPER_USER, WAREHOUSE_ADMIN, STORE_ADMIN, WAREHOUSE_KEEPER, STORE_KEEPER, END_USER
    }

    public User(String loginId, String password, String name, UserType userType) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.userType = userType;
    }

    // Getters and setters
    public String getLoginId() { return loginId; }
    public void setLoginId(String loginId) { this.loginId = loginId; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public UserType getUserType() { return userType; }

    // Abstract methods for role-specific actions
    public abstract void performAction();

    @Override
    public String toString() {
        return "User{" +
                "loginId='" + loginId + '\'' +
                ", name='" + name + '\'' +
                ", userType=" + userType +
                '}';
    }
}
