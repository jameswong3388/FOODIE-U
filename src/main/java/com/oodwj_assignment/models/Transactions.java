package com.oodwj_assignment.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transactions extends Model {
    public Double amount;
    public transactionType type;
    public transactionStatus status;
    public UUID payerId;
    public UUID payeeId;

    public Transactions(UUID transactionId, Double amount, transactionType type, transactionStatus status, UUID payerId, UUID payeeId, LocalDateTime updatedAt, LocalDateTime createdAt) {
        super(transactionId, updatedAt, createdAt);
        this.amount = amount;
        this.type = type;
        this.status = status;
        this.payerId = payerId;
        this.payeeId = payeeId;
    }

    public Double getAmount() {
        return amount;
    }

    public transactionType getType() {
        return type;
    }

    public transactionStatus getStatus() {
        return status;
    }

    public UUID getPayerId() {
        return payerId;
    }

    public UUID getPayeeId() {
        return payeeId;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setType(transactionType type) {
        this.type = type;
    }

    public void setStatus(transactionStatus status) {
        this.status = status;
    }

    public void setPayerId(UUID payerId) {
        this.payerId = payerId;
    }

    public void setPayeeId(UUID payeeId) {
        this.payeeId = payeeId;
    }

    public enum transactionStatus {
        Pending, Completed, Cancelled, Failed,
    }

    public enum transactionType {
        Purchase, Sale, Refund, Transfer, Payment
    }

    @Override
    public String toString() {
        return getId() + ";" + getAmount() + ";" + getType() + ";" + getStatus() + ";" + getPayerId() + ";" + getPayeeId() + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }
}
