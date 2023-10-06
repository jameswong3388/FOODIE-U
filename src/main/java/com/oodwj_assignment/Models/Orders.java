package com.oodwj_assignment.Models;

import java.time.LocalDate;
import java.util.UUID;

public class Orders {
    private final UUID orderId;
    private final UUID userId;
    private final Integer totalPrice;
    private final Integer totalQuantity;
    private final oderStatus status;
    private final orderType type;
    private final LocalDate updatedAt;
    private final LocalDate createdAt;

    public Orders(UUID orderId, UUID userId, Integer totalPrice, Integer totalQuantity, oderStatus status, orderType type, LocalDate updatedAt, LocalDate createdAt) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
        this.status = status;
        this.type = type;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getUserId() {
        return userId;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public oderStatus getStatus() {
        return status;
    }

    public orderType getType() {
        return type;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public enum orderType {
        DineIn,
        TakeAway,
        Delivery
    }

    public enum oderStatus {
        OrderPlaced,
        Processing,
        PreparingToShip,
        Shipped,
        Delivered,
        Cancelled,
        Declined
    }

    @Override
    public String toString() {
        return orderId + ";" + userId + ";" + totalPrice + ";" + totalQuantity + ";" + updatedAt + ";" + createdAt;
    }
}
