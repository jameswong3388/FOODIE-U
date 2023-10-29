package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.models.Stores;

import java.time.LocalDateTime;
import java.util.UUID;

public class StoreDaoImpl extends AbstractDao<Stores> implements StoreDao {

    private static final String FILE_NAME = "database/stores.txt";

    public StoreDaoImpl() {
        super(FILE_NAME);
    }

    public Stores parse(String[] parts) {
        try {
            UUID storeId = UUID.fromString(parts[0]);
            String name = parts[1];
            UUID vendorId = UUID.fromString(parts[2]);
            LocalDateTime updatedAt = LocalDateTime.parse(parts[3]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[4]);

            return new Stores(storeId, name, vendorId, updatedAt, createdAt);
        } catch (Exception e) {
            return null;
        }
    }
}
