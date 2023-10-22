package com.oodwj_assignment.Models;

import com.oodwj_assignment.APIs.Response;
import com.oodwj_assignment.Helpers.Model;

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

    public Response<Object> getAttributeValue(String attributeName) {
        switch (attributeName) {
            case "foodId" -> {
                return Response.success("Food ID retrieved successfully", getId());
            }
            case "storeId" -> {
                return Response.success("Store ID retrieved successfully", getStoreId());
            }
            case "foodName" -> {
                return Response.success("Food name retrieved successfully", getFoodName());
            }
            case "foodType" -> {
                return Response.success("Food type retrieved successfully", getFoodType());
            }
            case "foodDescription" -> {
                return Response.success("Food description retrieved successfully", getFoodDescription());
            }
            case "foodPrice" -> {
                return Response.success("Food price retrieved successfully", getFoodPrice());
            }
            case "updatedAt" -> {
                return Response.success("Updated at retrieved successfully", getUpdatedAt());
            }
            case "createdAt" -> {
                return Response.success("Created at retrieved successfully", getCreatedAt());
            }
            default -> {
                return Response.failure("Attribute name not found");
            }
        }
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

    public Response<Void> setAttributeValue(String attributeName, Object newValue) {
        switch (attributeName) {
            case "foodId" -> {
                if (newValue instanceof UUID) {
                    setId((UUID) newValue);
                    return Response.success("Food ID updated successfully");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "storeId" -> {
                if (newValue instanceof UUID) {
                    setStoreId((UUID) newValue);
                    return Response.success("Store ID updated successfully");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "foodName" -> {
                if (newValue instanceof String) {
                    setFoodName((String) newValue);
                    return Response.success("Food name updated successfully");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "foodType" -> {
                if (newValue instanceof Foods.foodType) {
                    setFoodType((Foods.foodType) newValue);
                    return Response.success("Food type updated successfully");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "foodDescription" -> {
                if (newValue instanceof String) {
                    setFoodDescription((String) newValue);
                    return Response.success("Food description updated successfully");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "foodPrice" -> {
                if (newValue instanceof Double) {
                    setFoodPrice((Double) newValue);
                    return Response.success("Food price updated successfully");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "updatedAt" -> {
                if (newValue instanceof LocalDateTime) {
                    setUpdatedAt((LocalDateTime) newValue);
                    return Response.success("Updated at updated successfully");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            case "createdAt" -> {
                if (newValue instanceof LocalDateTime) {
                    setCreatedAt((LocalDateTime) newValue);
                    return Response.success("Created at updated successfully");
                } else {
                    return Response.failure("Invalid value type");
                }
            }
            default -> {
                return Response.failure("Invalid attribute name");
            }
        }
    }

    public enum foodType {
        Appetizer,
        MainCourse,
        Dessert,
        Drink
    }

    @Override
    public String toString() {
        return getId() + ";" + storeId + ";" + foodName + ";" + foodType + ";" + foodDescription + ";" + foodPrice + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }
}
