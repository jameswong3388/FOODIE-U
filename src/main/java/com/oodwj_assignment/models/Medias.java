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
    private int height;
    private int width;
    private Double size;

    public Medias(UUID mediaId, String Model, UUID ModelUUID, String Collection, String fileName, String mimeType, String disk, Integer height, Integer width, Double size, LocalDateTime updatedAt, LocalDateTime createdAt) {
        super(mediaId, createdAt, updatedAt);
        this.Model = Model;
        this.ModelUUID = ModelUUID;
        this.Collection = Collection;
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.disk = disk;
        this.height = height;
        this.width = width;
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

    public Integer getHeight() {
        return height;
    }

    public Integer getWidth() {
        return width;
    }

    public Double getSize() {
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

    public void setHeight(Integer height) {
        this.height = height;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return getId() + ";" + getModel() + ";" + getModelUUID() + ";" + getCollection() + ";" + getFileName() + ";" + getMimeType() + ";" + getDisk() + ";" + getHeight() + ";" + getWidth() + ";" + getSize() + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }
}
