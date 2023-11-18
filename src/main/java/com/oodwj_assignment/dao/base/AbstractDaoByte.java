package com.oodwj_assignment.dao.base;

import com.oodwj_assignment.helpers.CustomObjectOutputStream;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Model;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

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
            UUID id = UUID.randomUUID();
            object.setId(id);

            try (FileOutputStream fos = new FileOutputStream(file, true)) {
                try (ObjectOutputStream oos = file.length() == 0 ? new ObjectOutputStream(fos) : new CustomObjectOutputStream(fos)) {
                    oos.writeObject(object);
                }
            }

            return Response.success("Object created successfully", id);
        } catch (Exception e) {
            return Response.failure("Failed to create object: " + e.getMessage());
        }
    }

    public Response<UUID> create2(T object) {
        try {
            DataOutputStream fos = new DataOutputStream(new FileOutputStream(file, true));

            UUID id = UUID.randomUUID();
            object.setId(id);

            fos.writeUTF(object.toString());
            return Response.success("Object created successfully", id);

        } catch (Exception e) {
            return Response.failure("Failed to create object: " + e.getMessage());
        }
    }


    public Response<ArrayList<T>> read(Map<String, Object> query) {
        ArrayList<T> matchedObjects = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            while (true) {
                try {
                    T object = (T) ois.readObject();

                    // Use the match method to check if the object matches the query
                    Response<Void> matchResponse = match(query, object);

                    if (matchResponse.isSuccess()) {
                        matchedObjects.add(object);
                    }

                } catch (EOFException e) {
                    // Reached end of the file
                    break;
                }
            }
        } catch (Exception e) {
            return Response.failure("Error reading objects from the file: " + e.getMessage());
        }

        if (matchedObjects.isEmpty()) {
            return Response.failure("No objects match the query", matchedObjects);
        }

        return Response.success("Objects read successfully", matchedObjects);
    }

    public Response<ArrayList<T>> read2(Map<String, Object> query) {
        ArrayList<T> matchedObjects = new ArrayList<>();

        try (DataInputStream ois = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            while (true) {
                try {
                    String line = ois.readUTF();

                    T object = parse(line.split(";"));

                    // Use the match method to check if the object matches the query
                    Response<Void> matchResponse = match(query, object);

                    if (matchResponse.isSuccess()) {
                        matchedObjects.add(object);
                    }

                } catch (EOFException e) {
                    // Reached end of the file
                    break;
                }
            }
        } catch (Exception e) {
            return Response.failure("Error reading objects from the file: " + e.getMessage());
        }

        if (matchedObjects.isEmpty()) {
            return Response.failure("No objects match the query", matchedObjects);
        }

        return Response.success("Objects read successfully", matchedObjects);
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