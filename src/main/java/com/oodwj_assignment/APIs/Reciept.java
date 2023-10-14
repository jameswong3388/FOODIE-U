package com.oodwj_assignment.APIs;

import com.oodwj_assignment.Models.Receipts;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class Reciept {
    private static final String FILE_NAME = "database/receipts.txt";

    public static Response<UUID> create(Receipts receipt) {
        UUID receiptId = UUID.randomUUID();
        receipt.setReceiptId(receiptId);

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            writer.println(receipt);
            return Response.success("Notification sent successfully", receiptId);
        } catch (IOException e) {
            return Response.failure("Failed to send notification: " + e.getMessage());
        }
    }

    public static Response<ArrayList<Receipts>> read(Map<String, Object> query) {
        ArrayList<Receipts> matchedReceipts = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");

                if (parts.length != 5) {
                    continue;
                }

                Receipts receipt = parseReceipt(parts);

                if (receipt != null) {
                    if (query.isEmpty() || match(query, receipt).isSuccess()) {
                        matchedReceipts.add(receipt);
                    }
                }
            }
        } catch (IOException e) {
            return Response.failure("Failed to read receipts: " + e.getMessage());
        }

        if (matchedReceipts.isEmpty()) {
            return Response.failure("No receipts match the query", matchedReceipts);
        }

        return Response.success("Receipts read successfully", matchedReceipts);
    }

    public static Response<Void> delete(Map<String, Object> query) {
        Response<ArrayList<Receipts>> receipts = read(Map.of());

        if (receipts.isSuccess()) {
            for (Receipts receipt : receipts.getData()) {
                Response<Void> matchRes = match(query, receipt);

                if (matchRes.isSuccess()) {
                    receipts.getData().remove(receipt);
                    Response<Void> saveRes = saveAllReceipts(receipts.getData());

                    if (saveRes.isSuccess()) {
                        return Response.success("Receipt deleted successfully");
                    } else {
                        return Response.failure(saveRes.getMessage());
                    }
                }
            }
            return Response.failure("Receipt not found");

        } else {
            return Response.failure(receipts.getMessage());
        }
    }

    public static Response<Void> deleteAll(Map<String, Object> query) {
        Response<ArrayList<Receipts>> receipts = read(Map.of());

        if (receipts.isSuccess()) {
            for (Receipts receipt : receipts.getData()) {
                Response<Void> matchRes = match(query, receipt);

                if (matchRes.isSuccess()) {
                    receipts.getData().remove(receipt);
                }
            }
            Response<Void> saveRes = saveAllReceipts(receipts.getData());

            if (saveRes.isSuccess()) {
                return Response.success("Receipts deleted successfully");
            } else {
                return Response.failure(saveRes.getMessage());
            }
        } else {
            return Response.failure(receipts.getMessage());
        }
    }

    private static Response<Void> match(Map<String, Object> query, Receipts receipts) {
        boolean match = true;

        for (Map.Entry<String, Object> entry : query.entrySet()) {
            String attributeName = entry.getKey();
            Object expectedValue = entry.getValue();

            Response<Object> actualValue = receipts.getAttributeValue(attributeName);

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

    private static Response<Void> saveAllReceipts(ArrayList<Receipts> receipts) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Receipts receipt : receipts) {
                writer.println(receipt.toString());
            }
        } catch (IOException e) {
            return Response.failure(e.getMessage());
        }

        return Response.success("Users saved successfully");
    }

    private static Receipts parseReceipt(String[] parts) {
        try {
            UUID receiptId = UUID.fromString(parts[0]);
            UUID userId = UUID.fromString(parts[1]);
            Double credit = Double.parseDouble(parts[2]);
            LocalDateTime updatedAt = LocalDateTime.parse(parts[3]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[4]);

            return new Receipts(receiptId, userId, credit, updatedAt, createdAt);
        } catch (Exception e) {
            return null;
        }
    }
}
