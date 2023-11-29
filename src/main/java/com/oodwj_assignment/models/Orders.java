package com.oodwj_assignment.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Orders extends Model {
    private UUID userId;
    private Double totalPrice;
    private Integer totalQuantity;
    private orderStatus status;
    private orderType type;
    private UUID transactionId;

    public Orders(UUID orderId, UUID userId, Double totalPrice, Integer totalQuantity, orderStatus status, orderType type, UUID transactionId, LocalDateTime updatedAt, LocalDateTime createdAt) {
        super(orderId, updatedAt, createdAt);
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
        this.status = status;
        this.type = type;
        this.transactionId = transactionId;
    }

    public UUID getUserId() {
        return userId;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public orderStatus getStatus() { return status; }

    public orderType getType() {
        return type;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public void setStatus(orderStatus status) {
        this.status = status;
    }

    public void setType(orderType type) {
        this.type = type;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public enum orderType {
        DineIn,
        TakeAway,
        Delivery
    }

    public enum orderStatus {
        OrderPlaced,
        Accepted,
        Declined,
        FoodsReady,
        Completed,
        Cancelled
    }

    @Override
    public String toString() {
        return getId() + ";" + getUserId() + ";" + getTotalPrice() + ";" + getTotalQuantity() + ";" + getStatus() + ";" + getType() + ";" + getTransactionId() + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }
}
