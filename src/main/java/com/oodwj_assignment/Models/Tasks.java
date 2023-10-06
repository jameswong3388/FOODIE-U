package com.oodwj_assignment.Models;

import java.time.LocalDate;
import java.util.UUID;

public class Tasks {
    private final UUID taskId;
    private final UUID runnerId; // may be null
    private final UUID orderId;
    private final Integer deliveryFee;
    private final taskStatus status;
    public final LocalDate updatedAt;
    public final LocalDate createdAt;

    public Tasks(UUID taskId, UUID runnerId, UUID orderId, Integer deliveryFee, taskStatus status, LocalDate updatedAt, LocalDate createdAt) {
        this.taskId = taskId;
        this.runnerId = runnerId;
        this.orderId = orderId;
        this.deliveryFee = deliveryFee;
        this.status = status;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public UUID getRunnerId() {
        return runnerId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public Integer getDeliveryFee() {
        return deliveryFee;
    }

    public taskStatus getStatus() {
        return status;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
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
        return taskId + ";" + runnerId + ";" + orderId + ";" + deliveryFee + ":" + status + ":" + updatedAt + ";" + createdAt;
    }

}
