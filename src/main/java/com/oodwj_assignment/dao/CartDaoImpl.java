package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.models.Carts;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

public class CartDaoImpl extends AbstractDao<Carts> implements CartDao {

    private static final File FILE = new File("database/carts.dat");

    public CartDaoImpl() {
        super(FILE);
    }
}
