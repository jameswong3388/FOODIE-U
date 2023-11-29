package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Orders;
import com.oodwj_assignment.models.Tasks;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class TaskDaoImpl extends AbstractDao<Tasks> implements TaskDao {

    private static final File FILE = new File("database/tasks.dat");

    public TaskDaoImpl() {
        super(FILE);
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
