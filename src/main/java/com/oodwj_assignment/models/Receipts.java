package com.oodwj_assignment.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Receipts extends Model {
    private final UUID userId;
    private final UUID transactionId;

    public Receipts(UUID receiptId, UUID userId, UUID transactionId, LocalDateTime updatedAt, LocalDateTime createdAt) {
        super(receiptId, updatedAt, createdAt);
        this.userId = userId;
        this.transactionId = transactionId;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    @Override
    public String toString() {
        return getId() + ";" + getUserId() + ";" + getTransactionId() + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }
}
