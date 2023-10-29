package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.Dao;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Sessions;

import java.net.UnknownHostException;
import java.util.UUID;

public interface SessionDao extends Dao<Sessions> {
    Response<UUID> login(String username, String password) throws UnknownHostException;

    Response<Void> logout(UUID sessionToken) throws UnknownHostException;

    Response<Boolean> isAuthenticated(String sessionToken);
}
