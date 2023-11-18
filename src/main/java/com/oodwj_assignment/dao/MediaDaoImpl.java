package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.models.Medias;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

public class MediaDaoImpl extends AbstractDao<Medias> implements MediaDao {

    private static final File FILE = new File("database/medias.txt");

    public MediaDaoImpl() {
        super(FILE);
    }

    public Medias parse(String[] parts) {
        try {
            UUID mediaId = UUID.fromString(parts[0]);
            String model = parts[1];
            UUID modelUUID = UUID.fromString(parts[2]);
            String collection = parts[3];
            String fileName = parts[4];
            String mimeType = parts[5];
            String disk = parts[6];
            Integer height = Integer.parseInt(parts[7]);
            Integer width = Integer.parseInt(parts[8]);
            Double size = Double.parseDouble(parts[9]);
            LocalDateTime updatedAt = LocalDateTime.parse(parts[10]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[11]);

            return new Medias(mediaId, model, modelUUID, collection, fileName, mimeType, disk, height, width, size, updatedAt, createdAt);
        } catch (Exception e) {
            System.out.println("Failed to parse media: " + e.getMessage());
            return null;
        }
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
