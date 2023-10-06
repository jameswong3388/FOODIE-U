package com.oodwj_assignment.Models;

import com.oodwj_assignment.APIs.Response;

import java.time.LocalDate;
import java.util.UUID;

public class Attachments {
    private UUID attachmentId;
    private final UUID NotificationId;
    private final UUID attachedId;
    private final attachmentType attachmentType;
    private final LocalDate updatedAt;
    private final LocalDate createdAt;

    public Attachments(UUID attachmentId, UUID NotificationId, UUID attachedId, attachmentType attachmentType, LocalDate updatedAt, LocalDate createdAt) {
        this.attachmentId = attachmentId;
        this.NotificationId = NotificationId;
        this.attachedId = attachedId;
        this.attachmentType = attachmentType;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public UUID getAttachmentId() {
        return attachmentId;
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

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public Response<Object> getAttributeValue(String attributeName) {
        return switch (attributeName) {
            case "attachmentId" -> Response.success("Attachment ID found", attachmentId);
            case "NotificationId" -> Response.success("Notification ID found", NotificationId);
            case "attachedId" -> Response.success("Attached ID found", attachedId);
            case "attachmentType" -> Response.success("Attachment type found", attachmentType);
            case "updatedAt" -> Response.success("Updated at found", updatedAt);
            case "createdAt" -> Response.success("Created at found", createdAt);
            default -> Response.failure("Invalid attribute name");
        };
    }

    public void setAttachmentId(UUID attachmentId) {
        this.attachmentId = attachmentId;
    }

    public enum attachmentType {
        Invoice,
        Receipt,
    }

    @Override
    public String toString() {
        return attachmentId + ";" + NotificationId + ";" + attachedId + ";" + attachmentType + ";" + updatedAt + ";" + createdAt;
    }
}
