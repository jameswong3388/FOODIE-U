package com.oodwj_assignment.interfaces;


import com.oodwj_assignment.models.Medias;

import java.io.File;
import java.util.UUID;

public interface HasMedia {
    public Void addMedia(File file, String Disk, UUID modelUUID);
}
