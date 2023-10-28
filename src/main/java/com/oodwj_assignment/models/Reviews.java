package com.oodwj_assignment.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Reviews extends Model {
    private UUID orderId;
    private UUID userId;
    private String reviewContent;
    private reviewRating reviewRating;

    public Reviews(UUID reviewId, UUID orderId, UUID userId, String reviewContent, reviewRating reviewRating, LocalDateTime updatedAt, LocalDateTime createdAt) {
        super(reviewId, updatedAt, createdAt);
        this.orderId = orderId;
        this.userId = userId;
        this.reviewContent = reviewContent;
        this.reviewRating = reviewRating;
    }

    public UUID getOrderId() {
        return orderId;
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

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
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
        return getId() + ";" + getOrderId() + ";" + getUserId() + ";" + getReviewContent() + ";" + getReviewRating() + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }
}
