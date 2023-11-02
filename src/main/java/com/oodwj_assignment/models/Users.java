package com.oodwj_assignment.models;

import java.time.LocalDateTime;
import java.util.UUID;


public class Users extends Model {
    private String username;
    private String password;
    private Role role;
    private String name;
    private String phoneNumber;
    private String email;
    private AccountStatus accountStatus;

    public Users(UUID userId, String username, String password, Role role, String name, String phoneNumber, String email, AccountStatus accountStatus, LocalDateTime updatedAt, LocalDateTime createdAt) {
        super(userId, updatedAt, createdAt);
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.accountStatus = accountStatus;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public enum Role {
        Admin,
        Vendor,
        Customer,
        Delivery_Runner
    }

    public enum AccountStatus {
        Approved,
        Pending,
    }

    @Override
    public String toString() {
        return getId() + ";" + getUsername() + ";" + getPassword() + ";" + getRole() + ";" + getName() + ";" + getPhoneNumber() + ";" + getEmail() + ";" + getAccountStatus() + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }
}
