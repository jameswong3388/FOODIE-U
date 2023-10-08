package com.oodwj_assignment.Models;

import com.oodwj_assignment.APIs.Response;

import java.time.LocalDateTime;
import java.util.UUID;

public class Wallets {
    public UUID walletId;
    public UUID userId;
    public Double credit;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    public Wallets(UUID walletId, UUID userId, Double credit, LocalDateTime updatedAt, LocalDateTime createdAt) {
        this.walletId = walletId;
        this.userId = userId;
        this.credit = credit;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public UUID getWalletId() {
        return walletId;
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
            case "walletId" -> {
                return Response.success("Wallet ID", getWalletId());
            }
            case "userId" -> {
                return Response.success("Users ID", getUserId());
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

    public void setWalletId(UUID walletId) {
        this.walletId = walletId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Response<Void> setAttributeValue(String attributeName, Object newValue) {
        switch (attributeName) {
            case "walletId" -> {
                setWalletId((UUID) newValue);
                return Response.success("Wallet ID updated successfully");
            }
            case "userId" -> {
                setUserId((UUID) newValue);
                return Response.success("User ID updated successfully");
            }
            case "credit" -> {
                setCredit((Double) newValue);
                return Response.success("Credit updated successfully");
            }
            case "updatedAt" -> {
                setUpdatedAt((LocalDateTime) newValue);
                return Response.success("Updated At updated successfully");
            }
            case "createdAt" -> {
                setCreatedAt((LocalDateTime) newValue);
                return Response.success("Created At updated successfully");
            }
            default -> {
                return Response.failure("Invalid attribute name");
            }
        }
    }


    @Override
    public String toString() {
        return walletId + ";" + userId + ";" + credit + ";" + updatedAt + ";" + createdAt;
    }
}
