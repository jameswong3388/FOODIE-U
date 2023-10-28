package com.oodwj_assignment.dao.media;

import com.oodwj_assignment.models.Medias;

public abstract class InteractWithMedia implements HasMedia {
    public void addMedia(Medias media) {
        System.out.println("Media added");
    }

    public void removeMedia(Medias media) {

    }

    public void updateMedia(Medias media) {

    }
}
