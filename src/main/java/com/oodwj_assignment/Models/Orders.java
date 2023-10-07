package com.oodwj_assignment.Models;

import com.oodwj_assignment.APIs.Response;

import java.time.LocalDate;
import java.util.UUID;

public class Orders {
    private UUID orderId;
    private  UUID userId;
    private  Integer totalPrice;
    private  Integer totalQuantity;
    private  oderStatus status;
    private  orderType type;
    private  LocalDate updatedAt;
    private  LocalDate createdAt;

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

    public Response<Object> getAttributeValue(String attributeName) {
        return switch (attributeName) {
            case "orderId" -> Response.success("Order ID found", getOrderId());
            case "userId" -> Response.success("User ID found", getUserId());
            case "totalPrice" -> Response.success("Total price found", getTotalPrice());
            case "totalQuantity" -> Response.success("Total quantity found", getTotalQuantity());
            case "status" -> Response.success("Status found", getStatus());
            case "type" -> Response.success("Type found", getType());
            case "updatedAt" -> Response.success("Updated at found", getUpdatedAt());
            case "createdAt" -> Response.success("Created at found", getCreatedAt());
            default -> Response.failure("Attribute not found");
        };
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setTotalPrice(Integer totalPrice) {
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

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public Response<Void> setAttributeValue(String attributeName, Object newValue) {
        return switch (attributeName) {
            case "orderId" -> {
                if (newValue instanceof UUID) {
                    setOrderId((UUID) newValue);
                    yield Response.success("Order ID updated successfully");
                } else {
                    yield Response.failure("Order ID must be a UUID");
                }
            }
            case "userId" -> {
                if (newValue instanceof UUID) {
                    setUserId((UUID) newValue);
                    yield Response.success("User ID updated successfully");
                } else {
                    yield Response.failure("User ID must be a UUID");
                }
            }
            case "totalPrice" -> {
                if (newValue instanceof Integer) {
                    setTotalPrice((Integer) newValue);
                    yield Response.success("Total price updated successfully");
                } else {
                    yield Response.failure("Total price must be an Integer");
                }
            }
            case "totalQuantity" -> {
                if (newValue instanceof Integer) {
                    setTotalQuantity((Integer) newValue);
                    yield Response.success("Total quantity updated successfully");
                } else {
                    yield Response.failure("Total quantity must be an Integer");
                }
            }
            case "status" -> {
                if (newValue instanceof oderStatus) {
                    setStatus((oderStatus) newValue);
                    yield Response.success("Status updated successfully");
                } else {
                    yield Response.failure("Status must be an oderStatus");
                }
            }
            case "type" -> {
                if (newValue instanceof orderType) {
                    setType((orderType) newValue);
                    yield Response.success("Type updated successfully");
                } else {
                    yield Response.failure("Type must be an orderType");
                }
            }
            case "updatedAt" -> {
                if (newValue instanceof LocalDate) {
                    setUpdatedAt((LocalDate) newValue);
                    yield Response.success("Updated at updated successfully");
                } else {
                    yield Response.failure("Updated at must be a LocalDate");
                }
            }
            case "createdAt" -> {
                if (newValue instanceof LocalDate) {
                    setCreatedAt((LocalDate) newValue);
                    yield Response.success("Created at updated successfully");
                } else {
                    yield Response.failure("Created at must be a LocalDate");
                }
            }
            default -> Response.failure("Attribute not found");
        };
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
