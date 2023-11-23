package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.models.Foods;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

public class FoodDaoImpl extends AbstractDao<Foods> implements FoodDao {
    private static final File FILE = new File("database/foods.dat");

    public FoodDaoImpl() {
        super(FILE);
    }

    public Foods parse(String[] parts) {
        try {
            UUID foodId = UUID.fromString(parts[0]);
            UUID storeId = UUID.fromString(parts[1]);
            String foodName = parts[2];
            Foods.foodType foodType = Foods.foodType.valueOf(parts[3]);
            Double foodPrice = Double.parseDouble(parts[4]);
            LocalDateTime updatedAt = LocalDateTime.parse(parts[5]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[6]);

            return new Foods(foodId, storeId, foodName, foodType, foodPrice, updatedAt, createdAt);
        } catch (Exception e) {
            System.out.println("Failed to parse food: " + e.getMessage());
            return null;
        }
    }
}
