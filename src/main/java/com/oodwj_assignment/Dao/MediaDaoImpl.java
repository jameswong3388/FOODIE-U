package com.oodwj_assignment.Dao;

import com.oodwj_assignment.Dao.Base.AbstractDao;
import com.oodwj_assignment.Models.Medias;

public class MediaDaoImpl extends AbstractDao<Medias> implements MediaDao {

    private static final String FILE_NAME = "database/medias.txt";

    public MediaDaoImpl() {
        super(FILE_NAME);
    }

    @Override
    public Medias parse(String[] parts) {
        return null;
    }
}
