package com.oodwj_assignment.models;

import java.util.UUID;

public class CustomerProfile {
    private UUID profileId;
    private UUID userId;

    public CustomerProfile(UUID profileId, UUID userId) {
        this.profileId = profileId;
        this.userId = userId;
    }

    public UUID getProfileId() {
        return profileId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setProfileId(UUID profileId) {
        this.profileId = profileId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return getProfileId() + ";" + getUserId();
    }
}
