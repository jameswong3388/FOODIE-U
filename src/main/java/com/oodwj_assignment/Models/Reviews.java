package com.oodwj_assignment.Models;

import java.time.LocalDate;
import java.util.UUID;

public class Reviews {
    private final UUID reviewId;
    private final UUID orderId;
    private final UUID userId;
    private final String reviewContent;
    private final reviewRating reviewRating;
    private final LocalDate updatedAt;
    private final LocalDate createdAt;

    public Reviews(UUID reviewId, UUID orderId, UUID userId, String reviewContent, reviewRating reviewRating, LocalDate updatedAt, LocalDate createdAt) {
        this.reviewId = reviewId;
        this.orderId = orderId;
        this.userId = userId;
        this.reviewContent = reviewContent;
        this.reviewRating = reviewRating;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public UUID getReviewId() {
        return reviewId;
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

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
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
        return reviewId + ";" + orderId + ";" + userId + ";" + reviewContent + ";" + reviewRating + ";" + updatedAt + ";" + createdAt;
    }
}
