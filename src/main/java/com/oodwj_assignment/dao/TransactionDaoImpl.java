package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.models.Transactions;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionDaoImpl extends AbstractDao<Transactions> implements TransactionDao {
    private static final File FILE = new File("database/transactions.dat");

    public TransactionDaoImpl() {
        super(FILE);
    }
}
