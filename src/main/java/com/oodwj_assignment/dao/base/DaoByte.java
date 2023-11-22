package com.oodwj_assignment.dao.base;

import com.oodwj_assignment.helpers.Response;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public interface DaoByte<T> {
    Response<UUID> create(T object);

    public Response<ArrayList<T>> read(Map<String, Object> query);

    Response<Void> update(Map<String, Object> query, Map<String, Object> newValue);

    Response<Void> deleteOne(Map<String, Object> query);

    Response<Void> deleteAll(Map<String, Object> query);

    Response<Void> saveAll(ArrayList<T> objects);
}
