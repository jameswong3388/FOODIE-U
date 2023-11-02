package com.oodwj_assignment.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Foods extends Model {

    private UUID storeId;
    private String foodName;
    private Foods.foodType foodType;
    private Double foodPrice;

    public Foods(UUID foodId, UUID storeId, String foodName, Foods.foodType foodType, Double foodPrice, LocalDateTime updatedAt, LocalDateTime createdAt) {
        super(foodId, updatedAt, createdAt);
        this.storeId = storeId;
        this.foodName = foodName;
        this.foodType = foodType;
        this.foodPrice = foodPrice;
    }

    public UUID getStoreId() {
        return storeId;
    }

    public String getFoodName() {
        return foodName;
    }

    public Foods.foodType getFoodType() {
        return foodType;
    }

    public Double getFoodPrice() {
        return foodPrice;
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setFoodType(Foods.foodType foodType) {
        this.foodType = foodType;
    }

    public void setFoodPrice(Double foodPrice) {
        this.foodPrice = foodPrice;
    }

    public enum foodType {
        Appetizer,
        MainCourse,
        Dessert,
        Drink
    }

    @Override
    public String toString() {
        return getId() + ";" + getStoreId() + ";" + getFoodName() + ";" + getFoodType() + ";" + getFoodPrice() + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }
}
