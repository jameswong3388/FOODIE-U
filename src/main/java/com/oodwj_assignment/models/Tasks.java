package com.oodwj_assignment.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Tasks extends Model {
    private UUID runnerId; // may be null
    private UUID orderId;
    private Double deliveryFee;
    private taskStatus status;

    public Tasks(UUID taskId, UUID runnerId, UUID orderId, Double deliveryFee, taskStatus status, LocalDateTime updatedAt, LocalDateTime createdAt) {
        super(taskId, updatedAt, createdAt);
        this.runnerId = runnerId;
        this.orderId = orderId;
        this.deliveryFee = deliveryFee;
        this.status = status;
    }

    public UUID getRunnerId() {
        return runnerId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public Double getDeliveryFee() {
        return deliveryFee;
    }

    public taskStatus getStatus() {
        return status;
    }

    public void setRunnerId(UUID runnerId) {
        this.runnerId = runnerId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public void setDeliveryFee(Double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public void setStatus(taskStatus status) {
        this.status = status;
    }

    public enum taskStatus {
        Pending,
        Accepted,
        Processing,
        PreparingToShip,
        Shipped,
        Delivered,
        Cancelled
    }

    @Override
    public String toString() {
        return getId() + ";" + getRunnerId() + ";" + getOrderId() + ";" + getDeliveryFee() + ";" + getStatus() + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }

}
