package com.oodwj_assignment.Dao;

import com.oodwj_assignment.Dao.Base.Dao;
import com.oodwj_assignment.Helpers.Response;
import com.oodwj_assignment.Models.Orders;
import com.oodwj_assignment.Models.Tasks;

public interface TaskDao extends Dao<Tasks> {
    Response<Double> calDeliveryFee(Orders orderId);
}
