package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.Dao;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Sessions;
import com.oodwj_assignment.models.Users;

import java.net.UnknownHostException;
import java.util.UUID;

public interface SessionDao extends Dao<Sessions> {
    /***
     * Authenticates user
     *
     * @param username username of user
     * @param password password of user
     * @return session token
     * @throws UnknownHostException if host is unknown
     */
    Response<UUID> login(String username, String password) throws UnknownHostException;

    /***
     * Logs out user
     * @param sessionToken session token stored in global state
     * @return a response object with status, message and data
     */
    Response<Void> logout(UUID sessionToken) throws UnknownHostException;

    /***
     * Checks if user is authenticated
     * @return a boolean indicating if user is authenticated
     */
    Response<Boolean> isAuthenticated();

    /***
     * Gets authenticated user
     * @return a response object with status, message and data
     */
    Response<Users> geAuthenticatedUser();
}
