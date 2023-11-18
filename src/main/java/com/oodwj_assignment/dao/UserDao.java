package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.Dao;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Users;

public interface UserDao extends Dao<Users> {
    Response<Boolean> isUsernameTaken(String username);

    Response<Users> getByUsername(String username);
}
