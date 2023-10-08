package com.oodwj_assignment.Models;

import com.oodwj_assignment.APIs.Response;

import java.time.LocalDateTime;
import java.util.UUID;

public class Notifications {
    private UUID notificationId;
    private final String message;
    private notificationStatus status;
    private final notificationType type;
    public final UUID userid;
    public final LocalDateTime createdAt;

    public Notifications(UUID notificationId, String message, notificationStatus status, notificationType type, UUID userid, LocalDateTime createdAt) {
        this.notificationId = notificationId;
        this.message = message;
        this.status = status;
        this.type = type;
        this.userid = userid;
        this.createdAt = createdAt;
    }

    public UUID getNotificationId() {
        return notificationId;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Response<Object> getAttributeValue(String attributeName) {
        switch (attributeName) {
            case "notificationId" -> {
                return Response.success("Notification ID", getNotificationId());
            }
            case "message" -> {
                return Response.success("Message", getMessage());
            }
            case "status" -> {
                return Response.success("Status", getStatus());
            }
            case "type" -> {
                return Response.success("Type", getType());
            }
            case "userid" -> {
                return Response.success("Recipient ID", getUserid());
            }
            case "createdAt" -> {
                return Response.success("Created At", getCreatedAt());
            }
            default -> {
                return Response.failure("Invalid attribute name");
            }
        }
    }

    public void setNotificationId(UUID notificationId) {
        this.notificationId = notificationId;
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
        return notificationId + ";" + message + ";" + status + ";" + type + ";" + userid + ";" + ";" + createdAt;
    }
}

