package com.oodwj_assignment.APIs;

import com.oodwj_assignment.Models.Stores;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class Store {
    private static final String FILE_NAME = "database/stores.txt";

    public static Response<UUID> create(Stores store) {
        UUID storeId = UUID.randomUUID();
        store.setId(storeId);

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            writer.println(store);

            return Response.success("Store created successfully", storeId);
        } catch (IOException e) {
            return Response.failure("Failed to create store: " + e.getMessage());
        }
    }

    public static Response<ArrayList<Stores>> read(Map<String, Object> query) {
        ArrayList<Stores> matchedStores = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");

                if (parts.length != 5) {
                    continue;
                }

                Stores store = parseStore(parts);

                if (store != null) {
                    if (query.isEmpty() || match(query, store).isSuccess()) {
                        matchedStores.add(store);
                    }
                }
            }
        } catch (IOException e) {
            return Response.failure("Failed to read stores: " + e.getMessage());
        }

        if (matchedStores.isEmpty()) {
            return Response.failure("No stores match the query", matchedStores);
        }

        return Response.success("Stores read successfully", matchedStores);
    }

    public static Response<Void> update(Map<String, Object> query, Map<String, Object> newValue) {
        Response<ArrayList<Stores>> stores = read(Map.of());

        if (stores.isSuccess()) {
            for (Stores store : stores.getData()) {
                Response<Void> matchRes = match(query, store);

                if (matchRes.isSuccess()) {
                    for (Map.Entry<String, Object> entry : newValue.entrySet()) {
                        String attributeName = entry.getKey();
                        Object expectedValue = entry.getValue();

                        Response<Void> updateRes = store.setAttributeValue(attributeName, expectedValue);

                        if (!updateRes.isSuccess()) {
                            return Response.failure(updateRes.getMessage());
                        }
                    }

                    Response<Void> saveRes = saveAllStores(stores.getData());

                    if (saveRes.isSuccess()) {
                        return Response.success("Store updated successfully");
                    } else {
                        return Response.failure(saveRes.getMessage());
                    }
                }
            }

            return Response.failure("Store not found");
        } else {
            return Response.failure(stores.getMessage());
        }
    }

    public static Response<Void> delete(Map<String, Object> query) {
        Response<ArrayList<Stores>> stores = read(Map.of());

        if (stores.isSuccess()) {
            for (Stores store : stores.getData()) {
                Response<Void> matchRes = match(query, store);

                if (matchRes.isSuccess()) {
                    stores.getData().remove(store);
                    Response<Void> saveRes = saveAllStores(stores.getData());

                    if (saveRes.isSuccess()) {
                        return Response.success("Store deleted successfully");
                    } else {
                        return Response.failure(saveRes.getMessage());
                    }
                }
            }
            return Response.failure("Store not found");

        } else {
            return Response.failure(stores.getMessage());
        }
    }


    private static Response<Void> match(Map<String, Object> query, Stores store) {
        boolean match = true;

        for (Map.Entry<String, Object> entry : query.entrySet()) {
            String attributeName = entry.getKey();
            Object expectedValue = entry.getValue();

            Response<Object> actualValue = store.getAttributeValue(attributeName);

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
            return Response.success("Store found");
        } else {
            return Response.failure("Store not found");
        }
    }

    private static Response<Void> saveAllStores(ArrayList<Stores> stores) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Stores store : stores) {
                writer.println(store);
            }
            return Response.success("Stores saved successfully");
        } catch (IOException e) {
            return Response.failure("Failed to save stores: " + e.getMessage());
        }
    }

    private static Stores parseStore(String[] parts) {
        try {
            UUID storeId = UUID.fromString(parts[0]);
            String name = parts[1];
            UUID vendorId = UUID.fromString(parts[2]);
            LocalDateTime updatedAt = LocalDateTime.parse(parts[3]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[4]);

            return new Stores(storeId, name, vendorId, updatedAt, createdAt);
        } catch (Exception e) {
            return null;
        }
    }
}
