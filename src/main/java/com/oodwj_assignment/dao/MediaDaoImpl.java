package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.models.Medias;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

public class MediaDaoImpl extends AbstractDao<Medias> implements MediaDao {

    private static final File FILE = new File("database/medias.dat");

    public MediaDaoImpl() {
        super(FILE);
    }

    public String getExtensionByStringHandling(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }

        return filename.substring(filename.lastIndexOf("."));
    }

    public Double getMediaSizeMegaBytes(File file) {
        return (double) file.length() / (1024 * 1024);
    }
}
