package com.oodwj_assignment.Models;

import java.util.UUID;

public class OderFoods {
    private final UUID oderFoodId;
    private final UUID foodId;
    private final UUID orderId;
    private final String foodName;
    private final String foodPrice;
    private final Integer foodQuantity;

    public OderFoods(UUID oderFoodId, UUID foodId, UUID orderId, String foodName, String foodPrice, Integer foodQuantity) {
        this.oderFoodId = oderFoodId;
        this.foodId = foodId;
        this.orderId = orderId;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodQuantity = foodQuantity;
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

    @Override
    public String toString() {
        return oderFoodId + ";" + foodId + ";" + orderId + ";" + foodName + ";" + foodPrice + ";" + foodQuantity;
    }
}
