package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.models.Orders;

import java.time.LocalDateTime;
import java.util.UUID;

public class OrderDaoImpl extends AbstractDao<Orders> implements OrderDao {

    private static final String FILE_NAME = "database/orders.txt";

    public OrderDaoImpl() {
        super(FILE_NAME);
    }

    public Orders parse(String[] parts) {
        try {
            UUID orderId = UUID.fromString(parts[0]);
            UUID userId = UUID.fromString(parts[1]);
            Double totalPrice = Double.parseDouble(parts[2]);
            Integer totalQuantity = Integer.parseInt(parts[3]);
            Orders.orderStatus status = Orders.orderStatus.valueOf(parts[4]); // Parse the role from the string.
            Orders.orderType type = Orders.orderType.valueOf(parts[5]); // Parse the role from the string.
            UUID transactionId = UUID.fromString(parts[6]);
            LocalDateTime updatedAt = LocalDateTime.parse(parts[7]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[8]);

            return new Orders(orderId, userId, totalPrice, totalQuantity, status, type, transactionId, updatedAt, createdAt);
        } catch (Exception e) {
            return null;
        }
    }
}
