package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Orders;
import com.oodwj_assignment.models.Tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class TaskDaoImpl extends AbstractDao<Tasks> implements TaskDao {

    private static final String FILE_NAME = "database/tasks.txt";

    public TaskDaoImpl() {
        super(FILE_NAME);
    }

    public Tasks parse(String[] parts) {
        try {
            UUID taskId = UUID.fromString(parts[0]);
            UUID runnerId = UUID.fromString(parts[1]);
            UUID orderId = UUID.fromString(parts[2]);
            Double deliveryFee = Double.parseDouble(parts[3]);
            Tasks.taskStatus status = Tasks.taskStatus.valueOf(parts[4]);
            LocalDateTime updatedAt = LocalDateTime.parse(parts[5]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[6]);

            return new Tasks(taskId, runnerId, orderId, deliveryFee, status, updatedAt, createdAt);
        } catch (Exception e) {
            return null;
        }
    }

    public Response<Double> calDeliveryFee(Orders orderId) {
        Response<ArrayList<Orders>> orders = DaoFactory.getOrderDao().read(Map.of("orderId", orderId));

        if (orders.isSuccess()) {
            double totalPrice = orders.getData().get(0).getTotalPrice();

            return Response.success("Delivery Fee calculated successfully", totalPrice * 0.15);
        } else {
            return Response.failure(orders.getMessage());
        }
    }
}
