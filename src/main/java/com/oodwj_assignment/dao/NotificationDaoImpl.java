package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.models.Notifications;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

public class NotificationDaoImpl extends AbstractDao<Notifications> implements NotificationDao {

    private static final File FILE = new File("database/notifications.dat");

    public NotificationDaoImpl() {
        super(FILE);
    }
}
