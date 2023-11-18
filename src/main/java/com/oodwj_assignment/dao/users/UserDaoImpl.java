package com.oodwj_assignment.dao.users;

import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Users;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class UserDaoImpl extends AbstractDao<Users> implements UserDao {

    private static final File FILE = new File("database/users.txt");

    public UserDaoImpl() {
        super(FILE);
    }

    @Override
    public Response<UUID> create(Users model) {
        if (isUsernameTaken(model.getUsername()).getData()) {
            return Response.failure("Username is taken");
        }

        UUID userId = UUID.randomUUID();
        model.setId(userId);

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE, true))) {
            writer.println(model);

            return Response.success("User created successfully", userId);
        } catch (IOException e) {
            return Response.failure("Failed to create user: " + e.getMessage());
        }
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

    /**
     * Checks if username is taken.
     *
     * @param username username to be checked
     * @return a boolean value indicating whether the username is taken or not
     */
    public Response<Boolean> isUsernameTaken(String username) {
        Response<ArrayList<Users>> users = DaoFactory.getUserDao().read(Map.of("username", username));

        if (users.isSuccess()) {
            for (Users user : users.getData()) {
                if (user.getUsername().equals(username)) {
                    return Response.success("Username is taken", true);
                }
            }
        } else {
            return Response.failure(users.getMessage());
        }

        return Response.success("Username is not taken", false);
    }

    /**
     * Gets user by username.
     *
     * @param username username of user to be fetched
     * @return Response object containing user if found, or error message if not
     */
    public Response<Users> getByUsername(String username) {
        Response<ArrayList<Users>> users = DaoFactory.getUserDao().read(Map.of("username", username));

        if (users.isSuccess()) {
            return Response.success("User found", users.getData().get(0));
        } else {
            return Response.failure(users.getMessage());
        }
    }
}
