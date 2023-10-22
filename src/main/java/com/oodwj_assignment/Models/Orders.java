package com.oodwj_assignment.Models;

import com.oodwj_assignment.APIs.Response;
import com.oodwj_assignment.Helpers.Model;

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

    public Response<Object> getAttributeValue(String attributeName) {
        return switch (attributeName) {
            case "orderId" -> Response.success("Order ID found", getId());
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

    public Response<Void> setAttributeValue(String attributeName, Object newValue) {
        return switch (attributeName) {
            case "orderId" -> {
                if (newValue instanceof UUID) {
                    setId((UUID) newValue);
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
                if (newValue instanceof Double) {
                    setTotalPrice((Double) newValue);
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
                if (newValue instanceof LocalDateTime) {
                    setUpdatedAt((LocalDateTime) newValue);
                    yield Response.success("Updated at updated successfully");
                } else {
                    yield Response.failure("Updated at must be a LocalDateTime");
                }
            }
            case "createdAt" -> {
                if (newValue instanceof LocalDateTime) {
                    setCreatedAt((LocalDateTime) newValue);
                    yield Response.success("Created at updated successfully");
                } else {
                    yield Response.failure("Created at must be a LocalDateTime");
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
        return getId() + ";" + userId + ";" + totalPrice + ";" + totalQuantity + ";" + status + ";" + type + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }
}
