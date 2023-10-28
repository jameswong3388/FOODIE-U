package com.oodwj_assignment.models;

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

    public void setName(String name) {
        this.name = name;
    }

    public void setVendorId(UUID vendorId) {
        this.vendorId = vendorId;
    }

    @Override
    public String toString() {
        return getId() + ";" + getName() + ";" + getVendorId() + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }
}
