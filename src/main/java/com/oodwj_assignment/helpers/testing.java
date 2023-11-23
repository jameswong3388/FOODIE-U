package com.oodwj_assignment.helpers;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Users;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class testing {
    public static void main(String[] args) throws InterruptedException {
        // Start the timer
        long startTime = System.currentTimeMillis(); // or System.nanoTime();

        // Create 10,000 users with Binary Object, Execution time: 91815 milliseconds, File size: 1.1 MB
        // for (int i = 0; i < 10000; i++) {
        //     Response<UUID> res = usersDao.create(new Users(null, "test", "test", Users.Role.Admin, "test", "test", "test", Users.AccountStatus.Pending, LocalDateTime.now(), LocalDateTime.now()));
        //     System.out.println(res.getData());
        //     System.out.println(res.getMessage());
        // }

        // Create 10,000 users with Text, Execution time: 320 milliseconds, File size: 1.3 MB
        // for (int i = 0; i < 10000; i++) {
        //     Response<UUID> res = DaoFactory.getUserDao().create(new Users(null, "test", "test", Users.Role.Admin, "test", "test", "test", Users.AccountStatus.Pending, LocalDateTime.now(), LocalDateTime.now()));
        //     System.out.println(res.getData());
        //     System.out.println(res.getMessage());
        // }

        // Read 10,000 users with Binary Object, Execution time: 74 milliseconds
        // readUserWithObjectByte(usersDao);


        // Read 10,000 users with text, Execution time: 84 milliseconds
        // readUserByText();

        // Stop the timer
        long endTime = System.currentTimeMillis(); // or System.nanoTime();

        // Calculate and print the execution time
        long executionTime = endTime - startTime;
        System.out.println("Execution time: " + executionTime + " milliseconds");
    }
}
