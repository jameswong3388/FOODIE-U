package com.oodwj_assignment.Dao.Base;

import com.oodwj_assignment.Helpers.Response;
import com.oodwj_assignment.Models.Model;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.lang.reflect.Method;

public abstract class AbstractDao<T extends Model> implements Dao<T> {
    private final String fileName;

    public AbstractDao(String fileName) {
        this.fileName = fileName;
    }

    public abstract T parse(String[] parts);

    public Response<UUID> create(T model) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            writer.println(model);

            return Response.success("Model created successfully", null);
        } catch (IOException e) {
            return Response.failure("Failed to create model: " + e.getMessage());
        }
    }

    public Response<ArrayList<T>> read(Map<String, Object> query) {
        ArrayList<T> matchedModels = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");

                T model = parse(parts);

                if (query.isEmpty() || match(query, model).isSuccess()) {
                    matchedModels.add(model);
                }
            }
        } catch (IOException e) {
            return Response.failure("Failed to read models: " + e.getMessage());
        }

        if (matchedModels.isEmpty()) {
            return Response.failure("No models match the query", matchedModels);
        }
        return Response.success("Models read successfully", matchedModels);
    }

    public Response<Void> update(Map<String, Object> query, Map<String, Object> newValue) {
        Response<ArrayList<T>> models = read(Map.of());

        if (models.isSuccess()) {
            for (T model : models.getData()) {

                Response<Void> matchRes = match(query, model);

                if (matchRes.isSuccess()) {
                    for (Map.Entry<String, Object> entry : newValue.entrySet()) {
                        String attributeName = entry.getKey();
                        Object expectedValue = entry.getValue();

                        try {
                            Method setter = model.getClass().getMethod("set" + capitalizeFirstLetter(attributeName), expectedValue.getClass());
                            setter.invoke(model, expectedValue);

                            System.out.println(model);
                        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                            return Response.failure("Error setting attribute value for " + attributeName);
                        }
                    }

                    Response<Void> saveRes = saveAll(models.getData());

                    if (saveRes.isSuccess()) {
                        return Response.success("Model updated successfully");
                    } else {
                        return Response.failure(saveRes.getMessage());
                    }
                }
            }

            return Response.failure("Model not found");
        } else {
            return Response.failure(models.getMessage());
        }
    }

    public Response<Void> delete(Map<String, Object> query) {
        Response<ArrayList<T>> models = read(Map.of());

        if (models.isSuccess()) {
            for (T model : models.getData()) {
                Response<Void> matchRes = match(query, model);

                if (matchRes.isSuccess()) {
                    models.getData().remove(model);

                    Response<Void> saveRes = saveAll(models.getData());

                    if (saveRes.isSuccess()) {
                        return Response.success("Model deleted successfully");
                    } else {
                        return Response.failure(saveRes.getMessage());
                    }
                }
            }

            return Response.failure("Model not found");
        } else {
            return Response.failure(models.getMessage());
        }
    }

    public Response<Void> match(Map<String, Object> query, T model) {
        boolean match = true;

        for (Map.Entry<String, Object> entry : query.entrySet()) {
            String attributeName = entry.getKey();
            Object expectedValue = entry.getValue();

            // Use reflection to dynamically invoke the getter method
            try {
                Method getter = model.getClass().getMethod("get" + capitalizeFirstLetter(attributeName));
                Object actualValue = getter.invoke(model);

                if (!Objects.equals(actualValue, expectedValue)) {
                    match = false;
                    break;
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                return Response.failure("Error getting attribute value for " + attributeName);
            }
        }
        if (match) {
            return Response.success("Model matches the query");
        } else {
            return Response.failure("Model does not match the query");
        }
    }

    /*
     * Helper method to capitalize the first letter of a string
     */
    private String capitalizeFirstLetter(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    public Response<Void> saveAll(ArrayList<T> models) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (T model : models) {
                writer.println(model);
            }
            return Response.success("Models saved successfully");
        } catch (IOException e) {
            return Response.failure("Failed to save models: " + e.getMessage());
        }
    }
}
