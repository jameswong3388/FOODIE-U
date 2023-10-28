package com.oodwj_assignment.Dao;

import com.oodwj_assignment.Dao.Base.AbstractDao;
import com.oodwj_assignment.Models.Transactions;

import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionDaoImpl extends AbstractDao<Transactions> implements TransactionDao {
    private static final String FILE_NAME = "database/transactions.txt";

    public TransactionDaoImpl() {
        super(FILE_NAME);
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
