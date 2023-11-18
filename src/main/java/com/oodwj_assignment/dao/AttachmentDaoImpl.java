package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.models.Attachments;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

public class AttachmentDaoImpl extends AbstractDao<Attachments> implements AttachmentDao {
    private static final File FILE = new File("database/attachments.txt");

    public AttachmentDaoImpl() {
        super(FILE);
    }

    public Attachments parse(String[] parts) {
        try {
            UUID attachmentId = UUID.fromString(parts[0]);
            UUID userId = UUID.fromString(parts[1]);
            UUID notificationId = UUID.fromString(parts[2]);
            Attachments.attachmentType attachment = Attachments.attachmentType.valueOf(parts[3]);
            LocalDateTime updatedAt = LocalDateTime.parse(parts[4]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[5]);

            return new Attachments(attachmentId, userId, notificationId, attachment, updatedAt, createdAt);
        } catch (Exception e) {
            return null;
        }
    }
}
