package com.oodwj_assignment.APIs;

import com.oodwj_assignment.Models.Attachments;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class Attachment {
    private static final String FILE_NAME = "database/attachments.txt";

    public static Response<UUID> create(Attachments attachment) {
        UUID attachmentId = UUID.randomUUID();
        attachment.setAttachmentId(attachmentId);

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            writer.println(attachment);
            return Response.success("Attachment sent successfully", attachmentId);
        } catch (IOException e) {
            return Response.failure("Failed to send attachment: " + e.getMessage());
        }
    }

    public static Response<ArrayList<Attachments>> read(Map<String, Object> query) {
        ArrayList<Attachments> matchedAttachments = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");

                if (parts.length != 6) {
                    continue;
                }

                Attachments attachment = parseAttachment(parts);

                if (attachment != null) {
                    if (query.isEmpty() || match(query, attachment).isSuccess()) {
                        matchedAttachments.add(attachment);
                    }
                }
            }
        } catch (IOException e) {
            return Response.failure("Failed to read attachments: " + e.getMessage());
        }

        if (matchedAttachments.isEmpty()) {
            return Response.failure("No attachments match the query", matchedAttachments);
        }

        return Response.success("Attachments read successfully", matchedAttachments);
    }

    public static Response<Void> match(Map<String, Object> query, Attachments attachment) {
        boolean match = true;

        for (Map.Entry<String, Object> entry : query.entrySet()) {
            String attributeName = entry.getKey();
            Object expectedValue = entry.getValue();

            Response<Object> actualValue = attachment.getAttributeValue(attributeName);

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

    private static Attachments parseAttachment(String[] parts) {
        try {
            UUID attachmentId = UUID.fromString(parts[0]);
            UUID userId = UUID.fromString(parts[1]);
            UUID notificationId = UUID.fromString(parts[2]);
            Attachments.attachmentType attachment = Attachments.attachmentType.valueOf(parts[3]);
            LocalDateTime updatedAt = LocalDateTime.parse(parts[4]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[5]);

            return new Attachments(attachmentId, userId, notificationId, attachment, updatedAt, createdAt);
        } catch (Exception e) {
            return null;
        }
    }
}
