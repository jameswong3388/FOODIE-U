package com.oodwj_assignment.Models;

import com.oodwj_assignment.APIs.Response;
import com.oodwj_assignment.Helpers.Model;

import java.time.LocalDateTime;
import java.util.UUID;

public class OrderFoods extends Model {
    private UUID foodId;
    private UUID orderId;
    private String foodName;
    private Double foodPrice;
    private Integer foodQuantity;

    public OrderFoods(UUID oderFoodId, UUID foodId, UUID orderId, String foodName, Double foodPrice, Integer foodQuantity, LocalDateTime updatedAt, LocalDateTime createdAt) {
        super(oderFoodId, updatedAt, createdAt);
        this.foodId = foodId;
        this.orderId = orderId;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodQuantity = foodQuantity;
    }

    public UUID getFoodId() {
        return foodId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public String getFoodName() {
        return foodName;
    }

    public Double getFoodPrice() {
        return foodPrice;
    }

    public Integer getFoodQuantity() {
        return foodQuantity;
    }

    public Response<Object> getAttributeValue(String attributeName) {
        return switch (attributeName) {
            case "oderFoodId" -> Response.success("oderFoodId", getId());
            case "foodId" -> Response.success("foodId", getFoodId());
            case "orderId" -> Response.success("orderId", getOrderId());
            case "foodName" -> Response.success("foodName", getFoodName());
            case "foodPrice" -> Response.success("foodPrice", getFoodPrice());
            case "foodQuantity" -> Response.success("foodQuantity", getFoodQuantity());
            case "updatedAt" -> Response.success("updatedAt", getUpdatedAt());
            case "createdAt" -> Response.success("createdAt", getCreatedAt());
            default -> Response.failure("Attribute not found");
        };
    }

    public void setFoodId(UUID foodId) {
        this.foodId = foodId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setFoodPrice(Double foodPrice) {
        this.foodPrice = foodPrice;
    }

    public void setFoodQuantity(Integer foodQuantity) {
        this.foodQuantity = foodQuantity;
    }

    public Response<Void> setAttributeValue(String attributeName, Object newValue) {
        return switch (attributeName) {
            case "oderFoodId" -> {
                if (newValue instanceof UUID) {
                    setId((UUID) newValue);
                    yield Response.success("oderFoodId updated successfully");
                } else {
                    yield Response.failure("oderFoodId must be a UUID");
                }
            }
            case "foodId" -> {
                if (newValue instanceof UUID) {
                    setFoodId((UUID) newValue);
                    yield Response.success("foodId updated successfully");
                } else {
                    yield Response.failure("foodId must be a UUID");
                }
            }
            case "orderId" -> {
                if (newValue instanceof UUID) {
                    setOrderId((UUID) newValue);
                    yield Response.success("orderId updated successfully");
                } else {
                    yield Response.failure("orderId must be a UUID");
                }
            }
            case "foodName" -> {
                if (newValue instanceof String) {
                    setFoodName((String) newValue);
                    yield Response.success("foodName updated successfully");
                } else {
                    yield Response.failure("foodName must be a String");
                }
            }
            case "foodPrice" -> {
                if (newValue instanceof Double) {
                    setFoodPrice((Double) newValue);
                    yield Response.success("foodPrice updated successfully");
                } else {
                    yield Response.failure("foodPrice must be a String");
                }
            }
            case "foodQuantity" -> {
                if (newValue instanceof Integer) {
                    setFoodQuantity((Integer) newValue);
                    yield Response.success("foodQuantity updated successfully");
                } else {
                    yield Response.failure("foodQuantity must be a Integer");
                }
            }
            case "updatedAt" -> {
                if (newValue instanceof LocalDateTime) {
                    setUpdatedAt((LocalDateTime) newValue);
                    yield Response.success("updatedAt updated successfully");
                } else {
                    yield Response.failure("updatedAt must be a LocalDateTime");
                }
            }
            case "createdAt" -> {
                if (newValue instanceof LocalDateTime) {
                    setCreatedAt((LocalDateTime) newValue);
                    yield Response.success("createdAt updated successfully");
                } else {
                    yield Response.failure("createdAt must be a LocalDateTime");
                }
            }
            default -> Response.failure("Attribute not found");
        };
    }

    @Override
    public String toString() {
        return getId() + ";" + foodId + ";" + orderId + ";" + foodName + ";" + foodPrice + ";" + foodQuantity + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }
}
