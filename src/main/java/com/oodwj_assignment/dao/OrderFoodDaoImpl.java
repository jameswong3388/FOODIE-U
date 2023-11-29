package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.models.OrderFoods;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

public class OrderFoodDaoImpl extends AbstractDao<OrderFoods> implements OrderFoodDao {

    private static final File FILE = new File("database/orderFoods.dat");

    public OrderFoodDaoImpl() {
        super(FILE);
    }
}
