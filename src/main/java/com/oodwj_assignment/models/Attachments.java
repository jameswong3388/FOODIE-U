package com.oodwj_assignment.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Attachments extends Model {
    private final UUID notificationId;
    private final UUID attachedId;

    public Attachments(UUID attachmentId, UUID notificationId, UUID attachedId, LocalDateTime updatedAt, LocalDateTime createdAt) {
        super(attachmentId, updatedAt, createdAt);
        this.notificationId = notificationId;
        this.attachedId = attachedId;
    }

    public UUID getNotificationId() {
        return notificationId;
    }

    public UUID getAttachedId() {
        return attachedId;
    }

    @Override
    public String toString() {
        return getId() + ";" + getNotificationId() + ";" + getAttachedId() + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }
}
