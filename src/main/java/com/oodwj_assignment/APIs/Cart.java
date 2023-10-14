package com.oodwj_assignment.APIs;

import com.oodwj_assignment.Models.Carts;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class Cart {
    private static final String FILE_NAME = "database/cart.txt";

    public static Response<UUID> create(Carts cart) {
        UUID cartId = UUID.randomUUID();
        cart.setCartId(cartId);

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            writer.println(cart);

            return Response.success("Cart created successfully", cartId);
        } catch (IOException e) {
            return Response.failure("Failed to create cart: " + e.getMessage());
        }
    }

    public static Response<ArrayList<Carts>> read(Map<String, Object> query) {
        ArrayList<Carts> matchedCarts = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");

                if (parts.length != 8) {
                    continue;
                }

                Carts cart = parseCart(parts);

                if (cart != null) {
                    if (query.isEmpty() || match(query, cart).isSuccess()) {
                        matchedCarts.add(cart);
                    }
                }
            }
        } catch (IOException e) {
            return Response.failure("Failed to read Carts: " + e.getMessage());
        }

        if (matchedCarts.isEmpty()) {
            return Response.failure("No Carts match the query", matchedCarts);
        }

        return Response.success("Carts read successfully", matchedCarts);
    }

    public static Response<Void> update(Map<String, Object> query, Map<String, Object> newValue) {
        Response<ArrayList<Carts>> carts = read(Map.of());

        if (carts.isSuccess()) {
            for (Carts cart : carts.getData()) {
                Response<Void> matchRes = match(query, cart);

                if (matchRes.isSuccess()) {
                    for (Map.Entry<String, Object> entry : newValue.entrySet()) {
                        String attributeName = entry.getKey();
                        Object expectedValue = entry.getValue();

                        Response<Void> updateRes = cart.setAttributeValue(attributeName, expectedValue);

                        if (!updateRes.isSuccess()) {
                            return Response.failure(updateRes.getMessage());
                        }
                    }

                    Response<Void> saveRes = saveAllCarts(carts.getData());

                    if (saveRes.isSuccess()) {
                        return Response.success("Cart updated successfully");
                    } else {
                        return Response.failure(saveRes.getMessage());
                    }
                }
            }

            return Response.failure("Cart not found");
        } else {
            return Response.failure(carts.getMessage());
        }
    }

    public static Response<Void> delete(Map<String, Object> query) {
        Response<ArrayList<Carts>> carts = read(Map.of());

        if (carts.isSuccess()) {
            for (Carts cart : carts.getData()) {
                Response<Void> matchRes = match(query, cart);

                if (matchRes.isSuccess()) {
                    carts.getData().remove(cart);
                    Response<Void> saveRes = saveAllCarts(carts.getData());

                    if (saveRes.isSuccess()) {
                        return Response.success("Cart deleted successfully");
                    } else {
                        return Response.failure(saveRes.getMessage());
                    }
                }
            }
            return Response.failure("Cart not found");

        } else {
            return Response.failure(carts.getMessage());
        }
    }


    private static Response<Void> match(Map<String, Object> query, Carts cart) {
        boolean match = true;

        for (Map.Entry<String, Object> entry : query.entrySet()) {
            String attributeName = entry.getKey();
            Object expectedValue = entry.getValue();

            Response<Object> actualValue = cart.getAttributeValue(attributeName);

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
            return Response.success("Cart found");
        } else {
            return Response.failure("Cart not found");
        }
    }

    private static Response<Void> saveAllCarts(ArrayList<Carts> Carts) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Carts cart : Carts) {
                writer.println(cart);
            }
            return Response.success("Carts saved successfully");
        } catch (IOException e) {
            return Response.failure("Failed to save Carts: " + e.getMessage());
        }
    }

    private static Carts parseCart(String[] parts) {
        try {
            UUID cartId = UUID.fromString(parts[0]);
            UUID userId = UUID.fromString(parts[1]);
            UUID foodId = UUID.fromString(parts[2]);
            String foodName = parts[3];
            String foodPrice = parts[4];
            Integer foodQuantity = Integer.parseInt(parts[5]);
            LocalDateTime updatedAt = LocalDateTime.parse(parts[6]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[7]);

            return new Carts(cartId, userId, foodId, foodName, foodPrice, foodQuantity, updatedAt, createdAt);
        } catch (Exception e) {
            return null;
        }
    }
}
