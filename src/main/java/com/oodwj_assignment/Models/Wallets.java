package com.oodwj_assignment.Models;

import com.oodwj_assignment.APIs.Response;

import java.time.LocalDate;
import java.util.UUID;

public class Wallets {
    public UUID walletId;
    public final UUID userId;
    public Double credit;
    private LocalDate updatedAt;
    private final LocalDate createdAt;

    public Wallets(UUID walletId, UUID userId, Double credit, LocalDate updatedAt, LocalDate createdAt) {
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

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public LocalDate getCreatedAt() {
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

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Response<Void> setAttributeValue(String attributeName, Object newValue) {
        switch (attributeName) {
            case "walletId" -> {
                if (newValue instanceof UUID) {
                    setWalletId((UUID) newValue);
                    return Response.success("Wallet ID updated successfully");
                } else {
                    return Response.failure("Wallet ID must be a UUID");
                }
            }
            case "credit" -> {
                if (newValue instanceof Double) {
                    setCredit((Double) newValue);
                    System.out.println("Credit updated successfully");
                    return Response.success("Credit updated successfully");
                } else {
                    return Response.failure("Credit must be a Double");
                }
            }
            case "updatedAt" -> {
                if (newValue instanceof LocalDate) {
                    setUpdatedAt((LocalDate) newValue);
                    return Response.success("Updated At updated successfully");
                } else {
                    return Response.failure("Updated At must be a LocalDate");
                }
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
