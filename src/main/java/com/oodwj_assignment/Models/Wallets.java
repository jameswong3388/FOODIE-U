package com.oodwj_assignment.Models;

import com.oodwj_assignment.APIs.Response;
import com.oodwj_assignment.Helpers.Model;

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

    public Response<Object> getAttributeValue(String attributeName) {
        switch (attributeName) {
            case "walletId" -> {
                return Response.success("Wallet ID", getId());
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

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Response<Void> setAttributeValue(String attributeName, Object newValue) {
        switch (attributeName) {
            case "walletId" -> {
                setId((UUID) newValue);
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
        return getId() + ";" + userId + ";" + credit + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }
}
