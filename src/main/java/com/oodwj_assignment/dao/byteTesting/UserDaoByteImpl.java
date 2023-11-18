package com.oodwj_assignment.dao.byteTesting;

import com.oodwj_assignment.dao.base.AbstractDaoByte;
import com.oodwj_assignment.models.Users;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

public class UserDaoByteImpl extends AbstractDaoByte<Users> implements UserDaoByte {
    private static final File FILE = new File("database/users.dat");
    public UserDaoByteImpl() {
        super(FILE);
    }

    public Users parse(String[] parts) {
        try {
            UUID uuid = UUID.fromString(parts[0]);
            String username = parts[1];
            String password = parts[2];
            Users.Role role = Users.Role.valueOf(parts[3]);
            String name = parts[4];
            String phoneNumber = parts[5];
            String email = parts[6];
            Users.AccountStatus accountStatus = Users.AccountStatus.valueOf(parts[7]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[8]);
            LocalDateTime updatedAt = LocalDateTime.parse(parts[9]);

            return new Users(uuid, username, password, role, name, phoneNumber, email, accountStatus, createdAt, updatedAt);
        } catch (Exception e) {
            return null; // Return null if parsing fails
        }
    }
}
