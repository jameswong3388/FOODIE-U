package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.models.Wallets;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

public class WalletDaoImpl extends AbstractDao<Wallets> implements WalletDao {

    private static final File FILE = new File("database/wallets.txt");

    public WalletDaoImpl() {
        super(FILE);
    }

    public Wallets parse(String[] parts) {
        try {
            UUID walletId = UUID.fromString(parts[0]);
            UUID userId = UUID.fromString(parts[1]);
            double balance = Double.parseDouble(parts[2]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[3]);
            LocalDateTime updatedAt = LocalDateTime.parse(parts[4]);

            return new Wallets(walletId, userId, balance, createdAt, updatedAt);
        } catch (Exception e) {
            return null;
        }
    }
}
