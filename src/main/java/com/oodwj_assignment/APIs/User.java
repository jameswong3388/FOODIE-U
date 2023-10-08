package com.oodwj_assignment.APIs;

import com.oodwj_assignment.Models.Users;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class User {
    private static final String FILE_NAME = "database/users.txt";

    public static Response<UUID> create(Users user) {
        if (isUsernameTaken(user.getUsername()).getData()) {
            return Response.failure("Username is taken");
        }

        UUID userId = UUID.randomUUID();
        user.setUserId(userId);

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            writer.println(user);

//            Response<Void> createRes = Wallet.create(new Wallets(null, userId, 0.00, LocalDate.now(), LocalDate.now()));
//
//            if (!createRes.isSuccess()) {
//                return Response.failure(createRes.getMessage());
//            }

            return Response.success("User created successfully", userId);
        } catch (IOException e) {
            return Response.failure("Failed to create user: " + e.getMessage());
        }
    }

    public static Response<ArrayList<Users>> read(Map<String, Object> query) {
        ArrayList<Users> users = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 8) {
                    UUID uuid = UUID.fromString(parts[0]);
                    String username = parts[1];
                    String password = parts[2];
                    String email = parts[3];
                    int age = Integer.parseInt(parts[4]);
                    Users.Role role = Users.Role.valueOf(parts[5]); // Parse the role from the string.
                    LocalDateTime createdAt = LocalDateTime.parse(parts[6]);
                    LocalDateTime updatedAt = LocalDateTime.parse(parts[7]);

                    users.add(new Users(uuid, username, password, email, age, role, updatedAt, createdAt));
                }
            }
        } catch (IOException e) {
            return Response.failure("Failed to read users: " + e.getMessage());
        }

        if (users.isEmpty()) {
            return Response.failure("No users found");
        }

        if (query.isEmpty()) {
            return Response.success("Users read successfully", users);
        }

        ArrayList<Users> matchedUsers = new ArrayList<>();

        for (Users user : users) {
            Response<Void> matchRes = match(query, user);

            if (matchRes.isSuccess()) {
                matchedUsers.add(user);
            }
        }

        return Response.success("User found", matchedUsers);
    }

    public static Response<Void> update(Map<String, Object> query, Map<String, Object> newValue) {
        Response<ArrayList<Users>> users = read(Map.of());

        if (users.isSuccess()) {
            for (Users user : users.getData()) {
                Response<Void> matchRes = match(query, user);

                if (matchRes.isSuccess()) {
                    for (Map.Entry<String, Object> entry : newValue.entrySet()) {
                        String attributeName = entry.getKey();
                        Object expectedValue = entry.getValue();

                        Response<Void> updateRes = user.setAttributeValue(attributeName, expectedValue);

                        if (!updateRes.isSuccess()) {
                            return Response.failure(updateRes.getMessage());
                        }
                    }

                    Response<Void> saveRes = saveAllUsers(users.getData());

                    if (saveRes.isSuccess()) {
                        return Response.success("User updated successfully");
                    } else {
                        return Response.failure(saveRes.getMessage());
                    }
                }
            }

            return Response.failure("User not found");
        } else {
            return Response.failure(users.getMessage());
        }
    }

    public static Response<Void> delete(Map<String, Object> query) {
        Response<ArrayList<Users>> users = read(Map.of());

        if (users.isSuccess()) {
            for (Users user : users.getData()) {
                Response<Void> matchRes = match(query, user);

                if (matchRes.isSuccess()) {
                    users.getData().remove(user);
                    Response<Void> saveRes = saveAllUsers(users.getData());

                    if (saveRes.isSuccess()) {
                        return Response.success("User deleted successfully");
                    } else {
                        return Response.failure(saveRes.getMessage());
                    }
                }
            }
            return Response.failure("User not found");

        } else {
            return Response.failure(users.getMessage());
        }
    }

    private static Response<Void> match(Map<String, Object> query, Users user) {
        boolean match = true;

        for (Map.Entry<String, Object> entry : query.entrySet()) {
            String attributeName = entry.getKey();
            Object expectedValue = entry.getValue();

            Response<Object> actualValue = user.getAttributeValue(attributeName);

            if (actualValue.isSuccess()) {
                if (!actualValue.getData().equals(expectedValue)) {
                    match = false;
                    break;
                }
            } else {
                return Response.failure(actualValue.getMessage());
            }
        }
        if (match) {
            return Response.success("User found");
        } else {
            return Response.failure("User not found");
        }
    }

    private static Response<Void> saveAllUsers(ArrayList<Users> users) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Users user : users) {
                writer.println(user.toString());
            }
        } catch (IOException e) {
            return Response.failure(e.getMessage());
        }

        return Response.success("Users saved successfully");
    }

    private static Response<Boolean> isUsernameTaken(String username) {
        Response<ArrayList<Users>> users = read(Map.of());

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
}
