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

    @Override
    public VendorProfile parse(String[] parts) {
        try {
            UUID profileId = UUID.fromString(parts[0]);
            UUID userId = UUID.fromString(parts[1]);
            LocalDateTime updatedAt = LocalDateTime.parse(parts[2]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[3]);

            return new VendorProfile(profileId, userId, updatedAt, createdAt);
        } catch (Exception e) {
            return null;
        }
    }
}
