package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.models.Receipts;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

public class ReceiptDaoImpl extends AbstractDao<Receipts> implements ReceiptDao {

    private static final File FILE = new File("database/receipts.dat");

    public ReceiptDaoImpl() {
        super(FILE);
    }
}
