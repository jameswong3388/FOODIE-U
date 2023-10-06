package com.oodwj_assignment.APIs;

import com.oodwj_assignment.Models.Stores;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class Store {
    private static final String FILE_NAME = "src/main/java/com/oodwj_assignment/Databases/stores.txt";

    public static Response<Void> create(Stores store) {
        UUID storeId = UUID.randomUUID();
        store.setStoreId(storeId);

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            writer.println(store);

            return Response.success("Store created successfully");
        } catch (IOException e) {
            return Response.failure("Failed to create store: " + e.getMessage());
        }
    }

    public static Response<ArrayList<Stores>> read(Map<String, Object> query) {
        ArrayList<Stores> stores = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");

                if (parts.length == 5) {
                    UUID storeId = UUID.fromString(parts[0]);
                    String name = parts[1];
                    UUID vendorId = UUID.fromString(parts[2]);
                    LocalDate updatedAt = LocalDate.parse(parts[3]);
                    LocalDate createdAt = LocalDate.parse(parts[4]);

                    stores.add(new Stores(storeId, name, vendorId, updatedAt, createdAt));
                }
            }
        } catch (IOException e) {
            return Response.failure("Failed to read stores: " + e.getMessage());
        }

        if (query.isEmpty()) {
            return Response.success("Stores read successfully", stores);
        }

        ArrayList<Stores> matchedStores = new ArrayList<>();

        for (Stores store : stores) {
            Response<Void> matchRes = match(query, store);

            if (matchRes.isSuccess()) {
                matchedStores.add(store);
            }
        }

        return Response.success("Stores read successfully", matchedStores);
    }

    public static Response<Void> update(Map<String, Object> query, Map<String, Object> newValue) {
        Response<ArrayList<Stores>> stores = read(query);

        System.out.println(stores.getData());

        if (stores.isSuccess()) {
            for (Stores store : stores.getData()) {
                for (Map.Entry<String, Object> entry : newValue.entrySet()) {
                    String attributeName = entry.getKey();
                    Object expectedValue = entry.getValue();

                    Response<Void> updateRes = store.setAttributeValue(attributeName, expectedValue);

                    if (!updateRes.isSuccess()) {
                        return Response.failure(updateRes.getMessage());
                    }
                }
            }

            Response<Void> saveRes = saveAllStores(stores.getData());

            if (saveRes.isSuccess()) {
                return Response.success("Stores updated successfully");
            } else {
                return Response.failure(saveRes.getMessage());
            }
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
}
