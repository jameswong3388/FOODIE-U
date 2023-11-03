package com.oodwj_assignment.dao.users;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.models.RunnerProfile;

import java.time.LocalDateTime;
import java.util.UUID;

public class RunnerProfileDaoImpl extends AbstractDao<RunnerProfile> implements RunnerProfileDao {
    private static final String FILE_NAME = "database/runnerProfile.txt";

    public RunnerProfileDaoImpl() {
        super(FILE_NAME);
    }

    @Override
    public RunnerProfile parse(String[] parts) {
        try {
            UUID profileId = UUID.fromString(parts[0]);
            UUID userId = UUID.fromString(parts[1]);
            LocalDateTime updatedAt = LocalDateTime.parse(parts[2]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[3]);

            return new RunnerProfile(profileId, userId, updatedAt, createdAt);
        } catch (Exception e) {
            return null;
        }
    }
}
