package com.oodwj_assignment.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class CustomerProfile extends Model {
    private UUID userId;
    private Gender gender;
    private LocalDate dob;

    public CustomerProfile(UUID profileId, UUID userId, Gender gender, LocalDate dob, LocalDateTime updatedAt, LocalDateTime createdAt) {
        super(profileId, updatedAt, createdAt);
        this.userId = userId;
        this.gender = gender;
        this.dob = dob;
    }

    public UUID getUserId() {
        return userId;
    }
    public Gender getGender() {
        return gender;
    }
    public LocalDate getDob() {
        return dob;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    public void setGender(Gender gender) {
        this.gender = gender;
    }
    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public enum Gender {
        Male,
        Female
    }

    @Override
    public String toString() {
        return getId() + ";" + getUserId() + ";" + getGender() + ";" + getDob() + ";" + getCreatedAt() + ";" + getUpdatedAt();
    }
}
