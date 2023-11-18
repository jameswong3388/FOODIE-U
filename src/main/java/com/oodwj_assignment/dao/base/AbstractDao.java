package com.oodwj_assignment.dao.base;

import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Medias;
import com.oodwj_assignment.models.Model;

import javax.imageio.ImageIO;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.lang.reflect.Method;

/**
 * Abstract class that implements core DAO CRUD methods for every entity
 *
 * @param <T> the type parameter
 */
public abstract class AbstractDao<T extends Model> implements Dao<T> {
    private final File FILE;
    private static final String CWD = System.getProperty("user.dir");
    private static final String MEDIA_DIR = CWD + "/src/main/resources/medias/";

    /**
     * Instantiates a new Abstract dao.
     *
     * @param file the database FILE name
     */
    public AbstractDao(File file) {
        this.FILE = file;
    }

    /**
     * Parses a line from the FILE into a T object
     *
     * @param parts a line from the FILE
     * @return a T object
     */
    public abstract T parse(String[] parts);

    /***
     * Creates object in database
     *
     * @param object object to be created
     * @return UUID of created object
     */
    public Response<UUID> create(T object) {

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE, true))) {
            UUID id = UUID.randomUUID();
            object.setId(id);

            String objectString = object.toString();

            writer.println(objectString);
            return Response.success("Object created successfully", id);
        } catch (IOException e) {
            return Response.failure("Failed to create object: " + e.getMessage());
        }
    }

    /***
     * Reads object from database
     *
     * @param query query to be executed
     * @return A list of objects
     */
    public Response<ArrayList<T>> read(Map<String, Object> query) {
        ArrayList<T> matchedObjects = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
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

    /***
     * Updates object in database
     *
     * @param query query to be executed
     * @param newValue new value of object
     * @return UUID of updated object
     */
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

    /***
     * Delete one object from database
     *
     * @param query query to be executed
     * @return Void returned a response object with status, message and data
     */
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

    /***
     * Delete all objects from database
     *
     * @param query query to be executed
     * @return Void returned a response object with status, message and data
     */
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

    /***
     * Matches model from a given query
     *
     * @param query query to be executed
     * @param object object to be matched
     * @return Void returned a response object with status, message and data
     */
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

    /***
     * Save all given objects to database
     *
     * @param objects objects to be saved
     * @return Void returned a response object with status, message and data
     */
    public Response<Void> saveAll(ArrayList<T> objects) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE))) {
            for (T object : objects) {
                writer.println(object.toString());
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

    /***
     * Add media to a defined disk, and record the media in the database
     * @param file uploaded FILE
     * @param media media object
     * @return null
     */
    public Response<Void> addMedia(File file, Medias media) {
        File dest = new File(MEDIA_DIR + media.getDisk());

        media.setModel(modelDescriptor());
        media.setSize(DaoFactory.getMediaDao().getMediaSizeMegaBytes(file));
        try {
            Integer height = ImageIO.read(file).getHeight();
            Integer width = ImageIO.read(file).getWidth();
            media.setHeight(height);
            media.setWidth(width);
        } catch (Exception e) {
            return Response.failure("Failed to get media height and width: " + e.getMessage());
        }

        Response<UUID> res = DaoFactory.getMediaDao().create(media);

        if (!res.isSuccess()) {
            System.out.println("Failed to save media to database: " + res.getMessage());
        }

        try {
            if (!dest.exists()) {
                dest.mkdirs();
            }

            java.nio.file.Files.copy(file.toPath(), new File(dest + "/" + res.getData().toString() + media.getMimeType()).toPath());

        } catch (IOException | NullPointerException e) {
            return Response.failure("Failed to save media: " + e.getMessage());
        }
        return Response.success("Media saved successfully");
    }

    /***
     * Get all media of a model
     *
     * @param modelUUID model UUID
     * @return FILE paths
     */
    public Response<ArrayList<String>> getMedia(UUID modelUUID) {
        Response<ArrayList<Medias>> res = DaoFactory.getMediaDao().read(Map.of("model", modelDescriptor(), "modelUUID", modelUUID));
        ArrayList<String> file_paths = new ArrayList<>();

        if (res.isSuccess()) {
            for (Medias media : res.getData()) {
                String file_path = MEDIA_DIR + media.getDisk() + "/" + media.getId() + media.getMimeType();
                file_paths.add(file_path);
            }
            return Response.success("Media retrieved successfully", file_paths);
        } else {
            return Response.failure("Failed to get media: " + res.getMessage());
        }
    }

    /***
     * Get the first media of a model
     *
     * @param modelUUID model UUID
     * @return FILE path
     */
    public Response<String> getFirstMedia(UUID modelUUID) {
        Response<ArrayList<Medias>> res = DaoFactory.getMediaDao().read(Map.of("model", modelDescriptor(), "modelUUID", modelUUID));

        if (res.isSuccess()) {
            String file_path = MEDIA_DIR + res.getData().get(0).getDisk() + "/" + res.getData().get(0).getId() + res.getData().get(0).getMimeType();
            return Response.success("Media retrieved successfully", file_path);
        } else {
            return Response.failure("Failed to get media: " + res.getMessage());
        }
    }

    /***
     * Remove all media of a model
     *
     * @param modelUUID model UUID
     * @return null
     */
    public Response<Void> removeMedia(UUID modelUUID) {
        Response<ArrayList<Medias>> readRes = DaoFactory.getMediaDao().read(Map.of("model", modelDescriptor(), "modelUUID", modelUUID));

        if (readRes.isSuccess()) {
            Response<Void> deleteRes = DaoFactory.getMediaDao().deleteAll(Map.of("model", modelDescriptor(), "modelUUID", modelUUID));

            if (!deleteRes.isSuccess()) {
                return Response.failure("Failed to delete media: " + deleteRes.getMessage());
            }

            for (Medias media : readRes.getData()) {
                String file_path = MEDIA_DIR + media.getDisk() + "/" + media.getId() + media.getMimeType();
                File file = new File(file_path);

                if (!file.delete()) {
                    return Response.failure("Failed to delete media: " + file_path);
                }
            }

            return Response.success("Media deleted successfully");
        } else {
            return Response.failure("Failed to get media: " + readRes.getMessage());
        }
    }

    /***
     * Check if a model has media
     * @param modelUUID model UUID
     * @return true if the model has media, false otherwise
     */
    public Response<Boolean> hasMedia(UUID modelUUID) {
        Response<ArrayList<Medias>> res = DaoFactory.getMediaDao().read(Map.of("model", modelDescriptor(), "modelUUID", modelUUID));

        if (res.isSuccess()) {
            return Response.success("Media retrieved successfully", !res.getData().isEmpty());
        } else {
            return Response.failure("Failed to get media: " + res.getMessage());
        }
    }

    /***
     * Remove "DaoImpl" from the class name
     *
     * @return model descriptor
     */
    private String modelDescriptor() {
        String className = this.getClass().getSimpleName();
        return "/models/" + className.substring(0, className.length() - 7);
    }
}
