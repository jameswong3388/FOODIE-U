package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.models.Foods;

import java.time.LocalDateTime;
import java.util.UUID;

public class FoodDaoImpl extends AbstractDao<Foods> implements FoodDao {
    private static final String FILE_NAME = "database/foods.txt";

    public FoodDaoImpl() {
        super(FILE_NAME);
    }

    public Foods parse(String[] parts) {
        try {
            UUID foodId = UUID.fromString(parts[0]);
            UUID storeId = UUID.fromString(parts[1]);
            String foodName = parts[2];
            Foods.foodType foodType = Foods.foodType.valueOf(parts[3]);
            String foodDescription = parts[4];
            Double foodPrice = Double.parseDouble(parts[5]);
            LocalDateTime updatedAt = LocalDateTime.parse(parts[6]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[7]);

            return new Foods(foodId, storeId, foodName, foodType, foodDescription, foodPrice, updatedAt, createdAt);
        } catch (Exception e) {
            System.out.println("Failed to parse food: " + e.getMessage());
            return null;
        }
    }
}
