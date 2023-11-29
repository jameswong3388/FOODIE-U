package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.models.Foods;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

public class FoodDaoImpl extends AbstractDao<Foods> implements FoodDao {
    private static final File FILE = new File("database/foods.dat");

    public FoodDaoImpl() {
        super(FILE);
    }
}
