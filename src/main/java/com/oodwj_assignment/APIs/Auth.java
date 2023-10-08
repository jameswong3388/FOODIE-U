package com.oodwj_assignment.APIs;

import com.oodwj_assignment.Models.Sessions;
import com.oodwj_assignment.Models.Users;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Auth {
    private static final String FILE_NAME = "database/sessions.txt";

    public static Response<ArrayList<Sessions>> read(Map<String, Object> query) {
        ArrayList<Sessions> sessions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 14) {
                    UUID sessionId = UUID.fromString(parts[0]);
                    UUID userId = UUID.fromString(parts[1]);
                    LocalDateTime startTime = LocalDateTime.parse(parts[2]);
                    LocalDateTime endTime = parts[3].equals("null") ? null : LocalDateTime.parse(parts[3]);
                    long duration = Long.parseLong(parts[4]);
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

        if (sessions.isEmpty()) {
            return Response.failure("No sessions found");
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

    public static Response<UUID> login(String username, String password) throws UnknownHostException {
        Users user = getUserByUsername(username);

        if (user != null) {
            if (user.getPassword().equals(password)) {
                Response<ArrayList<Sessions>> sessions = read(Map.of());
                boolean hasSession = false;
                UUID newSessionToken = generateSessionToken();

                for (Sessions session : sessions.getData()) {
                    Response<Void> matchRes = match(Map.of("userId", user.getUserId()), session);

                    if (matchRes.isSuccess()) {
                        hasSession = true;
                        session.setStartTime(LocalDateTime.now());
                        session.setEndTime(null);
                        session.setDuration(0);

                        InetAddress localhost = InetAddress.getLocalHost();
                        session.setIpAddress(localhost.getHostAddress());

                        session.setIsAuthenticated(true);
                        session.setTerminationReason(null);
                        session.setActive(true);
                        session.setSessionToken(newSessionToken);
                    }
                }

                if (!hasSession) {
                    Sessions newSession = new Sessions(UUID.randomUUID(), user.getUserId(), LocalDateTime.now(), null, 0, null, null, null, null, false, null, null, true, newSessionToken);
                    InetAddress localhost = InetAddress.getLocalHost();
                    newSession.setIpAddress(localhost.getHostAddress());
                    sessions.getData().add(newSession);
                }
                Response<Void> saveRes = saveAllSessions(sessions.getData());
                if (saveRes.isSuccess()) {
                    return Response.success("User logged in successfully", newSessionToken);
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


    public static Response<Void> logout(UUID sessionToken) throws UnknownHostException {
        Response<ArrayList<Sessions>> res = read(Map.of());

        if (res.isSuccess()) {
            ArrayList<Sessions> sessions = res.getData();

            for (Sessions session : sessions) {
                Response<Void> matchRes = match(Map.of("sessionToken", sessionToken), session);

                if (matchRes.isSuccess()) {
                    session.setActive(false);
                    session.setEndTime(LocalDateTime.now());
                    session.setTerminationReason("User logged out");
                    session.setIsAuthenticated(false);

                    session.setDuration(Duration.between(session.getStartTime(), session.getEndTime()).toMillis());

                    InetAddress localhost = InetAddress.getLocalHost();
                    session.setIpAddress(localhost.getHostAddress());

                    Response<Void> saveRes = saveAllSessions(sessions);

                    if (saveRes.isSuccess()) {
                        return Response.success("User logged out successfully");
                    } else {
                        return Response.failure(saveRes.getMessage());
                    }
                }
            }

            return Response.failure("Session not found");

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

