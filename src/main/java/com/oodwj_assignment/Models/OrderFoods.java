package com.oodwj_assignment.Models;

import com.oodwj_assignment.APIs.Response;

import java.time.LocalDate;
import java.util.UUID;

public class OrderFoods {
    private UUID oderFoodId;
    private UUID foodId;
    private UUID orderId;
    private String foodName;
    private String foodPrice;
    private Integer foodQuantity;
    private LocalDate updatedAt;
    private LocalDate createdAt;

    public OrderFoods(UUID oderFoodId, UUID foodId, UUID orderId, String foodName, String foodPrice, Integer foodQuantity, LocalDate updatedAt, LocalDate createdAt) {
        this.oderFoodId = oderFoodId;
        this.foodId = foodId;
        this.orderId = orderId;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodQuantity = foodQuantity;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public UUID getOderFoodId() {
        return oderFoodId;
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

    public String getFoodPrice() {
        return foodPrice;
    }

    public Integer getFoodQuantity() {
        return foodQuantity;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public Response<Object> getAttributeValue(String attributeName) {
        return switch (attributeName) {
            case "oderFoodId" -> Response.success("oderFoodId", getOderFoodId());
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

    public void setOderFoodId(UUID oderFoodId) {
        this.oderFoodId = oderFoodId;
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

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public void setFoodQuantity(Integer foodQuantity) {
        this.foodQuantity = foodQuantity;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public Response<Void> setAttributeValue(String attributeName, Object newValue) {
        return switch (attributeName) {
            case "oderFoodId" -> {
                if (newValue instanceof UUID) {
                    setOderFoodId((UUID) newValue);
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
                if (newValue instanceof String) {
                    setFoodPrice((String) newValue);
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
                if (newValue instanceof LocalDate) {
                    setUpdatedAt((LocalDate) newValue);
                    yield Response.success("updatedAt updated successfully");
                } else {
                    yield Response.failure("updatedAt must be a LocalDate");
                }
            }
            case "createdAt" -> {
                if (newValue instanceof LocalDate) {
                    setCreatedAt((LocalDate) newValue);
                    yield Response.success("createdAt updated successfully");
                } else {
                    yield Response.failure("createdAt must be a LocalDate");
                }
            }
            default -> Response.failure("Attribute not found");
        };
    }

    @Override
    public String toString() {
        return oderFoodId + ";" + foodId + ";" + orderId + ";" + foodName + ";" + foodPrice + ";" + foodQuantity + ";" + updatedAt + ";" + createdAt;
    }
}
