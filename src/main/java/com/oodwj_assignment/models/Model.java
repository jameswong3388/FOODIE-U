package com.oodwj_assignment.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Model implements Serializable {
    private UUID id;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    public Model(UUID id, LocalDateTime updatedAt, LocalDateTime createdAt) {
        this.id = id;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
