package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.models.Transactions;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionDaoImpl extends AbstractDao<Transactions> implements TransactionDao {
    private static final File FILE = new File("database/transactions.txt");

    public TransactionDaoImpl() {
        super(FILE);
    }

    public Transactions parse(String[] parts) {
        try {
            UUID transactionId = UUID.fromString(parts[0]);
            Double amount = Double.parseDouble(parts[1]);
            Transactions.transactionType type = Transactions.transactionType.valueOf(parts[2]);
            Transactions.transactionStatus status = Transactions.transactionStatus.valueOf(parts[3]);
            UUID payerId = UUID.fromString(parts[4]);
            UUID payeeId = UUID.fromString(parts[5]);
            LocalDateTime updatedAt = LocalDateTime.parse(parts[6]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[7]);

            return new Transactions(transactionId, amount, type, status, payerId, payeeId, updatedAt, createdAt);
        } catch (Exception e) {
            return null;
        }
    }
}
