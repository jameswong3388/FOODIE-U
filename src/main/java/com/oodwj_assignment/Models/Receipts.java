package com.oodwj_assignment.Models;

import com.oodwj_assignment.APIs.Response;
import com.oodwj_assignment.Helpers.Model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Receipts extends Model {
    private final UUID userId;
    private final Double credit;

    public Receipts(UUID receiptId, UUID userId, Double credit, LocalDateTime updatedAt, LocalDateTime createdAt) {
        super(receiptId, updatedAt, createdAt);
        this.userId = userId;
        this.credit = credit;
    }

    public UUID getUserId() {
        return userId;
    }

    public Double getCredit() {
        return credit;
    }

    public Response<Object> getAttributeValue(String attributeName) {
        switch (attributeName) {
            case "receiptId" -> {
                return Response.success("Receipt ID", getId());
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

    @Override
    public String toString() {
        return getId() + ";" + userId + ";" + credit + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }
}
