package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.models.Attachments;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

public class AttachmentDaoImpl extends AbstractDao<Attachments> implements AttachmentDao {
    private static final File FILE = new File("database/attachments.dat");

    public AttachmentDaoImpl() {
        super(FILE);
    }
}
