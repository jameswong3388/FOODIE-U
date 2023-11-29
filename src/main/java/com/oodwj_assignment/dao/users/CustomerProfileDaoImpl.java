package com.oodwj_assignment.dao.users;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.models.AdminProfile;
import com.oodwj_assignment.models.CustomerProfile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class CustomerProfileDaoImpl extends AbstractDao<CustomerProfile> implements CustomerProfileDao  {
    private static final String FILE_NAME = "database/customerProfiles.txt";

    public CustomerProfileDaoImpl() {
        super(FILE_NAME);
    }
    @Override
    public CustomerProfile parse(String[] parts) {
        try {
            UUID profileId = UUID.fromString(parts[0]);
            UUID userId = UUID.fromString(parts[1]);
            CustomerProfile.Gender gender = CustomerProfile.Gender.valueOf(parts[2]);
            LocalDate dob = LocalDate.parse(parts[3]);
            LocalDateTime updatedAt = LocalDateTime.parse(parts[4]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[5]);

            return new CustomerProfile(profileId, userId, gender, dob, updatedAt, createdAt);
        } catch (Exception e) {
            return null;
        }
    }
}
