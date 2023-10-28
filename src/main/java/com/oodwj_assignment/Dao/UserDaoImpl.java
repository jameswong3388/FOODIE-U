package com.oodwj_assignment.Dao;

import com.oodwj_assignment.Dao.Base.AbstractDao;
import com.oodwj_assignment.Dao.Base.DaoFactory;
import com.oodwj_assignment.Helpers.Response;
import com.oodwj_assignment.Models.Users;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class UserDaoImpl extends AbstractDao<Users> implements UserDao {

    private static final String FILE_NAME = "database/users.txt";

    public UserDaoImpl() {
        super(FILE_NAME);
    }

    @Override
    public Users parse(String[] parts) {
        try {
            UUID uuid = UUID.fromString(parts[0]);
            String username = parts[1];
            String password = parts[2];
            String email = parts[3];
            int age = Integer.parseInt(parts[4]);
            Users.Role role = Users.Role.valueOf(parts[5]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[6]);
            LocalDateTime updatedAt = LocalDateTime.parse(parts[7]);

            return new Users(uuid, username, password, email, age, role, createdAt, updatedAt);
        } catch (Exception e) {
            return null; // Return null if parsing fails
        }
    }

    @Override
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

    @Override
    public Response<Users> getByUsername(String username) {
        Response<ArrayList<Users>> users = DaoFactory.getUserDao().read(Map.of("username", username));

        if (users.isSuccess()) {
            return Response.success("User found", users.getData().get(0));
        } else {
            return null;
        }
    }
}
