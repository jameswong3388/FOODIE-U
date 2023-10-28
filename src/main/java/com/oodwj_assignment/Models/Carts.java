package com.oodwj_assignment.Models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Carts extends Model {

    private UUID userId;
    private UUID foodId;
    private String foodName;
    private String foodPrice;
    private Integer foodQuantity;


    public Carts(UUID cartId, UUID userId, UUID foodId, String foodName, String foodPrice, Integer foodQuantity, LocalDateTime updatedAt, LocalDateTime createdAt) {
        super(cartId, updatedAt, createdAt);
        this.userId = userId;
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodQuantity = foodQuantity;
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

    @Override
    public String toString() {
        return getId() + "," + getUserId() + "," + getFoodId() + "," + getFoodName() + "," + getFoodPrice() + "," + getFoodQuantity() + "," + getUpdatedAt() + "," + getCreatedAt();
    }
}
