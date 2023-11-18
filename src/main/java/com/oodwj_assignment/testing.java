package com.oodwj_assignment;

import com.oodwj_assignment.dao.UserDao;
import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.dao.byteTesting.UserDaoByte;
import com.oodwj_assignment.dao.byteTesting.UserDaoByteImpl;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Users;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class testing {
    public static void main(String[] args) {
        UserDaoByte usersDao = new UserDaoByteImpl();

        // Start the timer
        long startTime = System.currentTimeMillis(); // or System.nanoTime();

        // Create 10000 users with object bytes, Execution time: 773 milliseconds, File size: 7.3 MB
        // for (int i = 0; i < 10000; i++) {
        //     Response<UUID> res = usersDao.create(new Users(null, "test", "test", Users.Role.Admin, "test", "test", "test", Users.AccountStatus.Pending, LocalDateTime.now(), LocalDateTime.now()));
        //     System.out.println(res.getData());
        //     System.out.println(res.getMessage());
        // }

        // Create 15,000 users with string bytes, Execution time: 204 milliseconds, File size: 1.3 MB
        // for (int i = 0; i < 15000; i++) {
        //     Response<UUID> res = usersDao.create2(new Users(null, "test", "test", Users.Role.Admin, "test", "test", "test", Users.AccountStatus.Pending, LocalDateTime.now(), LocalDateTime.now()));
        //     System.out.println(res.getData());
        //     System.out.println(res.getMessage());
        // }

        // Create 10000 users with text, Execution time: 324 milliseconds, File size: 1.3 MB
        // for (int i = 0; i < 10000; i++) {
        //     Response<UUID> res = DaoFactory.getUserDao().create(new Users(null, "test", "test", Users.Role.Admin, "test", "test", "test", Users.AccountStatus.Pending, LocalDateTime.now(), LocalDateTime.now()));
        //     System.out.println(res.getData());
        //     System.out.println(res.getMessage());
        // }

        // Read 10000 users with object bytes, Execution time: 977 milliseconds
        // readUserWithObjectByte(usersDao);

        // Read 10000 users with string bytes, Execution time: 107 milliseconds
        // readUserWithStringByte(usersDao);

        // Read 1000 users with text, Execution time: 112 milliseconds
        // readUserByText();

        // Stop the timer
        long endTime = System.currentTimeMillis(); // or System.nanoTime();

        // Calculate and print the execution time
        long executionTime = endTime - startTime;
        System.out.println("Execution time: " + executionTime + " milliseconds");
    }

    static void readUserWithObjectByte(UserDaoByte usersDao) {
        Response<ArrayList<Users>> res2 = usersDao.read(Map.of("username", "test"));
        System.out.println(res2.getData());
        System.out.println(res2.getMessage());
    }

    static void readUserWithStringByte(UserDaoByte usersDao) {
        Response<ArrayList<Users>> res2 = usersDao.read2(Map.of("username", "test"));
        System.out.println(res2.getData());
        System.out.println(res2.getMessage());
    }

    static void readUserByText() {
        Response<ArrayList<Users>> res2 = DaoFactory.getUserDao().read(Map.of("username", "test"));
        System.out.println(res2.getData());
        System.out.println(res2.getMessage());
    }

}
