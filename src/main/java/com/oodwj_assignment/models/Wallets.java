package com.oodwj_assignment.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Wallets extends Model {
    public UUID userId;
    public Double credit;

    public Wallets(UUID walletId, UUID userId, Double credit, LocalDateTime updatedAt, LocalDateTime createdAt) {
        super(walletId, updatedAt, createdAt);
        this.userId = userId;
        this.credit = credit;
    }

    public UUID getUserId() {
        return userId;
    }

    public Double getCredit() {
        return credit;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    @Override
    public String toString() {
        return getId() + ";" + getUserId() + ";" + getCredit() + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }
}
