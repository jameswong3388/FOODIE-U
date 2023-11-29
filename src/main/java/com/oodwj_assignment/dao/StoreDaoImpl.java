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
}
