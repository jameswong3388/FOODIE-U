package com.oodwj_assignment.APIs;

import com.oodwj_assignment.Models.Orders;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class Order {
    private static final String FILE_NAME = "src/main/java/com/oodwj_assignment/Databases/orders.txt";

    public static Response<UUID> create(Orders order) {
        UUID orderId = UUID.randomUUID();
        order.setOrderId(orderId);

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            writer.println(order);
            return Response.success("Order created successfully", orderId);
        } catch (IOException e) {
            return Response.failure("Failed to create order: " + e.getMessage());
        }
    }

    public static Response<ArrayList<Orders>> read(Map<String, Object> query)  {
        ArrayList<Orders> orders = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 8) {
                    UUID orderId = UUID.fromString(parts[0]);
                    UUID userId = UUID.fromString(parts[1]);
                    Integer totalPrice = Integer.parseInt(parts[2]);
                    Integer totalQuantity = Integer.parseInt(parts[3]);
                    Orders.oderStatus status = Orders.oderStatus.valueOf(parts[4]); // Parse the role from the string.
                    Orders.orderType type = Orders.orderType.valueOf(parts[5]); // Parse the role from the string.
                    LocalDate updatedAt = LocalDate.parse(parts[6]);
                    LocalDate createdAt = LocalDate.parse(parts[7]);

                    orders.add(new Orders(orderId, userId, totalPrice, totalQuantity, status, type, updatedAt, createdAt));
                }
            }
        } catch (IOException e) {
            return Response.failure("Failed to read orders: " + e.getMessage());
        }

        if (query.isEmpty()) {
            return Response.success("Orders read successfully", orders);
        }

        ArrayList<Orders> matchOrders = new ArrayList<>();

        for (Orders order : orders) {
            Response<Void> matchRes = match(query, order);

            if (matchRes.isSuccess()) {
                matchOrders.add(order);
            }
        }

        return Response.success("Orders read successfully", matchOrders);
    }

    public static Response<Void> update(Map<String, Object> query, Map<String, Object> newValue) {
        Response<ArrayList<Orders>> orders = read(query);

        if (orders.isSuccess()) {
            for (Orders order : orders.getData()) {
                for (Map.Entry<String, Object> entry : newValue.entrySet()) {
                    String attributeName = entry.getKey();
                    Object expectedValue = entry.getValue();

                    Response<Void> updateRes = order.setAttributeValue(attributeName, expectedValue);

                    if (!updateRes.isSuccess()) {
                        return Response.failure(updateRes.getMessage());
                    }
                }
            }

            Response<Void> saveRes = saveAllOrders(orders.getData());

            if (saveRes.isSuccess()) {
                return Response.success("Orders updated successfully");
            } else {
                return Response.failure(saveRes.getMessage());
            }
        } else {
            return Response.failure(orders.getMessage());
        }
    }

    public static Response<Void> delete(Map<String, Object> query) {
        Response<ArrayList<Orders>> orders = read(Map.of());

        if (orders.isSuccess()) {
            for (Orders order : orders.getData()) {
                Response<Void> matchRes = match(query, order);

                if (matchRes.isSuccess()) {
                    orders.getData().remove(order);
                    Response<Void> saveRes = saveAllOrders(orders.getData());

                    if (saveRes.isSuccess()) {
                        return Response.success("Order deleted successfully");
                    } else {
                        return Response.failure(saveRes.getMessage());
                    }
                }
            }
            return Response.failure("Order not found");
        } else {
            return Response.failure(orders.getMessage());
        }
    }

    public static Response<Void> deleteAll(Map<String, Object> query) {
        Response<ArrayList<Orders>> orders = read(Map.of());

        if (orders.isSuccess()) {
            for (Orders order : orders.getData()) {
                Response<Void> matchRes = match(query, order);

                if (matchRes.isSuccess()) {
                    orders.getData().remove(order);
                }
            }
            Response<Void> saveRes = saveAllOrders(orders.getData());

            if (saveRes.isSuccess()) {
                return Response.success("Orders deleted successfully");
            } else {
                return Response.failure(saveRes.getMessage());
            }
        } else {
            return Response.failure(orders.getMessage());
        }
    }

    private static Response<Void> match(Map<String, Object> query, Orders order) {
        boolean match = true;

        for (Map.Entry<String, Object> entry : query.entrySet()) {
            String attributeName = entry.getKey();
            Object expectedValue = entry.getValue();

            Response<Object> actualValue = order.getAttributeValue(attributeName);

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
            return Response.success("Order found");
        } else {
            return Response.failure("Order not found");
        }
    }

    private static Response<Void> saveAllOrders(ArrayList<Orders> orders) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Orders order : orders) {
                writer.println(order.toString());
            }
        } catch (IOException e) {
            return Response.failure(e.getMessage());
        }

        return Response.success("Orders saved successfully");
    }
}
