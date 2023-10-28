package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.models.Notifications;

import java.time.LocalDateTime;
import java.util.UUID;

public class NotificationDaoImpl extends AbstractDao<Notifications> implements NotificationDao {

    private static final String FILE_NAME = "database/notifications.txt";

    public NotificationDaoImpl() {
        super(FILE_NAME);
    }

    public Notifications parse(String[] parts) {
        try {
            UUID notificationId = UUID.fromString(parts[0]);
            String message = parts[1];
            Notifications.notificationStatus status = Notifications.notificationStatus.valueOf(parts[2]);
            Notifications.notificationType type = Notifications.notificationType.valueOf(parts[3]);
            UUID userid = UUID.fromString(parts[4]);
            LocalDateTime updatedAt = LocalDateTime.parse(parts[5]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[6]);

            return new Notifications(notificationId, message, status, type, userid, updatedAt, createdAt);
        } catch (Exception e) {
            return null;
        }
    }
}
