package com.oodwj_assignment.dao.users;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.models.AdminProfile;
import com.oodwj_assignment.models.CustomerProfile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

public class CustomerProfileDaoImpl extends AbstractDao<CustomerProfile> implements CustomerProfileDao  {
    private static final File FILE = new File("database/customerProfiles.dat");

    public CustomerProfileDaoImpl() {
        super(FILE);
    }
}
