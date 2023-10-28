package com.oodwj_assignment.Dao;

import com.oodwj_assignment.Dao.Base.Dao;
import com.oodwj_assignment.Helpers.Response;
import com.oodwj_assignment.Models.Users;

public interface UserDao extends Dao<Users> {
    Response<Boolean> isUsernameTaken(String username);

    Response<Users> getByUsername(String username);
}
