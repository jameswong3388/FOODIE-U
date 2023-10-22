package com.oodwj_assignment.Models;

import com.oodwj_assignment.APIs.Response;
import com.oodwj_assignment.Helpers.Model;

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

    public Response<Object> getAttributeValue(String attributeName) {
        return switch (attributeName) {
            case "transactionId" -> Response.success("TransactionId found", getId());
            case "amount" -> Response.success("Amount found", getAmount());
            case "type" -> Response.success("Type found", getType());
            case "status" -> Response.success("Status found", getStatus());
            case "payerId" -> Response.success("PayerId found", getPayerId());
            case "payeeId" -> Response.success("PayeeId found", getPayeeId());
            case "updatedAt" -> Response.success("UpdatedAt found", getUpdatedAt());
            case "createdAt" -> Response.success("CreatedAt found", getCreatedAt());
            default -> Response.failure("Invalid attribute name");
        };
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

    public Response<Void> setAttributeValue(String attributeName, Object newValue) {
        switch (attributeName) {
            case "transactionId" -> {
                if (newValue instanceof UUID) {
                    setId((UUID) newValue);
                    return Response.success("TransactionId updated successfully");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "amount" -> {
                if (newValue instanceof Double) {
                    setAmount((Double) newValue);
                    return Response.success("Amount updated successfully");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "type" -> {
                if (newValue instanceof transactionType) {
                    setType((transactionType) newValue);
                    return Response.success("Type updated successfully");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "status" -> {
                if (newValue instanceof transactionStatus) {
                    setStatus((transactionStatus) newValue);
                    return Response.success("Status updated successfully");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "payerId" -> {
                if (newValue instanceof UUID) {
                    setPayerId((UUID) newValue);
                    return Response.success("PayerId updated successfully");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "payeeId" -> {
                if (newValue instanceof UUID) {
                    setPayeeId((UUID) newValue);
                    return Response.success("PayeeId updated successfully");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "updatedAt" -> {
                if (newValue instanceof LocalDateTime) {
                    setUpdatedAt((LocalDateTime) newValue);
                    return Response.success("UpdatedAt updated successfully");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "createdAt" -> {
                if (newValue instanceof LocalDateTime) {
                    setCreatedAt((LocalDateTime) newValue);
                    return Response.success("CreatedAt updated successfully");
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
        return getId() + ";" + amount + ";" + type + ";" + status + ";" + payerId + ";" + payeeId + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }
}
