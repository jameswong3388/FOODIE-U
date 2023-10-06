package com.oodwj_assignment.Models;

import java.time.LocalDate;
import java.util.UUID;

public class Foods {
    private final UUID foodId;
    private final UUID storeId;
    private final String foodName;
    private final Foods.foodType foodType;
    private final String foodDescription;
    private final String foodPrice;
    private final LocalDate updatedAt;
    private final LocalDate createdAt;

    public Foods(UUID foodId, UUID storeId, String foodName, Foods.foodType foodType, String foodDescription, String foodPrice, LocalDate updatedAt, LocalDate createdAt) {
        this.foodId = foodId;
        this.storeId = storeId;
        this.foodName = foodName;
        this.foodType = foodType;
        this.foodDescription = foodDescription;
        this.foodPrice = foodPrice;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public UUID getFoodId() {
        return foodId;
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

    public String getFoodPrice() {
        return foodPrice;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public enum foodType {
        Appetizer,
        MainCourse,
        Dessert,
        Drink
    }

    @Override
    public String toString() {
        return foodId + ";" + storeId + ";" + foodName + ";" + foodType + ";" + foodDescription + ";" + foodPrice + ";" + updatedAt + ";" + createdAt;
    }
}
