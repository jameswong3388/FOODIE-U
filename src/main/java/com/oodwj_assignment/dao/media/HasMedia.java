package com.oodwj_assignment.dao.media;


import com.oodwj_assignment.models.Medias;

public interface HasMedia {
    public void addMedia(Medias media);
    public void removeMedia(Medias media);
    public void updateMedia(Medias media);
}
