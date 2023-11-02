package com.oodwj_assignment.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class OrderFoods extends Model {
    private UUID foodId;
    private UUID orderId;
    private String foodName;
    private Double foodPrice;
    private Double amount;
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

    public Double getAmount() {
        return amount;
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

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return getId() + ";" + getFoodId() + ";" + getOrderId() + ";" + getFoodName() + ";" + getFoodPrice() + ";" + getFoodQuantity() + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }
}
