package com.oodwj_assignment.dao.users;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.models.AdminProfile;
import com.oodwj_assignment.models.RunnerProfile;
import java.time.LocalDate;
import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

public class AdminProfileDaoImpl extends AbstractDao<AdminProfile> implements AdminProfileDao {
    private static final File FILE = new File("database/adminProfiles.dat");

    public AdminProfileDaoImpl() {
        super(FILE);
    }
}
