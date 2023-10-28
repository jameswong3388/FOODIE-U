package com.oodwj_assignment.Dao;

import com.oodwj_assignment.Dao.Base.Dao;
import com.oodwj_assignment.Helpers.Response;
import com.oodwj_assignment.Models.Sessions;

import java.net.UnknownHostException;
import java.util.UUID;

public interface SessionDao extends Dao<Sessions> {
    Response<UUID> login(String username, String password) throws UnknownHostException;

    Response<Void> logout(UUID sessionToken) throws UnknownHostException;

    Response<Boolean> isAuthenticated(String sessionToken);
}
