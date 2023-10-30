package com.oodwj_assignment.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Reviews extends Model {
    private String model;
    private UUID modelUUID;
    private UUID userId;
    private String reviewContent;
    private reviewRating reviewRating;

    public Reviews(UUID reviewId, String model,UUID modelUUID, UUID userId, String reviewContent, reviewRating reviewRating, LocalDateTime updatedAt, LocalDateTime createdAt) {
        super(reviewId, updatedAt, createdAt);
        this.model = model;
        this.modelUUID = modelUUID;
        this.userId = userId;
        this.reviewContent = reviewContent;
        this.reviewRating = reviewRating;
    }

    public String getModel() {
        return model;
    }

    public UUID getModelUUID() {
        return modelUUID;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public reviewRating getReviewRating() {
        return reviewRating;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setModelUUID(UUID modelUUID) {
        this.modelUUID = modelUUID;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public void setReviewRating(reviewRating reviewRating) {
        this.reviewRating = reviewRating;
    }

    public enum reviewRating {
        One,
        Two,
        Three,
        Four,
        Five
    }

    @Override
    public String toString() {
        return getId() + ";" + getModel() + ";" + getModelUUID() + ";" + getUserId() + ";" + getReviewContent() + ";" + getReviewRating() + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }
}
