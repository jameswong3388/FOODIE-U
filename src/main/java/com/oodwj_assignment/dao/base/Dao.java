package com.oodwj_assignment.dao.base;

import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.interfaces.HasMedia;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

/**
 * Root interface for all DAO classes
 *
 * @param <T> the type parameter
 */
public interface Dao<T> extends HasMedia {
    /***
     * Creates object in database
     *
     * @param object object to be created
     * @return UUID of created object
     */
    Response<UUID> create(T object);

    /***
     * Reads object from database
     *
     * @param query query to be executed
     * @return A list of objects
     */
    Response<ArrayList<T>> read(Map<String, Object> query);

    /***
     * Updates object in database
     *
     * @param query query to be executed
     * @param newValue new value of object
     * @return UUID of updated object
     */
    Response<Void> update(Map<String, Object> query, Map<String, Object> newValue);

    /***
     * Delete one object from database
     *
     * @param query query to be executed
     * @return Void returned a response object with status, message and data
     */
    Response<Void> deleteOne(Map<String, Object> query);

    /***
     * Delete all objects from database
     *
     * @param query query to be executed
     * @return Void returned a response object with status, message and data
     */
    Response<Void> deleteAll(Map<String, Object> query);

    /***
     * Save all given objects to database
     *
     * @param objects objects to be saved
     * @return Void returned a response object with status, message and data
     */
    Response<Void> saveAll(ArrayList<T> objects);

    /***
     * Matches model from a given query
     *
     * @param query query to be executed
     * @param object object to be matched
     * @return Void returned a response object with status, message and data
     */
    Response<Void> match(Map<String, Object> query, T object);
}
