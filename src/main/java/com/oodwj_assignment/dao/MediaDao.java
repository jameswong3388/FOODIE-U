package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.Dao;
import com.oodwj_assignment.models.Medias;

import java.util.Optional;

public interface MediaDao extends Dao<Medias> {
    /***
     * Get extension from file name
     * Special case:
     * 1. No extension – this method will return an empty String
     * 2. Only extension – this method will return the String after the dot, e.g. “gitignore”
     *
     * @param filename filename to retrieve the extension from
     * @return String extension
     */
    String getExtensionByStringHandling(String filename);
}
