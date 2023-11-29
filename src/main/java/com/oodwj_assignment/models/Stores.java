package com.oodwj_assignment.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Stores extends Model {
    private String name;
    private UUID vendorId;
    private String description;
    private storeCategory category;

    public Stores(UUID storeId, String name, UUID vendorId,String description,storeCategory category, LocalDateTime updatedAt, LocalDateTime createdAt) {
        super(storeId, updatedAt, createdAt);
        this.name = name;
        this.vendorId = vendorId;
        this.description = description;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public UUID getVendorId() {
        return vendorId;
    }

    public String getDescription() {
        return description;
    }

    public storeCategory getCategory() {
        return category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVendorId(UUID vendorId) {
        this.vendorId = vendorId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(storeCategory category) {
        this.category = category;
    }

    public enum storeCategory {
        Western,
        Chinese,
        Asian,
        Default
    }

    @Override
    public String toString() {
        return getId() + ";" + getName() + ";" + getVendorId() + ";" + getDescription() + ";"+ getCategory() + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }
}
