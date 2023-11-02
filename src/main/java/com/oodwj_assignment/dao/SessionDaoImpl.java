package com.oodwj_assignment.dao;

import com.oodwj_assignment.helpers.UniqueId;
import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Sessions;
import com.oodwj_assignment.models.Users;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class SessionDaoImpl extends AbstractDao<Sessions> implements SessionDao {

    private static final String FILE_NAME = "database/sessions.txt";

    public SessionDaoImpl() {
        super(FILE_NAME);
    }

    public Sessions parse(String[] parts) {
        try {
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
            LocalDateTime updatedAt = LocalDateTime.parse(parts[14]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[15]);

            return new Sessions(sessionId, userId, startTime, endTime, duration, ipAddress, userAgent, location, deviceInfo, isAuthenticated, referer, terminationReason, isActive, sessionToken, updatedAt, createdAt);
        } catch (Exception e) {
            return null;
        }
    }

    /***
     * Authenticates user
     *
     * @param username username of user
     * @param password password of user
     * @return session token
     * @throws UnknownHostException if host is unknown
     */
    public Response<UUID> login(String username, String password) throws UnknownHostException {
        Response<Users> user = new UserDaoImpl().getByUsername(username);

        if (!user.isSuccess()) {
            return Response.failure("User not found");
        }

        if (!user.getData().getPassword().equals(password)) {
            return Response.failure("Incorrect password");
        }

        Response<ArrayList<Sessions>> sessions = read(Map.of());

        UUID newSessionToken = generateSessionToken();
        InetAddress localhost = InetAddress.getLocalHost();
        String hostAddress = localhost.getHostAddress();

        boolean hasSession = false;

        for (Sessions session : sessions.getData()) {
            Response<Void> matchRes = match(Map.of("userId", user.getData().getId()), session);

            if (matchRes.isSuccess()) {
                hasSession = true;
                session.setStartTime(LocalDateTime.now());
                session.setEndTime(null);
                session.setDuration(0);

                session.setIpAddress(hostAddress);

                session.setIsAuthenticated(true);
                session.setTerminationReason(null);
                session.setActive(true);
                session.setSessionToken(newSessionToken);

                session.setUpdatedAt(LocalDateTime.now());
            }
        }

        if (!hasSession) {
            Sessions newSession = new Sessions(UUID.randomUUID(), user.getData().getId(), LocalDateTime.now(), null, 0, null, null, null, null, false, null, null, true, newSessionToken, LocalDateTime.now(), LocalDateTime.now());
            newSession.setIpAddress(hostAddress);
            sessions.getData().add(newSession);
        }

        Response<Void> saveRes = saveAll(sessions.getData());

        if (saveRes.isSuccess()) {
            return Response.success("User logged in successfully", newSessionToken);
        } else {
            return Response.failure(saveRes.getMessage());
        }
    }

    /***
     * Logs out user
     * @param sessionToken session token stored in global state
     * @return a response object with status, message and data
     */
    public Response<Void> logout(UUID sessionToken) throws UnknownHostException {
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

                    session.setUpdatedAt(LocalDateTime.now());

                    Response<Void> saveRes = saveAll(sessions);

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

    /***
     * Checks if user is authenticated
     * @param sessionToken session token stored in global state
     * @return a boolean indicating if user is authenticated
     */
    public Response<Boolean> isAuthenticated(String sessionToken) {
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

    /***
     * Generates a session token
     * @return a session token in UUID format
     */
    private UUID generateSessionToken() {
        return UniqueId.generateType1UUID();
    }
}
