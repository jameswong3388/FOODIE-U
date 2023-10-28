package com.oodwj_assignment.dao.base;

import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Model;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.lang.reflect.Method;

/**
 * Abstract class that implements core DAO CRUD methods for every entity
 *
 * @param <T> the type parameter
 */
public abstract class AbstractDao<T extends Model> implements Dao<T> {
    private final String fileName;

    /**
     * Instantiates a new Abstract dao.
     *
     * @param fileName the database file name
     */
    public AbstractDao(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Parses a line from the file into a T object
     *
     * @param parts a line from the file
     * @return a T object
     */
    public abstract T parse(String[] parts);

    public Response<UUID> create(T object) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            writer.println(object);

            return Response.success("Object created successfully", null);
        } catch (IOException e) {
            return Response.failure("Failed to create object: " + e.getMessage());
        }
    }

    public Response<ArrayList<T>> read(Map<String, Object> query) {
        ArrayList<T> matchedObjects = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");

                T object = parse(parts);

                if (query.isEmpty() || match(query, object).isSuccess()) {
                    matchedObjects.add(object);
                }
            }
        } catch (IOException e) {
            return Response.failure("Failed to read objects: " + e.getMessage());
        }

        if (matchedObjects.isEmpty()) {
            return Response.failure("No objects match the query", matchedObjects);
        }
        return Response.success("Objects read successfully", matchedObjects);
    }

    public Response<Void> update(Map<String, Object> query, Map<String, Object> newValue) {
        Response<ArrayList<T>> objects = read(Map.of());

        if (objects.isSuccess()) {
            for (T object : objects.getData()) {

                Response<Void> matchRes = match(query, object);

                if (matchRes.isSuccess()) {
                    for (Map.Entry<String, Object> entry : newValue.entrySet()) {
                        String attributeName = entry.getKey();
                        Object expectedValue = entry.getValue();

                        try {
                            Method setter = object.getClass().getMethod("set" + capitalizeFirstLetter(attributeName), expectedValue.getClass());
                            setter.invoke(object, expectedValue);
                        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                            return Response.failure("Error setting attribute value for " + attributeName);
                        }
                    }

                    Response<Void> saveRes = saveAll(objects.getData());

                    if (saveRes.isSuccess()) {
                        return Response.success("Object updated successfully");
                    } else {
                        return Response.failure(saveRes.getMessage());
                    }
                }
            }

            return Response.failure("Object not found");
        } else {
            return Response.failure(objects.getMessage());
        }
    }

    public Response<Void> delete(Map<String, Object> query) {
        Response<ArrayList<T>> objects = read(Map.of());

        if (objects.isSuccess()) {
            for (T object : objects.getData()) {
                Response<Void> matchRes = match(query, object);

                if (matchRes.isSuccess()) {
                    objects.getData().remove(object);

                    Response<Void> saveRes = saveAll(objects.getData());

                    if (saveRes.isSuccess()) {
                        return Response.success("Object deleted successfully");
                    } else {
                        return Response.failure(saveRes.getMessage());
                    }
                }
            }

            return Response.failure("Object not found");
        } else {
            return Response.failure(objects.getMessage());
        }
    }

    public Response<Void> match(Map<String, Object> query, T object) {
        boolean match = true;

        for (Map.Entry<String, Object> entry : query.entrySet()) {
            String attributeName = entry.getKey();
            Object expectedValue = entry.getValue();

            // Use reflection to dynamically invoke the getter method
            try {
                Method getter = object.getClass().getMethod("get" + capitalizeFirstLetter(attributeName));
                Object actualValue = getter.invoke(object);

                if (!Objects.equals(actualValue, expectedValue)) {
                    match = false;
                    break;
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                return Response.failure("Error getting attribute value for " + attributeName);
            }
        }
        if (match) {
            return Response.success("Object matches the query");
        } else {
            return Response.failure("Object does not match the query");
        }
    }

    public Response<Void> saveAll(ArrayList<T> objects) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (T object : objects) {
                writer.println(object);
            }
            return Response.success("Objects saved successfully");
        } catch (IOException e) {
            return Response.failure("Failed to save objects: " + e.getMessage());
        }
    }

    /**
     * Capitalize first letter of a string
     *
     * @param s the string
     * @return the string with first letter capitalized
     */
    private String capitalizeFirstLetter(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
