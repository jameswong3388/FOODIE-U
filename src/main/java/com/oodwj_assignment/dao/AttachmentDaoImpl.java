package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.models.Attachments;

import java.time.LocalDateTime;
import java.util.UUID;

public class AttachmentDaoImpl extends AbstractDao<Attachments> implements AttachmentDao {
    private static final String FILE_NAME = "database/attachments.txt";

    public AttachmentDaoImpl() {
        super(FILE_NAME);
    }

    public Attachments parse(String[] parts) {
        try {
            UUID attachmentId = UUID.fromString(parts[0]);
            UUID notificationId = UUID.fromString(parts[1]);
            UUID attachedId = UUID.fromString(parts[2]);
            LocalDateTime updatedAt = LocalDateTime.parse(parts[3]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[4]);

            return new Attachments(attachmentId, notificationId, attachedId, updatedAt, createdAt);
        } catch (Exception e) {
            return null;
        }
    }
}
