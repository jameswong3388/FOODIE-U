package com.oodwj_assignment.APIs;

import com.oodwj_assignment.Models.Sessions;
import com.oodwj_assignment.Models.Users;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class Auth {
    private static final String FILE_NAME = "src/main/java/com/oodwj_assignment/Databases/sessions.txt";

    public static Response<ArrayList<Sessions>> read(Map<String, Object> query) {
        ArrayList<Sessions> sessions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 14) {
                    UUID sessionId = UUID.fromString(parts[0]);
                    UUID userId = UUID.fromString(parts[1]);
                    LocalDate startTime = LocalDate.parse(parts[2]);
                    LocalDate endTime = parts[3].equals("null") ? null : LocalDate.parse(parts[3]);
                    long duration = parts[4].equals("null") ? 0 : Long.parseLong(parts[4]);
                    String ipAddress = parts[5];
                    String userAgent = parts[6];
                    String location = parts[7];
                    String deviceInfo = parts[8];
                    boolean isAuthenticated = Boolean.parseBoolean(parts[9]);
                    String referer = parts[10];
                    String terminationReason = parts[11];
                    boolean isActive = Boolean.parseBoolean(parts[12]);
                    UUID sessionToken = UUID.fromString(parts[13]);

                    sessions.add(new Sessions(sessionId, userId, startTime, endTime, duration, ipAddress, userAgent, location, deviceInfo, isAuthenticated, referer, terminationReason, isActive, sessionToken));
                }
            }
        } catch (IOException e) {
            return Response.failure("Failed to read sessions: " + e.getMessage());
        }
        if (query.isEmpty()) {
            return Response.success("Sessions read successfully", sessions);
        }

        ArrayList<Sessions> matchedSessions = new ArrayList<>();

        for (Sessions session : sessions) {
            Response<Void> matchRes = match(query, session);

            if (matchRes.isSuccess()) {
                matchedSessions.add(session);
            }
        }

        return Response.success("Session found", matchedSessions);
    }

    public static Response<UUID> login(String username, String password) {
        Users user = getUserByUsername(username);

        if (user != null) {
            if (user.getPassword().equals(password)) {
                Sessions session = new Sessions(UUID.randomUUID(), user.getUserId(), LocalDate.now(), null, 0, null, null, null, null, false, null, null, true, generateSessionToken());

                Response<Void> saveRes = saveAllSessions(new ArrayList<>(Arrays.asList(session)));

                if (saveRes.isSuccess()) {
                    return Response.success("User logged in successfully", session.getSessionToken());
                } else {
                    return Response.failure(saveRes.getMessage());
                }
            } else {
                return Response.failure("Incorrect password");
            }
        } else {
            return Response.failure("User not found");
        }
    }


    public static Response<Void> logout(UUID sessionToken) {
        Response<ArrayList<Sessions>> res = read(Map.of("sessionToken", sessionToken));

        if (res.isSuccess()) {
            ArrayList<Sessions> sessions = res.getData();

            if (!sessions.isEmpty()) {
                Sessions session = sessions.get(0);

                if (session.isActive()) {
                    session.setActive(false);
                    session.setEndTime(LocalDate.now());
                    session.setTerminationReason("User logged out");
                    session.setIsAuthenticated(false);

                    Response<Void> saveRes = saveAllSessions(sessions);

                    if (saveRes.isSuccess()) {
                        return Response.success("User logged out successfully");
                    } else {
                        return Response.failure(saveRes.getMessage());
                    }
                } else {
                    return Response.failure("Session is not active");
                }
            } else {
                return Response.failure("Session not found");
            }
        } else {
            return Response.failure(res.getMessage());
        }
    }

    public static Response<Boolean> isAuthenticated(String sessionToken) {
        Response<ArrayList<Sessions>> res = read(Map.of("sessionToken", sessionToken));

        if (res.isSuccess()) {
            ArrayList<Sessions> sessions = res.getData();

            if (!sessions.isEmpty()) {
                Sessions session = sessions.get(0);

                if (session.isActive() && session.isAuthenticated()) {
                    return Response.success("User is authenticated", true);
                } else {
                    return Response.success("User is not authenticated", false);
                }
            } else {
                return Response.failure("Session not found");
            }
        } else {
            return Response.failure(res.getMessage());
        }
    }

    private static Users getUserByUsername(String username) {
        // Implement a method to retrieve a user by username from your Users database
        // For simplicity, we assume you have such a method here
        // Example:
        ArrayList<Users> users = User.read(Map.of("username", username)).getData();
        if (!users.isEmpty()) {
            return users.get(0);
        } else {
            return null;
        }
    }

    private static UUID generateSessionToken() {
        // Implement a method to generate a unique session token
        // You can use UUID or other secure methods to generate tokens
        return UUID.randomUUID();
    }

    private static Response<Void> match(Map<String, Object> query, Sessions session) {
        boolean match = true;

        for (Map.Entry<String, Object> entry : query.entrySet()) {
            String attributeName = entry.getKey();
            Object expectedValue = entry.getValue();

            Response<Object> actualValue = session.getAttributeValue(attributeName);

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
            return Response.success("Session found");
        } else {
            return Response.failure("Session not found");
        }
    }

    private static Response<Void> saveAllSessions(ArrayList<Sessions> sessions) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Sessions session : sessions) {
                writer.println(session.toString());
            }
        } catch (IOException e) {
            return Response.failure(e.getMessage());
        }

        return Response.success("Sessions saved successfully");
    }
}

