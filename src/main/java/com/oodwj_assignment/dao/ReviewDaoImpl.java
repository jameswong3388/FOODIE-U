package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.AbstractDao;

import com.oodwj_assignment.models.Reviews;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

public class ReviewDaoImpl extends AbstractDao<Reviews> implements ReviewDao {

    private static final File FILE = new File("database/reviews.dat");

    public ReviewDaoImpl() {
        super(FILE);
    }
}
