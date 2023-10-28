package com.oodwj_assignment.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Foods extends Model {

    private UUID storeId;
    private String foodName;
    private Foods.foodType foodType;
    private String foodDescription;
    private Double foodPrice;

    public Foods(UUID foodId, UUID storeId, String foodName, Foods.foodType foodType, String foodDescription, Double foodPrice, LocalDateTime updatedAt, LocalDateTime createdAt) {
        super(foodId, updatedAt, createdAt);
        this.storeId = storeId;
        this.foodName = foodName;
        this.foodType = foodType;
        this.foodDescription = foodDescription;
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

    public String getFoodDescription() {
        return foodDescription;
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

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
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
        return getId() + ";" + getStoreId() + ";" + getFoodName() + ";" + getFoodType() + ";" + getFoodDescription() + ";" + getFoodPrice() + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }
}
