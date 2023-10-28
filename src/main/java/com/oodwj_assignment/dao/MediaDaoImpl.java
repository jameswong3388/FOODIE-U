package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.models.Medias;

import java.time.LocalDateTime;
import java.util.UUID;

public class MediaDaoImpl extends AbstractDao<Medias> implements MediaDao {

    private static final String FILE_NAME = "database/medias.txt";

    public MediaDaoImpl() {
        super(FILE_NAME);
    }

    public Medias parse(String[] parts) {
        try {
            UUID mediaId = UUID.fromString(parts[0]);
            String model = parts[1];
            UUID modelUUID = UUID.fromString(parts[2]);
            String collection = parts[3];
            String name = parts[4];
            String fileName = parts[5];
            String mimeType = parts[6];
            String disk = parts[7];
            String dimension = parts[8];
            String size = parts[9];
            LocalDateTime updatedAt = LocalDateTime.parse(parts[10]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[11]);

            return new Medias(mediaId, model, modelUUID, collection, name, fileName, mimeType, disk, dimension, size, updatedAt, createdAt);
        } catch (Exception e) {
            System.out.println("Failed to parse media: " + e.getMessage());
            return null;
        }
    }
}
