package com.oodwj_assignment.APIs;

import com.oodwj_assignment.Models.Tasks;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class Task {
    private static final String FILE_NAME = "database/tasks.txt";

    public static Response<UUID> create(Tasks task) {
        UUID taskId = UUID.randomUUID();
        task.setTaskId(taskId);

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            writer.println(task);

            return Response.success("Task created successfully", taskId);
        } catch (IOException e) {
            return Response.failure("Failed to create task: " + e.getMessage());
        }
    }

    public static Response<ArrayList<Tasks>> read(Map<String, Object> query) {
        ArrayList<Tasks> tasks = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 7) {
                    UUID taskId = UUID.fromString(parts[0]);
                    UUID runnerId = UUID.fromString(parts[1]);
                    UUID orderId = UUID.fromString(parts[2]);
                    Integer deliveryFee = Integer.parseInt(parts[3]);
                    Tasks.taskStatus status = Tasks.taskStatus.valueOf(parts[4]);
                    LocalDateTime updatedAt = LocalDateTime.parse(parts[5]);
                    LocalDateTime createdAt = LocalDateTime.parse(parts[6]);

                    tasks.add(new Tasks(taskId, runnerId, orderId, deliveryFee, status, updatedAt, createdAt));

                }
            }
        } catch (IOException e) {
            return Response.failure("Failed to read tasks: " + e.getMessage());
        }

        if (tasks.isEmpty()) {
            return Response.failure("No tasks found");
        }

        if (query.isEmpty()) {
            return Response.success("Tasks read successfully", tasks);
        }

        ArrayList<Tasks> matchTasks = new ArrayList<>();

        for (Tasks task : tasks) {
            Response<Void> matchRes = match(query, task);

            if (matchRes.isSuccess()) {
                matchTasks.add(task);
            }
        }

        return Response.success("Tasks read successfully", matchTasks);
    }

    public static Response<Void> update(Map<String, Object> query, Map<String, Object> newValue) {
        Response<ArrayList<Tasks>> tasks = read(Map.of());

        if (tasks.isSuccess()) {
            for (Tasks task : tasks.getData()) {
                Response<Void> matchRes = match(query, task);

                if (matchRes.isSuccess()) {
                    for (Map.Entry<String, Object> entry : newValue.entrySet()) {
                        String attributeName = entry.getKey();
                        Object expectedValue = entry.getValue();

                        Response<Void> updateRes = task.setAttributeValue(attributeName, expectedValue);

                        if (!updateRes.isSuccess()) {
                            return Response.failure(updateRes.getMessage());
                        }
                    }

                    Response<Void> saveRes = saveAllTasks(tasks.getData());

                    if (saveRes.isSuccess()) {
                        return Response.success("Task updated successfully");
                    } else {
                        return Response.failure(saveRes.getMessage());
                    }
                }
            }

            return Response.failure("Task not found");
        } else {
            return Response.failure(tasks.getMessage());
        }
    }

    public static Response<Void> delete(Map<String, Object> query) {
        Response<ArrayList<Tasks>> tasks = read(Map.of());

        if (tasks.isSuccess()) {
            for (Tasks task : tasks.getData()) {
                Response<Void> matchRes = match(query, task);

                if (matchRes.isSuccess()) {
                    tasks.getData().remove(task);
                    Response<Void> saveRes = saveAllTasks(tasks.getData());

                    if (saveRes.isSuccess()) {
                        return Response.success("Task deleted successfully");
                    } else {
                        return Response.failure(saveRes.getMessage());
                    }
                }
            }
            return Response.failure("Task not found");
        } else {
            return Response.failure(tasks.getMessage());
        }
    }

    private static Response<Void> match(Map<String, Object> query, Tasks task) {
        boolean match = true;

        for (Map.Entry<String, Object> entry : query.entrySet()) {
            String attributeName = entry.getKey();
            Object expectedValue = entry.getValue();

            Response<Object> actualValue = task.getAttributeValue(attributeName);

            if (actualValue.isSuccess()) {
                if (!actualValue.getData().equals(expectedValue)) {
                    match = false;
                    break;
                }
            } else {
                return Response.failure(actualValue.getMessage());
            }
        }
        if (match) {
            return Response.success("Task matched successfully");
        } else {
            return Response.failure("Task not found");
        }
    }

    private static Response<Void> saveAllTasks(ArrayList<Tasks> tasks) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Tasks task : tasks) {
                writer.println(task.toString());
            }
        } catch (IOException e) {
            return Response.failure(e.getMessage());
        }

        return Response.success("Tasks saved successfully");
    }
}
