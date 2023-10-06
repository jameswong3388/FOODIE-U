package com.oodwj_assignment.Models;

import com.oodwj_assignment.APIs.Response;

import java.time.LocalDate;
import java.util.UUID;

public class Transactions {
    public UUID transactionId;
    public final Double amount;
    public final transactionType type;
    public final transactionStatus status;
    public final UUID payerId;
    public final UUID payeeId;
    public final LocalDate updatedAt;
    public final LocalDate createdAt;


    public Transactions(UUID transactionId, Double amount, transactionType type, transactionStatus status, UUID payerId, UUID payeeId, LocalDate updatedAt, LocalDate createdAt) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.type = type;
        this.status = status;
        this.payerId = payerId;
        this.payeeId = payeeId;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public UUID getTransactionId() {
        return transactionId;
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

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public Response<Object> getAttributeValue(String attributeName) {
        return switch (attributeName) {
            case "transactionId" -> Response.success("TransactionId found", transactionId);
            case "amount" -> Response.success("Amount found", amount);
            case "type" -> Response.success("Type found", type);
            case "status" -> Response.success("Status found", status);
            case "payerId" -> Response.success("PayerId found", payerId);
            case "payeeId" -> Response.success("PayeeId found", payeeId);
            case "updatedAt" -> Response.success("UpdatedAt found", updatedAt);
            case "createdAt" -> Response.success("CreatedAt found", createdAt);
            default -> Response.failure("Invalid attribute name");
        };
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public Response<Void> setAttributeValue(String attributeName, Object newValue) {
        switch (attributeName) {
            case "transactionId" -> {
                if (newValue instanceof UUID) {
                    setTransactionId((UUID) newValue);
                    return Response.success("TransactionId updated successfully");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "amount" -> {
                if (newValue instanceof Double) {
                    return Response.failure("Amount cannot be updated");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "type" -> {
                if (newValue instanceof transactionType) {
                    return Response.failure("Type cannot be updated");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "status" -> {
                if (newValue instanceof transactionStatus) {
                    return Response.failure("Status cannot be updated");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "payerId" -> {
                if (newValue instanceof UUID) {
                    return Response.failure("PayerId cannot be updated");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "payeeId" -> {
                if (newValue instanceof UUID) {
                    return Response.failure("PayeeId cannot be updated");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "updatedAt" -> {
                if (newValue instanceof LocalDate) {
                    return Response.failure("UpdatedAt cannot be updated");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "createdAt" -> {
                if (newValue instanceof LocalDate) {
                    return Response.failure("CreatedAt cannot be updated");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            default -> {
                return Response.failure("Invalid attribute name");
            }
        }
    }

    public enum transactionStatus {
        Pending, Completed, Cancelled, Failed,
    }

    public enum transactionType {
        Purchase, Sale, Refund, Transfer, Payment
    }

    @Override
    public String toString() {
        return transactionId + ";" + amount + ";" + type + ";" + status + ";" + payerId + ";" + payeeId + ";" + updatedAt + ";" + createdAt;
    }
}
