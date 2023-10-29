package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.Dao;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Orders;
import com.oodwj_assignment.models.Tasks;

public interface TaskDao extends Dao<Tasks> {
    Response<Double> calDeliveryFee(Orders orderId);
}
