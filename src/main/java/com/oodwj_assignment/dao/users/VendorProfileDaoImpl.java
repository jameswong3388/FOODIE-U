package com.oodwj_assignment.dao.users;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.models.VendorProfile;

import java.time.LocalDateTime;
import java.util.UUID;

public class VendorProfileDaoImpl extends AbstractDao<VendorProfile> implements VendorProfileDao {
    private static final String FILE_NAME = "database/vendorProfiles.txt";

    public VendorProfileDaoImpl() {
        super(FILE_NAME);
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
