package com.oodwj_assignment.Models;

import com.oodwj_assignment.APIs.Response;
import com.oodwj_assignment.Helpers.Model;

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

    public Response<Object> getAttributeValue(String attributeName) {
        switch (attributeName) {
            case "notificationId" -> {
                return Response.success("Notification ID", getId());
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
            case "updatedAt" -> {
                return Response.success("Updated At", getUpdatedAt());
            }
            case "createdAt" -> {
                return Response.success("Created At", getCreatedAt());
            }
            default -> {
                return Response.failure("Invalid attribute name");
            }
        }
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
        return getId() + ";" + message + ";" + status + ";" + type + ";" + userid + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }
}

