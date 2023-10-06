package com.oodwj_assignment.Models;

import com.oodwj_assignment.APIs.Response;

import java.time.LocalDate;
import java.util.UUID;

public class Stores {
    private UUID storeId;
    private final String name;
    private final UUID vendorId;
    private final LocalDate updatedAt;
    private final LocalDate createdAt;

    public Stores(UUID storeId, String name, UUID vendorId, LocalDate updatedAt, LocalDate createdAt) {
        this.storeId = storeId;
        this.name = name;
        this.vendorId = vendorId;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public UUID getStoreId() {
        return storeId;
    }

    public String getName() {
        return name;
    }

    public UUID getVendorId() {
        return vendorId;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public Response<Object> getAttributeValue(String attributeName) {
        return switch (attributeName) {
            case "storeId" -> Response.success("Store ID", getStoreId());
            case "name" -> Response.success("Name", getName());
            case "vendorId" -> Response.success("Vendor ID", getVendorId());
            case "updatedAt" -> Response.success("Updated At", getUpdatedAt());
            case "createdAt" -> Response.success("Created At", getCreatedAt());
            default -> Response.failure("Invalid attribute name");
        };
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }

    public Response<Void> setAttributeValue(String attributeName, Object newValue) {
        switch (attributeName) {
            case "storeId" -> {
                if (newValue instanceof UUID) {
                    setStoreId((UUID) newValue);
                    return Response.success("Store ID updated successfully");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "name" -> {
                if (newValue instanceof String) {
                    return Response.failure("Name cannot be updated");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "vendorId" -> {
                if (newValue instanceof UUID) {
                    return Response.failure("Vendor ID cannot be updated");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "updatedAt" -> {
                if (newValue instanceof LocalDate) {
                    return Response.failure("Updated At cannot be updated");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "createdAt" -> {
                if (newValue instanceof LocalDate) {
                    return Response.failure("Created At cannot be updated");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            default -> {
                return Response.failure("Invalid attribute name");
            }
        }
    }

    @Override
    public String toString() {
        return storeId + ";" + name + ";" + vendorId + ";" + updatedAt + ";" + createdAt;
    }
}
