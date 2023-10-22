package com.oodwj_assignment.Models;

import com.oodwj_assignment.APIs.Response;
import com.oodwj_assignment.Helpers.Model;

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

    public Response<Object> getAttributeValue(String attributeName) {
        switch (attributeName) {
            case "userId" -> {
                return Response.success("User ID", getId());
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
            case "updatedAt" -> {
                return Response.success("Updated at", getUpdatedAt());
            }
            case "createdAt" -> {
                return Response.success("Created at", getCreatedAt());
            }
            default -> {
                return Response.failure("Invalid attribute name");
            }
        }
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

    public Response<Void> setAttributeValue(String attributeName, Object newValue) {
        switch (attributeName) {
            case "userId" -> {
                if (newValue instanceof UUID) {
                    setId((UUID) newValue);
                    return Response.success("User ID updated successfully");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "username" -> {
                if (newValue instanceof String) {
                    setUsername((String) newValue);
                    return Response.success("Username updated successfully");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "password" -> {
                if (newValue instanceof String) {
                    setPassword((String) newValue);
                    return Response.success("Password updated successfully");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "email" -> {
                if (newValue instanceof String) {
                    setEmail((String) newValue);
                    return Response.success("Email updated successfully");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "age" -> {
                if (newValue instanceof Integer) {
                    setAge((Integer) newValue);
                    return Response.success("Age updated successfully");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "role" -> {
                if (newValue instanceof Role) {
                    setRole((Role) newValue);
                    return Response.success("Role updated successfully");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "updatedAt" -> {
                if (newValue instanceof LocalDateTime) {
                    setUpdatedAt((LocalDateTime) newValue);
                    return Response.success("Updated at updated successfully");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "createdAt" -> {
                if (newValue instanceof LocalDateTime) {
                    setCreatedAt((LocalDateTime) newValue);
                    return Response.success("Created at updated successfully");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            default -> {
                return Response.failure("Invalid attribute name");
            }
        }
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
        return getId() + ";" + username + ";" + password + ";" + email + ";" + age + ";" + role.name() + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }
}
