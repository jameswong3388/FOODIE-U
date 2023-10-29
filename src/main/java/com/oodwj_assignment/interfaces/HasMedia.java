package com.oodwj_assignment.interfaces;


import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Medias;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

/***
 * Interface for media operations
 */
public interface HasMedia {
    /***
     * Add media to a defined disk, and record the media in the database
     * @param file uploaded file
     * @param media media object
     * @return null
     */
    Response<Void> addMedia(File file, Medias media);

    /***
     * Get all media of a model
     *
     * @param modelUUID model UUID
     * @return file paths
     */
    Response<ArrayList<String>> getMedia(UUID modelUUID);

    /***
     * Get the first media of a model
     *
     * @param modelUUID model UUID
     * @return file path
     */
    Response<String> getFirstMedia(UUID modelUUID);

    /***
     * Remove all media of a model
     *
     * @param modelUUID model UUIDœ
     * @return null
     */
    Response<Void> removeMedia(UUID modelUUID);

    /***
     * Check if a model has media
     * @param modelUUID model UUID
     * @return true if the model has media, false otherwise
     */
    Response<Boolean> hasMedia(UUID modelUUID);
}
