package com.oodwj_assignment.Dao;

import com.oodwj_assignment.Dao.Base.AbstractDao;
import com.oodwj_assignment.Models.OrderFoods;

import java.time.LocalDateTime;
import java.util.UUID;

public class OrderFoodDaoImpl extends AbstractDao<OrderFoods> implements OrderFoodDao {

    private static final String FILE_NAME = "database/orderFoods.txt";

    public OrderFoodDaoImpl() {
        super(FILE_NAME);
    }

    public OrderFoods parse(String[] parts) {
        try {
            UUID orderFoodId = UUID.fromString(parts[0]);
            UUID foodId = UUID.fromString(parts[1]);
            UUID orderId = UUID.fromString(parts[2]);
            String foodName = parts[3];
            Double foodPrice = Double.parseDouble(parts[4]);
            Integer foodQuantity = Integer.parseInt(parts[5]);
            LocalDateTime updatedAt = LocalDateTime.parse(parts[6]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[7]);

            return new OrderFoods(orderFoodId, foodId, orderId, foodName, foodPrice, foodQuantity, updatedAt, createdAt);
        } catch (Exception e) {
            return null;
        }
    }
}
