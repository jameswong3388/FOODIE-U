package com.oodwj_assignment.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Orders extends Model {
    private  UUID userId;
    private  Double totalPrice;
    private  Integer totalQuantity;
    private  oderStatus status;
    private  orderType type;

    public Orders(UUID orderId, UUID userId, Double totalPrice, Integer totalQuantity, oderStatus status, orderType type, LocalDateTime updatedAt, LocalDateTime createdAt) {
        super(orderId, updatedAt, createdAt);
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
        this.status = status;
        this.type = type;
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

    public oderStatus getStatus() {
        return status;
    }

    public orderType getType() {
        return type;
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

    public void setStatus(oderStatus status) {
        this.status = status;
    }

    public void setType(orderType type) {
        this.type = type;
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
        return getId() + ";" + getUserId() + ";" + getTotalPrice() + ";" + getTotalQuantity() + ";" + getStatus() + ";" + getType() + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }
}
