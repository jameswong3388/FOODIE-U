package com.oodwj_assignment.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Medias extends Model {
    private String Model;
    private UUID ModelUUID;
    private String Collection;
    private String fileName;
    private String mimeType;
    private String disk;
    private String dimension;
    private String size;

    public Medias(UUID mediaId, String Model, UUID ModelUUID, String Collection, String fileName, String mimeType, String disk, String dimension, String size, LocalDateTime updatedAt, LocalDateTime createdAt) {
        super(mediaId, createdAt, updatedAt);
        this.Model = Model;
        this.ModelUUID = ModelUUID;
        this.Collection = Collection;
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.disk = disk;
        this.dimension = dimension;
        this.size = size;
    }

    public String getModel() {
        return Model;
    }

    public UUID getModelUUID() {
        return ModelUUID;
    }

    public String getCollection() {
        return Collection;
    }

    public String getFileName() {
        return fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getDisk() {
        return disk;
    }

    public String getDimension() {
        return dimension;
    }

    public String getSize() {
        return size;
    }

    public void setModel(String Model) {
        this.Model = Model;
    }

    public void setModelUUID(UUID ModelUUID) {
        this.ModelUUID = ModelUUID;
    }

    public void setCollection(String Collection) {
        this.Collection = Collection;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setDisk(String disk) {
        this.disk = disk;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return getId() + ";" + getModel() + ";" + getModelUUID() + ";" + getCollection() + ";" + getFileName() + ";" + getMimeType() + ";" + getDisk() + ";" + getDimension() + ";" + getSize() + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }
}
