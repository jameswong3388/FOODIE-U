package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.models.Carts;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

public class CartDaoImpl extends AbstractDao<Carts> implements CartDao {

    private static final File FILE = new File("database/carts.txt");

    public CartDaoImpl() {
        super(FILE);
    }

    public Carts parse(String[] parts) {
        try {
            UUID cartId = UUID.fromString(parts[0]);
            UUID userId = UUID.fromString(parts[1]);
            UUID foodId = UUID.fromString(parts[2]);
            String foodName = parts[3];
            String foodPrice = parts[4];
            Integer foodQuantity = Integer.parseInt(parts[5]);
            LocalDateTime updatedAt = LocalDateTime.parse(parts[6]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[7]);

            return new Carts(cartId, userId, foodId, foodName, foodPrice, foodQuantity, updatedAt, createdAt);
        } catch (Exception e) {
            return null;
        }
    }
}
