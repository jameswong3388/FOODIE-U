package com.oodwj_assignment.Models;

import com.oodwj_assignment.APIs.Response;

import java.time.LocalDate;
import java.util.UUID;

public class Reviews {
    private UUID reviewId;
    private UUID orderId;
    private UUID userId;
    private String reviewContent;
    private reviewRating reviewRating;
    private LocalDate updatedAt;
    private LocalDate createdAt;

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

    public Response<Object> getAttributeValue(String attribute) {
        return switch (attribute) {
            case "reviewId" -> Response.success("Review ID retrieved successfully", getReviewId());
            case "orderId" -> Response.success("Order ID retrieved successfully", getOrderId());
            case "userId" -> Response.success("User ID retrieved successfully", getUserId());
            case "reviewContent" -> Response.success("Review content retrieved successfully", getReviewContent());
            case "reviewRating" -> Response.success("Review rating retrieved successfully", getReviewRating());
            case "updatedAt" -> Response.success("Updated at retrieved successfully", getUpdatedAt());
            case "createdAt" -> Response.success("Created at retrieved successfully", getCreatedAt());
            default -> Response.failure("Invalid attribute name");
        };
    }

    public void setReviewId(UUID reviewId) {
        this.reviewId = reviewId;
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

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public Response<Void> setAttributeValue(String attribute, Object newValue) {
        return switch (attribute) {
            case "reviewId" -> {
                if (newValue instanceof UUID) {
                    setReviewId((UUID) newValue);
                    yield Response.success("Review ID set successfully");
                } else {
                    yield Response.failure("Invalid newValue type");
                }
            }
            case "orderId" -> {
                if (newValue instanceof UUID) {
                    setOrderId((UUID) newValue);
                    yield Response.success("Order ID set successfully");
                } else {
                    yield Response.failure("Invalid newValue type");
                }
            }
            case "userId" -> {
                if (newValue instanceof UUID) {
                    setUserId((UUID) newValue);
                    yield Response.success("User ID set successfully");
                } else {
                    yield Response.failure("Invalid newValue type");
                }
            }
            case "reviewContent" -> {
                if (newValue instanceof String) {
                    setReviewContent((String) newValue);
                    yield Response.success("Review content set successfully");
                } else {
                    yield Response.failure("Invalid newValue type");
                }
            }
            case "reviewRating" -> {
                if (newValue instanceof reviewRating) {
                    setReviewRating((reviewRating) newValue);
                    yield Response.success("Review rating set successfully");
                } else {
                    yield Response.failure("Invalid newValue type");
                }
            }
            case "updatedAt" -> {
                if (newValue instanceof LocalDate) {
                    setUpdatedAt((LocalDate) newValue);
                    yield Response.success("Updated at set successfully");
                } else {
                    yield Response.failure("Invalid newValue type");
                }
            }
            case "createdAt" -> {
                if (newValue instanceof LocalDate) {
                    setCreatedAt((LocalDate) newValue);
                    yield Response.success("Created at set successfully");
                } else {
                    yield Response.failure("Invalid newValue type");
                }
            }
            default -> Response.failure("Invalid attribute name");
        };
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
