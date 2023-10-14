package com.oodwj_assignment.Models;

import com.oodwj_assignment.APIs.Response;

import java.time.LocalDateTime;
import java.util.UUID;

public class Carts {
    private UUID cartId;
    private UUID userId;
    private UUID foodId;
    private String foodName;
    private String foodPrice;
    private Integer foodQuantity;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    public Carts(UUID cartId, UUID userId, UUID foodId, String foodName, String foodPrice, Integer foodQuantity, LocalDateTime updatedAt, LocalDateTime createdAt) {
        this.cartId = cartId;
        this.userId = userId;
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodQuantity = foodQuantity;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public UUID getCartId() {
        return cartId;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getFoodId() {
        return foodId;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Response<Object> getAttributeValue(String attributeName) {
        return switch (attributeName) {
            case "cartId" -> Response.success("Cart ID found", getCartId());
            case "userId" -> Response.success("User ID found", getUserId());
            case "foodId" -> Response.success("Food ID found", getFoodId());
            case "foodName" -> Response.success("Food name found", getFoodName());
            case "foodPrice" -> Response.success("Food price found", getFoodPrice());
            case "foodQuantity" -> Response.success("Food quantity found", getFoodQuantity());
            case "updatedAt" -> Response.success("Updated at found", getUpdatedAt());
            case "createdAt" -> Response.success("Created at found", getCreatedAt());
            default -> Response.failure("Attribute not found");
        };
    }

    public void setCartId(UUID cartId) {
        this.cartId = cartId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setFoodId(UUID foodId) {
        this.foodId = foodId;
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

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Response<Void> setAttributeValue(String attributeName, Object newValue) {
        return switch (attributeName) {
            case "cartId" -> {
                if (newValue instanceof UUID) {
                    setCartId((UUID) newValue);
                    yield Response.success("Cart ID set successfully");
                } else {
                    yield Response.failure("Cart ID must be UUID");
                }
            }
            case "userId" -> {
                if (newValue instanceof UUID) {
                    setUserId((UUID) newValue);
                    yield Response.success("User ID set successfully");
                } else {
                    yield Response.failure("User ID must be UUID");
                }
            }
            case "foodId" -> {
                if (newValue instanceof UUID) {
                    setFoodId((UUID) newValue);
                    yield Response.success("Food ID set successfully");
                } else {
                    yield Response.failure("Food ID must be UUID");
                }
            }
            case "foodName" -> {
                if (newValue instanceof String) {
                    setFoodName((String) newValue);
                    yield Response.success("Food name set successfully");
                } else {
                    yield Response.failure("Food name must be String");
                }
            }
            case "foodPrice" -> {
                if (newValue instanceof String) {
                    setFoodPrice((String) newValue);
                    yield Response.success("Food price set successfully");
                } else {
                    yield Response.failure("Food price must be String");
                }
            }
            case "foodQuantity" -> {
                if (newValue instanceof Integer) {
                    setFoodQuantity((Integer) newValue);
                    yield Response.success("Food quantity set successfully");
                } else {
                    yield Response.failure("Food quantity must be Integer");
                }
            }
            case "updatedAt" -> {
                if (newValue instanceof LocalDateTime) {
                    setUpdatedAt((LocalDateTime) newValue);
                    yield Response.success("Updated at set successfully");
                } else {
                    yield Response.failure("Updated at must be LocalDateTime");
                }
            }
            case "createdAt" -> {
                if (newValue instanceof LocalDateTime) {
                    setCreatedAt((LocalDateTime) newValue);
                    yield Response.success("Created at set successfully");
                } else {
                    yield Response.failure("Created at must be LocalDateTime");
                }
            }
            default -> Response.failure("Attribute not found");
        };
    }

    @Override
    public String toString() {
        return cartId + "," + userId + "," + foodId + "," + foodName + "," + foodPrice + "," + foodQuantity + "," + updatedAt + "," + createdAt;
    }
}
