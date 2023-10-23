package com.oodwj_assignment.Models;

import com.oodwj_assignment.APIs.Response;
import com.oodwj_assignment.Helpers.Model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Attachments extends Model {
    private final UUID NotificationId;
    private final UUID attachedId;
    private final attachmentType attachmentType;

    public Attachments(UUID attachmentId, UUID NotificationId, UUID attachedId, attachmentType attachmentType, LocalDateTime updatedAt, LocalDateTime createdAt) {
        super(attachmentId, updatedAt, createdAt);
        this.NotificationId = NotificationId;
        this.attachedId = attachedId;
        this.attachmentType = attachmentType;
    }

    public UUID getNotificationId() {
        return NotificationId;
    }

    public UUID getAttachedId() {
        return attachedId;
    }

    public attachmentType getAttachmentType() {
        return attachmentType;
    }

    public Response<Object> getAttributeValue(String attributeName) {
        return switch (attributeName) {
            case "attachmentId" -> Response.success("Attachment ID found", getId());
            case "NotificationId" -> Response.success("Notification ID found", getNotificationId());
            case "attachedId" -> Response.success("Attached ID found", getAttachedId());
            case "attachmentType" -> Response.success("Attachment type found", getAttachmentType());
            case "updatedAt" -> Response.success("Updated at found", getUpdatedAt());
            case "createdAt" -> Response.success("Created at found", getCreatedAt());
            default -> Response.failure("Invalid attribute name");
        };
    }

    public enum attachmentType {
        Invoice,
        Receipt,
    }

    @Override
    public String toString() {
        return getId() + ";" + NotificationId + ";" + attachedId + ";" + attachmentType + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }
}
