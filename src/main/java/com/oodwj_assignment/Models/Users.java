package com.oodwj_assignment.Models;

import java.time.LocalDateTime;
import java.util.UUID;


public class Users extends Model {
    private String username;
    private String password;
    private String email;
    private Integer age;
    private Role role;

    public Users(UUID userId, String username, String password, String email, Integer age, Role role, LocalDateTime updatedAt, LocalDateTime createdAt) {
        super(userId, updatedAt, createdAt);
        this.username = username;
        this.password = password;
        this.email = email;
        this.age = age;
        this.role = role;
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public Role getRole() {
        return role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public enum Role {
        Admin,
        Vendor,
        Customer,
        Delivery_Runner
    }

    @Override
    public String toString() {
        return getId() + ";" + getUsername() + ";" + getPassword() + ";" + getEmail() + ";" + getAge() + ";" + getRole() + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }
}
