package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.models.Orders;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

public class OrderDaoImpl extends AbstractDao<Orders> implements OrderDao {

    private static final File FILE = new File("database/orders.dat");

    public OrderDaoImpl() {
        super(FILE);
    }
}
