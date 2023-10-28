package com.oodwj_assignment.Dao.Base;

import com.oodwj_assignment.Helpers.Response;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public interface Dao<T> {
    Response<UUID> create(T model);

    Response<ArrayList<T>> read(Map<String, Object> query);

    Response<Void> update(Map<String, Object> query, Map<String, Object> newValue);

    Response<Void> delete(Map<String, Object> query);

    Response<Void> saveAll(ArrayList<T> models);

    Response<Void> match(Map<String, Object> query, T model);
}
