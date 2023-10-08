package com.oodwj_assignment.Models;

import com.oodwj_assignment.APIs.Response;

import java.time.LocalDateTime;
import java.util.UUID;

public class Receipts {
    private UUID receiptId;
    private final UUID userId;
    private final Double credit;
    private final LocalDateTime updatedAt;
    private final LocalDateTime createdAt;

    public Receipts(UUID receiptId, UUID userId, Double credit, LocalDateTime updatedAt, LocalDateTime createdAt) {
        this.receiptId = receiptId;
        this.userId = userId;
        this.credit = credit;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public UUID getReceiptId() {
        return receiptId;
    }

    public UUID getUserId() {
        return userId;
    }

    public Double getCredit() {
        return credit;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Response<Object> getAttributeValue(String attributeName) {
        switch (attributeName) {
            case "receiptId" -> {
                return Response.success("Receipt ID", getReceiptId());
            }
            case "userId" -> {
                return Response.success("User ID", getUserId());
            }
            case "credit" -> {
                return Response.success("Credit", getCredit());
            }
            case "updatedAt" -> {
                return Response.success("Updated At", getUpdatedAt());
            }
            case "createdAt" -> {
                return Response.success("Created At", getCreatedAt());
            }
            default -> {
                return Response.failure("Invalid attribute name");
            }
        }
    }

    public void setReceiptId(UUID receiptId) {
        this.receiptId = receiptId;
    }

    @Override
    public String toString() {
        return receiptId + " " + userId + " " + credit + " " + updatedAt + " " + createdAt;
    }


}
