package com.oodwj_assignment.dao.base;

import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Model;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public abstract class AbstractDaoByte<T extends Model> implements DaoByte<T> {
    private final File file;

    private static final String CWD = System.getProperty("user.dir");
    private static final String MEDIA_DIR = CWD + "/src/main/resources/medias/";

    /**
     * Instantiates a new Abstract dao.
     *
     * @param file the database file name
     */
    public AbstractDaoByte(File file) {
        this.file = file;
    }

    /**
     * Parses a line from the FILE into a T object
     *
     * @param parts a line from the FILE
     * @return a T object
     */
    public abstract T parse(String[] parts);

    public Response<UUID> create(T object) {
        try {
            ArrayList<T> list;
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
                list = (ArrayList<T>) objectInputStream.readObject();
            } catch (EOFException e) {
                list = new ArrayList<>();
            }

            UUID id = UUID.randomUUID();
            object.setId(id);

            list.add(object);

            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
                objectOutputStream.writeObject(list);
            }

            return Response.success("Object created successfully", id);
        } catch (Exception e) {
            return Response.failure("Failed to create object: " + e.getMessage());
        }
    }

    public Response<ArrayList<T>> read(Map<String, Object> query) {
        ArrayList<T> objects;

        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {

            objects = (ArrayList<T>) ois.readObject();

            if (query.isEmpty()) {
                return Response.success("Objects read successfully", objects);
            }

            for (Iterator<T> iterator = objects.iterator(); iterator.hasNext();) {
                T object = iterator.next();
                Response<Void> matchResponse = match(query, object);

                if (!matchResponse.isSuccess()) {
                    iterator.remove();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.failure("Error reading objects from the file: " + e.getMessage());
        }

        if (objects.isEmpty()) {
            return Response.failure("No objects match the query", objects);
        }

        return Response.success("Objects read successfully", objects);
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

    public Response<Void> deleteOne(Map<String, Object> query) {
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

    public Response<Void> deleteAll(Map<String, Object> query) {
        Response<ArrayList<T>> objects = read(Map.of());
        ArrayList<T> toRemove = new ArrayList<>();

        if (objects.isSuccess()) {
            for (T object : objects.getData()) {
                Response<Void> matchRes = match(query, object);

                if (matchRes.isSuccess()) {
                    toRemove.add(object); // to avoid ConcurrentModificationException
                }
            }

            objects.getData().removeAll(toRemove);
            Response<Void> saveRes = saveAll(objects.getData());

            if (saveRes.isSuccess()) {
                return Response.success("Objects deleted successfully");
            } else {
                return Response.failure(saveRes.getMessage());
            }
        } else {
            return Response.failure(objects.getMessage());
        }
    }

    public Response<Void> saveAll(ArrayList<T> objects) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            objectOutputStream.writeObject(objects);
            return Response.success("Objects saved successfully");
        } catch (Exception e) {
            return Response.failure("Error saving objects to the file: " + e.getMessage());
        }
    }



    public Response<Void> match(Map<String, Object> query, T object) {
        boolean match = query.entrySet()
                .parallelStream()
                .allMatch(entry -> {
                    String attributeName = entry.getKey();
                    Object expectedValue = entry.getValue();

                    try {
                        Method getter = object.getClass().getMethod("get" + capitalizeFirstLetter(attributeName));
                        Object actualValue = getter.invoke(object);

                        return Objects.equals(actualValue, expectedValue);

                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException("Error getting attribute value for " + attributeName, e);
                    }
                });

        if (match) {
            return Response.success("Object matches the query");
        } else {
            return Response.failure("Object does not match the query");
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