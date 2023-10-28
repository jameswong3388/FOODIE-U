package com.oodwj_assignment.Models;

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

    @Override
    public String toString() {
        return getId() + ";" + getUserId() + ";" + getCredit() + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }
}
