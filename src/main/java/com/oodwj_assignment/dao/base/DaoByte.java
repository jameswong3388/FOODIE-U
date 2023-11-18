package com.oodwj_assignment.dao.base;

import com.oodwj_assignment.helpers.Response;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public interface DaoByte<T>{
    Response<UUID> create(T object);

    public Response<UUID> create2(T object);

    public Response<ArrayList<T>> read(Map<String, Object> query);

    public Response<ArrayList<T>> read2(Map<String, Object> query);
}
