package com.oodwj_assignment.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Notifications extends Model {
    private final String message;
    private notificationStatus status;
    private final notificationType type;
    public final UUID userid;

    public Notifications(UUID notificationId, String message, notificationStatus status, notificationType type, UUID userid, LocalDateTime updatedAt, LocalDateTime createdAt) {
        super(notificationId, updatedAt, createdAt);
        this.message = message;
        this.status = status;
        this.type = type;
        this.userid = userid;
    }

    public String getMessage() {
        return message;
    }

    public notificationStatus getStatus() {
        return status;
    }

    public notificationType getType() {
        return type;
    }

    public UUID getUserid() {
        return userid;
    }

    public void setStatus(notificationStatus status) {
        this.status = status;
    }

    public boolean hasAttachment() {
        return false;
    }

    public enum notificationStatus {
        Read,
        Unread
    }

    public enum notificationType {
        Receipt,
        Information,
        Warning,
        Error,
        Success,
        Other
    }

    @Override
    public String toString() {
        return getId() + ";" + getMessage() + ";" + getStatus() + ";" + getType() + ";" + getUserid() + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }
}

