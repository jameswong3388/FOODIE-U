package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.dao.users.UserDaoImpl;
import com.oodwj_assignment.helpers.UniqueId;
import com.oodwj_assignment.dao.base.AbstractDao;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Sessions;
import com.oodwj_assignment.models.Users;
import com.oodwj_assignment.states.AppState;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class SessionDaoImpl extends AbstractDao<Sessions> implements SessionDao {

    private static final File FILE = new File("database/sessions.dat");

    public SessionDaoImpl() {
        super(FILE);
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

        if (user.getData().getAccountStatus() != Users.AccountStatus.Approved) {
            return Response.failure("Your account is pending to be approved, please try again later");
        }

        UUID newSessionToken = generateSessionToken();
        InetAddress localhost = InetAddress.getLocalHost();
        String hostAddress = localhost.getHostAddress();

        Response<ArrayList<Sessions>> sessions = read(Map.of());

        if (sessions.getData().isEmpty()) {
            Sessions newSession = new Sessions(UUID.randomUUID(), user.getData().getId(), LocalDateTime.now(), null, 0, null, null, null, null, true, null, null, true, newSessionToken, LocalDateTime.now(), LocalDateTime.now());
            newSession.setIpAddress(hostAddress);
            sessions.getData().add(newSession);
        } else {
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
                Sessions newSession = new Sessions(UUID.randomUUID(), user.getData().getId(), LocalDateTime.now(), null, 0, null, null, null, null, true, null, null, true, newSessionToken, LocalDateTime.now(), LocalDateTime.now());
                newSession.setIpAddress(hostAddress);
                sessions.getData().add(newSession);
            }
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
                    session.setSessionToken(null);
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
                        AppState.setSessionToken(null);
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
     * @return a boolean indicating if user is authenticated
     */
    public Response<Boolean> isAuthenticated() {
        UUID sessionToken = AppState.getSessionToken();

        if (sessionToken == null) {
            return Response.failure("Session token is null");
        }

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
     * Gets authenticated user
     * @return a response object with status, message and data
     */
    public Response<Users> geAuthenticatedUser() {
        UUID sessionToken = AppState.getSessionToken();

        if (sessionToken == null) {
            return Response.failure("Session token is null");
        }

        Response<ArrayList<Sessions>> res = read(Map.of("sessionToken", sessionToken));

        if (res.isSuccess()) {
            Sessions session = res.getData().get(0);

            if (session.isActive() && session.isAuthenticated()) {
                Response<ArrayList<Users>> user = DaoFactory.getUserDao().read(Map.of("id", session.getUserId()));

                if (user.isSuccess()) {
                    return Response.success("User is read", user.getData().get(0));
                } else {
                    return Response.failure(user.getMessage());
                }
            } else {
                return Response.failure("User is not authenticated");
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
