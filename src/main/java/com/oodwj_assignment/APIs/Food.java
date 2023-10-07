package com.oodwj_assignment.APIs;

import com.oodwj_assignment.Models.Foods;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class Food {
    private static final String FILE_NAME = "src/main/java/com/oodwj_assignment/Databases/foods.txt";

    public static Response<Void> create(Foods food) {
        UUID foodId = UUID.randomUUID();
        food.setFoodId(foodId);

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            writer.println(food);

            return Response.success("Food created successfully");
        } catch (IOException e) {
            return Response.failure("Failed to create food: " + e.getMessage());
        }
    }

    public static Response<ArrayList<Foods>> read(Map<String, Object> query) {
        ArrayList<Foods> foods = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");

                if (parts.length == 8) {
                    UUID foodId = UUID.fromString(parts[0]);
                    UUID storeId = UUID.fromString(parts[1]);
                    String foodName = parts[2];
                    Foods.foodType foodType = Foods.foodType.valueOf(parts[3]);
                    String foodDescription = parts[4];
                    String foodPrice = parts[5];
                    LocalDate updatedAt = LocalDate.parse(parts[6]);
                    LocalDate createdAt = LocalDate.parse(parts[7]);

                    foods.add(new Foods(foodId, storeId, foodName, foodType, foodDescription, foodPrice, updatedAt, createdAt));
                }
            }
        } catch (IOException e) {
            return Response.failure("Failed to read foods: " + e.getMessage());
        }

        if (query.isEmpty()) {
            return Response.success("Foods read successfully", foods);
        }

        ArrayList<Foods> matchedFoods = new ArrayList<>();

        for (Foods food : foods) {
            Response<Void> matchRes = match(query, food);

            if (matchRes.isSuccess()) {
                matchedFoods.add(food);
            }
        }

        return Response.success("Foods read successfully", matchedFoods);
    }

    public static Response<Void> update(Map<String, Object> query, Map<String, Object> newValue) {
        Response<ArrayList<Foods>> foods = read(query);

        if (foods.isSuccess()) {
            for (Foods food : foods.getData()) {
                for (Map.Entry<String, Object> entry : newValue.entrySet()) {
                    String attributeName = entry.getKey();
                    Object expectedValue = entry.getValue();

                    Response<Void> updateRes = food.setAttributeValue(attributeName, expectedValue);

                    if (!updateRes.isSuccess()) {
                        return Response.failure(updateRes.getMessage());
                    }
                }
            }

            Response<Void> saveRes = saveAllFoods(foods.getData());

            if (saveRes.isSuccess()) {
                return Response.success("Food updated successfully");
            } else {
                return Response.failure(saveRes.getMessage());
            }
        } else {
            return Response.failure(foods.getMessage());
        }
    }

    public static Response<Void> delete(Map<String, Object> query) {
        Response<ArrayList<Foods>> foods = read(Map.of());

        if (foods.isSuccess()) {
            for (Foods food : foods.getData()) {
                Response<Void> matchRes = match(query, food);

                if (matchRes.isSuccess()) {
                    foods.getData().remove(food);
                    Response<Void> saveRes = saveAllFoods(foods.getData());

                    if (saveRes.isSuccess()) {
                        return Response.success("Food deleted successfully");
                    } else {
                        return Response.failure(saveRes.getMessage());
                    }
                }
            }
            return Response.failure("Food not found");

        } else {
            return Response.failure(foods.getMessage());
        }
    }


    private static Response<Void> match(Map<String, Object> query, Foods food) {
        boolean match = true;

        for (Map.Entry<String, Object> entry : query.entrySet()) {
            String attributeName = entry.getKey();
            Object expectedValue = entry.getValue();

            Response<Object> actualValue = food.getAttributeValue(attributeName);

            if (actualValue.isSuccess()) {
                if (!actualValue.getData().equals(expectedValue)) {
                    match = false;
                    break;
                }
            } else {
                return Response.failure(actualValue.getMessage());
            }
        }
        if (match) {
            return Response.success("Food found");
        } else {
            return Response.failure("Food not found");
        }
    }

    private static Response<Void> saveAllFoods(ArrayList<Foods> foods) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Foods food : foods) {
                writer.println(food);
            }
            return Response.success("Foods saved successfully");
        } catch (IOException e) {
            return Response.failure("Failed to save foods: " + e.getMessage());
        }
    }
}
