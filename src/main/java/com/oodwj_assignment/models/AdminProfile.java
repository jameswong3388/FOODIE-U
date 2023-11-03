package com.oodwj_assignment.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class AdminProfile extends Model{
    private UUID userId;

    public AdminProfile(UUID profileId, UUID userId, LocalDateTime updatedAt, LocalDateTime createdAt) {
        super(profileId, updatedAt, createdAt);
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return getId() + ";" + getUserId() + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }
}
