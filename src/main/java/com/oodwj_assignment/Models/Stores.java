package com.oodwj_assignment.Models;

import java.time.LocalDate;
import java.util.UUID;

public class Stores {
    private final UUID storeId;
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

    @Override
    public String toString() {
        return storeId + ";" + name + ";" + vendorId + ";" + updatedAt + ";" + createdAt;
    }
}
