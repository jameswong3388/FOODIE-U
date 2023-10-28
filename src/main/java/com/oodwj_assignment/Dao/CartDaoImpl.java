package com.oodwj_assignment.Dao;

import com.oodwj_assignment.Dao.Base.AbstractDao;
import com.oodwj_assignment.Models.Carts;

import java.time.LocalDateTime;
import java.util.UUID;

public class CartDaoImpl extends AbstractDao<Carts> implements CartDao {

    private static final String FILE_NAME = "database/carts.txt";

    public CartDaoImpl() {
        super(FILE_NAME);
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
