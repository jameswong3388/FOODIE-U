package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.models.Stores;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

public class StoreDaoImpl extends AbstractDao<Stores> implements StoreDao {

    private static final File FILE = new File("database/stores.dat");

    public StoreDaoImpl() {
        super(FILE);
    }

    public Stores parse(String[] parts) {
        try {
            UUID storeId = UUID.fromString(parts[0]);
            String name = parts[1];
            UUID vendorId = UUID.fromString(parts[2]);
            String description = parts[3];
            Stores.storeCategory category = Stores.storeCategory.valueOf(parts[4]);
            LocalDateTime updatedAt = LocalDateTime.parse(parts[5]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[6]);

            return new Stores(storeId, name, vendorId, description, category, updatedAt, createdAt);
        } catch (Exception e) {
            return null;
        }
    }
}
