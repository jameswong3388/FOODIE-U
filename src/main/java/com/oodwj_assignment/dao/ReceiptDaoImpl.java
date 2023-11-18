package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.models.Receipts;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

public class ReceiptDaoImpl extends AbstractDao<Receipts> implements ReceiptDao {

    private static final File FILE = new File("database/receipts.txt");

    public ReceiptDaoImpl() {
        super(FILE);
    }

    public Receipts parse(String[] parts) {
        try {
            UUID receiptId = UUID.fromString(parts[0]);
            UUID userId = UUID.fromString(parts[1]);
            Double credit = Double.parseDouble(parts[2]);
            LocalDateTime updatedAt = LocalDateTime.parse(parts[3]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[4]);

            return new Receipts(receiptId, userId, credit, updatedAt, createdAt);
        } catch (Exception e) {
            return null;
        }
    }
}
