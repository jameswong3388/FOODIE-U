package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.models.Wallets;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

public class WalletDaoImpl extends AbstractDao<Wallets> implements WalletDao {

    private static final File FILE = new File("database/wallets.dat");

    public WalletDaoImpl() {
        super(FILE);
    }
}
