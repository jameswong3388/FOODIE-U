package com.oodwj_assignment.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Tasks extends Model {
    private UUID runnerId; // may be null
    private UUID orderId;
    private Double deliveryFee;
    private taskStatus status;
    private UUID transactionId;

    public Tasks(UUID taskId, UUID runnerId, UUID orderId, Double deliveryFee, taskStatus status, UUID transactionId, LocalDateTime updatedAt, LocalDateTime createdAt) {
        super(taskId, updatedAt, createdAt);
        this.runnerId = runnerId;
        this.orderId = orderId;
        this.deliveryFee = deliveryFee;
        this.status = status;
        this.transactionId = transactionId;
    }

    public UUID getRunnerId() { return runnerId; }

    public UUID getOrderId() {
        return orderId;
    }

    public Double getDeliveryFee() {
        return deliveryFee;
    }

    public taskStatus getStatus() {
        return status;
    }

    public UUID getTransactionId() {
        return transactionId;
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

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public enum taskStatus {
        Pending,
        Accepted,
        Declined,
        PickedUp,
        Delivered,
    }

    @Override
    public String toString() {
        return getId() + ";" + getRunnerId() + ";" + getOrderId() + ";" + getDeliveryFee() + ";" + getStatus() + ";" + getTransactionId() + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }

}
