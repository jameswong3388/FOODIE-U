package com.oodwj_assignment.dao.users;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.models.RunnerProfile;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class RunnerProfileDaoImpl extends AbstractDao<RunnerProfile> implements RunnerProfileDao {
    private static final File FILE = new File("database/runnerProfiles.dat");

    public RunnerProfileDaoImpl() {
        super(FILE);
    }

}
