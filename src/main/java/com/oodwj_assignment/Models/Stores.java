package com.oodwj_assignment.Models;

import com.oodwj_assignment.APIs.Response;
import com.oodwj_assignment.Helpers.Model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Stores extends Model {
    private String name;
    private UUID vendorId;

    public Stores(UUID storeId, String name, UUID vendorId, LocalDateTime updatedAt, LocalDateTime createdAt) {
        super(storeId, updatedAt, createdAt);
        this.name = name;
        this.vendorId = vendorId;
    }

    public String getName() {
        return name;
    }

    public UUID getVendorId() {
        return vendorId;
    }

    public Response<Object> getAttributeValue(String attributeName) {
        return switch (attributeName) {
            case "storeId" -> Response.success("Store ID", getId());
            case "name" -> Response.success("Name", getName());
            case "vendorId" -> Response.success("Vendor ID", getVendorId());
            case "updatedAt" -> Response.success("Updated At", getUpdatedAt());
            case "createdAt" -> Response.success("Created At", getCreatedAt());
            default -> Response.failure("Invalid attribute name");
        };
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVendorId(UUID vendorId) {
        this.vendorId = vendorId;
    }

    public Response<Void> setAttributeValue(String attributeName, Object newValue) {
        switch (attributeName) {
            case "storeId" -> {
                if (newValue instanceof UUID) {
                    setId((UUID) newValue);
                    return Response.success("Store ID updated successfully");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "name" -> {
                if (newValue instanceof String) {
                    setName((String) newValue);
                    return Response.success("Name updated successfully");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "vendorId" -> {
                if (newValue instanceof UUID) {
                    setVendorId((UUID) newValue);
                    return Response.success("Vendor ID updated successfully");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "updatedAt" -> {
                if (newValue instanceof LocalDateTime) {
                    setUpdatedAt((LocalDateTime) newValue);
                    return Response.success("Updated At updated successfully");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "createdAt" -> {
                if (newValue instanceof LocalDateTime) {
                    setCreatedAt((LocalDateTime) newValue);
                    return Response.success("Created At updated successfully");
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
        return getId() + ";" + name + ";" + vendorId + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }
}
