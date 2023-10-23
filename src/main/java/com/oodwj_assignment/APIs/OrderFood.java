package com.oodwj_assignment.APIs;

import com.oodwj_assignment.Models.OrderFoods;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class OrderFood {
    private static final String FILE_NAME = "database/orderFoods.txt";

    public static Response<UUID> create(OrderFoods orderFood) {
        UUID orderFoodId = UUID.randomUUID();
        orderFood.setId(orderFoodId);

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            writer.println(orderFood);
            return Response.success("OrderFood created successfully", orderFoodId);
        } catch (IOException e) {
            return Response.failure("Failed to create orderFood: " + e.getMessage());
        }
    }

    public static Response<ArrayList<OrderFoods>> read(Map<String, Object> query) {
        ArrayList<OrderFoods> matchOrderFoods = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length != 8) {
                    continue;
                }

                OrderFoods orderFood = parseOrderFood(parts);

                if (orderFood != null) {
                    if (query.isEmpty() || match(query, orderFood).isSuccess()) {
                        matchOrderFoods.add(orderFood);
                    }
                }
            }
        } catch (IOException e) {
            return Response.failure("Failed to read orderFoods: " + e.getMessage());
        }

        if (matchOrderFoods.isEmpty()) {
            return Response.failure("No orderFoods match the query", matchOrderFoods);
        }

        return Response.success("OrderFoods read successfully", matchOrderFoods);
    }

    public static Response<Void> update(Map<String, Object> query, Map<String, Object> newValue) {
        Response<ArrayList<OrderFoods>> orderFoods = read(Map.of());

        if (orderFoods.isSuccess()) {
            for (OrderFoods orderFood : orderFoods.getData()) {
                Response<Void> matchRes = match(query, orderFood);

                if (matchRes.isSuccess()) {
                    for (Map.Entry<String, Object> entry : newValue.entrySet()) {
                        String attributeName = entry.getKey();
                        Object expectedValue = entry.getValue();

                        Response<Void> updateRes = orderFood.setAttributeValue(attributeName, expectedValue);

                        if (!updateRes.isSuccess()) {
                            return Response.failure(updateRes.getMessage());
                        }
                    }

                    Response<Void> saveRes = saveAllOrderFoods(orderFoods.getData());

                    if (saveRes.isSuccess()) {
                        return Response.success("OrderFood updated successfully");
                    } else {
                        return Response.failure(saveRes.getMessage());
                    }
                }
            }

            return Response.failure("OrderFood not found");
        } else {
            return Response.failure(orderFoods.getMessage());
        }
    }

    public static Response<Void> delete(Map<String, Object> query) {
        Response<ArrayList<OrderFoods>> orderFoods = read(Map.of());

        if (orderFoods.isSuccess()) {
            for (OrderFoods orderFood : orderFoods.getData()) {
                Response<Void> matchRes = match(query, orderFood);

                if (matchRes.isSuccess()) {
                    orderFoods.getData().remove(orderFood);
                    Response<Void> saveRes = saveAllOrderFoods(orderFoods.getData());

                    if (saveRes.isSuccess()) {
                        return Response.success("OrderFood deleted successfully");
                    } else {
                        return Response.failure(saveRes.getMessage());
                    }
                }
            }
            return Response.failure("OrderFoods not found");
        } else {
            return Response.failure(orderFoods.getMessage());
        }
    }

    public static Response<Void> deleteAll(Map<String, Object> query) {
        Response<ArrayList<OrderFoods>> orderFoods = read(Map.of());

        if (orderFoods.isSuccess()) {
            for (OrderFoods orderFood : orderFoods.getData()) {
                Response<Void> matchRes = match(query, orderFood);

                if (matchRes.isSuccess()) {
                    orderFoods.getData().remove(orderFood);
                }
            }
            Response<Void> saveRes = saveAllOrderFoods(orderFoods.getData());

            if (saveRes.isSuccess()) {
                return Response.success("OrderFoods deleted successfully");
            } else {
                return Response.failure(saveRes.getMessage());
            }
        } else {
            return Response.failure(orderFoods.getMessage());
        }
    }

    private static Response<Void> match(Map<String, Object> query, OrderFoods orderFood) {
        boolean match = true;

        for (Map.Entry<String, Object> entry : query.entrySet()) {
            String attributeName = entry.getKey();
            Object expectedValue = entry.getValue();

            Response<Object> actualValue = orderFood.getAttributeValue(attributeName);

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
            return Response.success("OrderFood found");
        } else {
            return Response.failure("OrderFood not found");
        }
    }

    private static Response<Void> saveAllOrderFoods(ArrayList<OrderFoods> orderFoods) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (OrderFoods orderFood : orderFoods) {
                writer.println(orderFood.toString());
            }
        } catch (IOException e) {
            return Response.failure(e.getMessage());
        }

        return Response.success("OrderFoods saved successfully");
    }

    private static OrderFoods parseOrderFood(String[] parts) {
        try {
            UUID orderFoodId = UUID.fromString(parts[0]);
            UUID foodId = UUID.fromString(parts[1]);
            UUID orderId = UUID.fromString(parts[2]);
            String foodName = parts[3];
            Double foodPrice = Double.parseDouble(parts[4]);
            Integer foodQuantity = Integer.parseInt(parts[5]);
            LocalDateTime updatedAt = LocalDateTime.parse(parts[6]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[7]);

            return new OrderFoods(orderFoodId, foodId, orderId, foodName, foodPrice, foodQuantity, updatedAt, createdAt);
        } catch (Exception e) {
            return null;
        }
    }
}
