package com.oodwj_assignment.Models;

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

    public enum attachmentType {
        Invoice,
        Receipt,
    }

    @Override
    public String toString() {
        return getId() + ";" + getNotificationId() + ";" + getAttachedId() + ";" + getAttachmentType() + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }
}
