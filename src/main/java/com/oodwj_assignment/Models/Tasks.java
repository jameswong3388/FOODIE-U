package com.oodwj_assignment.Models;

import com.oodwj_assignment.APIs.Response;

import java.time.LocalDate;
import java.util.UUID;

public class Tasks {
    private UUID taskId;
    private UUID runnerId; // may be null
    private UUID orderId;
    private Integer deliveryFee;
    private taskStatus status;
    public LocalDate updatedAt;
    public LocalDate createdAt;

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

    public Response<Object> getAttributeValue(String attributeName) {
        return switch (attributeName) {
            case "taskId" -> Response.success("Task ID read successfully", getTaskId());
            case "runnerId" -> Response.success("Runner ID read successfully", getRunnerId());
            case "orderId" -> Response.success("Order ID read successfully", getOrderId());
            case "deliveryFee" -> Response.success("Delivery Fee read successfully", getDeliveryFee());
            case "status" -> Response.success("Status read successfully", getStatus());
            case "updatedAt" -> Response.success("Updated At read successfully", getUpdatedAt());
            case "createdAt" -> Response.success("Created At read successfully", getCreatedAt());
            default -> Response.failure("No such attribute");
        };
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

    public void setRunnerId(UUID runnerId) {
        this.runnerId = runnerId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public void setDeliveryFee(Integer deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public void setStatus(taskStatus status) {
        this.status = status;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public Response<Void> setAttributeValue(String attributeName, Object newValue) {
        switch (attributeName) {
            case "taskId":
                if (newValue instanceof UUID) {
                    setTaskId((UUID) newValue);
                    return Response.success("Task ID updated successfully");
                } else {
                    return Response.failure("Task ID must be a UUID");
                }
            case "runnerId":
                if (newValue instanceof UUID) {
                    setRunnerId((UUID) newValue);
                    return Response.success("Runner ID updated successfully");
                } else {
                    return Response.failure("Runner ID must be a UUID");
                }
            case "orderId":
                if (newValue instanceof UUID) {
                    setOrderId((UUID) newValue);
                    return Response.success("Order ID updated successfully");
                } else {
                    return Response.failure("Order ID must be a UUID");
                }
            case "deliveryFee":
                if (newValue instanceof Integer) {
                    setDeliveryFee((Integer) newValue);
                    return Response.success("Delivery Fee updated successfully");
                } else {
                    return Response.failure("Delivery Fee must be an Integer");
                }
            case "status":
                if (newValue instanceof taskStatus) {
                    setStatus((taskStatus) newValue);
                    return Response.success("Status updated successfully");
                } else {
                    return Response.failure("Status must be a taskStatus");
                }
            case "updatedAt":
                if (newValue instanceof LocalDate) {
                    setUpdatedAt((LocalDate) newValue);
                    return Response.success("Updated At updated successfully");
                } else {
                    return Response.failure("Updated At must be a LocalDate");
                }
            case "createdAt":
                if (newValue instanceof LocalDate) {
                    setCreatedAt((LocalDate) newValue);
                    return Response.success("Created At updated successfully");
                } else {
                    return Response.failure("Created At must be a LocalDate");
                }
            default:
                return Response.failure("No such attribute");
        }
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
