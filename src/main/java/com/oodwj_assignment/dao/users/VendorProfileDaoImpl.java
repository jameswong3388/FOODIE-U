package com.oodwj_assignment.dao.users;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.models.VendorProfile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

public class VendorProfileDaoImpl extends AbstractDao<VendorProfile> implements VendorProfileDao {
    private static final File FILE = new File("database/vendorProfiles.dat");

    public VendorProfileDaoImpl() {
        super(FILE);
    }
}
