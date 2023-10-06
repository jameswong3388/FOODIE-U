package com.oodwj_assignment.Models;

import com.oodwj_assignment.APIs.Response;

import java.time.LocalDate;
import java.util.UUID;


public class Users {
    private UUID userId;
    private final String username;
    private final String password;
    private final String email;
    private final Integer age;
    private final Role role;
    private final LocalDate updatedAt;
    private final LocalDate createdAt;

    public Users(UUID userId, String username, String password, String email, int age, Role role, LocalDate updatedAt, LocalDate createdAt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.age = age;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and setters for API.User attributes would go here...

    public UUID getUserId() {
        return userId;
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

    public Response<Object> getAttributeValue(String attributeName) {
        switch (attributeName) {
            case "userId" -> {
                return Response.success("User ID", getUserId());
            }
            case "username" -> {
                return Response.success("Username", getUsername());
            }
            case "password" -> {
                return Response.success("Password", getPassword());
            }
            case "email" -> {
                return Response.success("Email", getEmail());
            }
            case "age" -> {
                return Response.success("Age", getAge());
            }
            case "role" -> {
                return Response.success("Role", getRole());
            }
            default -> {
                return Response.failure("Invalid attribute name");
            }
        }
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    // Define an enum for user roles.
    public enum Role {
        Admin,
        Vendor,
        Customer,
        Delivery_Runner
    }

    @Override
    public String toString() {
        return userId + ";" + username + ";" + password + ";" + email + ";" + age + ";" + role.name() + ";" + updatedAt + ";" + createdAt;
    }
}
