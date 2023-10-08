package com.oodwj_assignment.APIs;

import com.oodwj_assignment.Models.Notifications;
import com.oodwj_assignment.Models.Orders;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class Notification {
    private static final String FILE_NAME = "database/notifications.txt";

    public static Response<UUID> send(Notifications notification) {
        UUID notificationId = UUID.randomUUID();
        notification.setNotificationId(notificationId);

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            writer.println(notification);
            return Response.success("Notification sent successfully", notificationId);
        } catch (IOException e) {
            return Response.failure("Failed to send notification: " + e.getMessage());
        }
    }

    public static Response<Void> readNotification(Map<String, Object> query) {
        Response<ArrayList<Notifications>> notifications = read(Map.of());

        if (notifications.isSuccess()) {
            for (Notifications notification : notifications.getData()) {
                Response<Void> matchRes = match(query, notification);

                if (matchRes.isSuccess()) {
                    notification.setStatus(Notifications.notificationStatus.Read);
                    Response<Void> saveRes = saveAllNotifications(notifications.getData());
                    if (saveRes.isSuccess()) {
                        return Response.success("Notification readNotification", null);
                    } else {
                        return Response.failure(saveRes.getMessage());
                    }
                }
            }
            return Response.failure("Notification not found");

        } else {
            return Response.failure(notifications.getMessage());
        }
    }

    public static Response<ArrayList<Notifications>> read(Map<String, Object> query) {
        ArrayList<Notifications> notifications = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 6) {
                    UUID notificationId = UUID.fromString(parts[0]);
                    String message = parts[1];
                    Notifications.notificationStatus status = Notifications.notificationStatus.valueOf(parts[2]);
                    Notifications.notificationType type = Notifications.notificationType.valueOf(parts[3]);
                    UUID userid = UUID.fromString(parts[4]);
                    LocalDateTime createdAt = LocalDateTime.parse(parts[5]);

                    notifications.add(new Notifications(notificationId, message, status, type, userid, createdAt));
                }
            }
        } catch (IOException e) {
            return Response.failure("Failed to read notifications: " + e.getMessage());
        }

        if (notifications.isEmpty()) {
            return Response.failure("No notifications found");
        }

        if (query.isEmpty()) {
            return Response.success("Notifications read successfully", notifications);
        }

        ArrayList<Notifications> matchedNotifications = new ArrayList<>();

        for (Notifications notification : notifications) {
            Response<Void> matchRes = match(query, notification);

            if (matchRes.isSuccess()) {
                matchedNotifications.add(notification);
            }
        }
        return Response.success("Notification found", matchedNotifications);
    }

    public static Response<Void> delete(Map<String, Object> query) {
        Response<ArrayList<Notifications>> notifications = read(Map.of());

        if (notifications.isSuccess()) {
            for (Notifications notification : notifications.getData()) {
                Response<Void> matchRes = match(query, notification);

                if (matchRes.isSuccess()) {
                    notifications.getData().remove(notification);
                    Response<Void> saveRes = saveAllNotifications(notifications.getData());

                    if (saveRes.isSuccess()) {
                        return Response.success("Notification deleted successfully");
                    } else {
                        return Response.failure(saveRes.getMessage());
                    }
                }
            }
            return Response.failure("Notification not found");
        } else {
            return Response.failure(notifications.getMessage());
        }
    }

    public static Response<Void> deleteAll(Map<String, Object> query) {
        Response<ArrayList<Notifications>> notifications = read(Map.of());

        if (notifications.isSuccess()) {
            for (Notifications notification : notifications.getData()) {
                Response<Void> matchRes = match(query, notification);

                if (matchRes.isSuccess()) {
                    notifications.getData().remove(notification);
                }
            }
            Response<Void> saveRes = saveAllNotifications(notifications.getData());

            if (saveRes.isSuccess()) {
                return Response.success("Notifications deleted successfully");
            } else {
                return Response.failure(saveRes.getMessage());
            }
        } else {
            return Response.failure(notifications.getMessage());
        }
    }

    private static Response<Void> match(Map<String, Object> query, Notifications notification) {
        boolean match = true;

        for (Map.Entry<String, Object> entry : query.entrySet()) {
            String attributeName = entry.getKey();
            Object expectedValue = entry.getValue();

            Response<Object> actualValue = notification.getAttributeValue(attributeName);

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
            return Response.success("Notification found");
        } else {
            return Response.failure("Notification not found");
        }
    }

    private static Response<Void> saveAllNotifications(ArrayList<Notifications> notifications) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Notifications notification : notifications) {
                writer.println(notification.toString());
            }
        } catch (IOException e) {
            return Response.failure(e.getMessage());
        }

        return Response.success("Users saved successfully");
    }

}
