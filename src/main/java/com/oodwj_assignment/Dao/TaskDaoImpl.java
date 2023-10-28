package com.oodwj_assignment.Dao;

import com.oodwj_assignment.Dao.Base.AbstractDao;
import com.oodwj_assignment.Models.Tasks;

import java.time.LocalDateTime;
import java.util.UUID;

public class TaskDaoImpl extends AbstractDao<Tasks> implements TaskDao {

    private static final String FILE_NAME = "database/tasks.txt";

    public TaskDaoImpl() {
        super(FILE_NAME);
    }

    @Override
    public Tasks parse(String[] parts) {
        try {
            UUID taskId = UUID.fromString(parts[0]);
            UUID runnerId = UUID.fromString(parts[1]);
            UUID orderId = UUID.fromString(parts[2]);
            Double deliveryFee = Double.parseDouble(parts[3]);
            Tasks.taskStatus status = Tasks.taskStatus.valueOf(parts[4]);
            LocalDateTime updatedAt = LocalDateTime.parse(parts[5]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[6]);

            return new Tasks(taskId, runnerId, orderId, deliveryFee, status, updatedAt, createdAt);
        } catch (Exception e) {
            return null;
        }
    }
}
