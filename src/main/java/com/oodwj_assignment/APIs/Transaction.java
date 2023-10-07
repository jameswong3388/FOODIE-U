package com.oodwj_assignment.APIs;

import com.oodwj_assignment.Models.Transactions;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class Transaction {
    private static final String FILE_NAME = "src/main/java/com/oodwj_assignment/Databases/transactions.txt";

    public static Response<UUID> create(Transactions transaction) {
        UUID transactionId = UUID.randomUUID();
        transaction.setTransactionId(transactionId);

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            writer.println(transaction);
            return Response.success("Transaction sent successfully", transactionId);
        } catch (IOException e) {
            return Response.failure("Failed to send transaction: " + e.getMessage());
        }
    }

    public static Response<ArrayList<Transactions>> read(Map<String, Object> query) {
        ArrayList<Transactions> transactions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");

                if (parts.length == 8) {
                    UUID transactionId = UUID.fromString(parts[0]);
                    Double amount = Double.parseDouble(parts[1]);
                    Transactions.transactionType type = Transactions.transactionType.valueOf(parts[2]);
                    Transactions.transactionStatus status = Transactions.transactionStatus.valueOf(parts[3]);
                    UUID payerId = UUID.fromString(parts[4]);
                    UUID payeeId = UUID.fromString(parts[5]);
                    LocalDate updatedAt = LocalDate.parse(parts[6]);
                    LocalDate createdAt = LocalDate.parse(parts[7]);

                    transactions.add(new Transactions(transactionId, amount, type, status, payerId, payeeId, updatedAt, createdAt));
                }
            }
        } catch (IOException e) {
            return Response.failure("Failed to read transactions: " + e.getMessage());
        }

        if (query.isEmpty()) {
            return Response.success("Transactions read successfully", transactions);
        }

        ArrayList<Transactions> matchedTransactions = new ArrayList<>();

        for (Transactions transaction : transactions) {
            Response<Void> matchRes = match(query, transaction);

            if (matchRes.isSuccess()) {
                matchedTransactions.add(transaction);
            }
        }

        return Response.success("Transactions read successfully", matchedTransactions);
    }

    public static Response<Void> update(Map<String, Object> query, Map<String, Object> newValue) {
        Response<ArrayList<Transactions>> transactions = read(query);

        if (transactions.isSuccess()) {
            for (Transactions transaction : transactions.getData()) {
                for (Map.Entry<String, Object> entry : newValue.entrySet()) {
                    String attributeName = entry.getKey();
                    Object expectedValue = entry.getValue();

                    Response<Void> updateRes = transaction.setAttributeValue(attributeName, expectedValue);

                    if (!updateRes.isSuccess()) {
                        return Response.failure(updateRes.getMessage());
                    }
                }
            }

            Response<Void> saveRes = saveAllTransactions(transactions.getData());

            if (saveRes.isSuccess()) {
                return Response.success("Transactions updated successfully");
            } else {
                return Response.failure(saveRes.getMessage());
            }
        } else {
            return Response.failure(transactions.getMessage());
        }
    }

    public static Response<Void> delete(Map<String, Object> query) {
        Response<ArrayList<Transactions>> transactions = read(Map.of());

        if (transactions.isSuccess()) {
            for (Transactions transaction : transactions.getData()) {
                Response<Void> matchRes = match(query, transaction);

                if (matchRes.isSuccess()) {
                    transactions.getData().remove(transaction);
                    Response<Void> saveRes = saveAllTransactions(transactions.getData());

                    if (saveRes.isSuccess()) {
                        return Response.success("Transaction deleted successfully");
                    } else {
                        return Response.failure(saveRes.getMessage());
                    }
                }
            }
            return Response.failure("Transaction not found");

        } else {
            return Response.failure(transactions.getMessage());
        }
    }
    public static Response<Void> deleteAll(Map<String, Object> query) {
        Response<ArrayList<Transactions>> transactions = read(Map.of());

        if (transactions.isSuccess()) {
            for (Transactions transaction : transactions.getData()) {
                Response<Void> matchRes = match(query, transaction);

                if (matchRes.isSuccess()) {
                    transactions.getData().remove(transaction);
                }
            }
            Response<Void> saveRes = saveAllTransactions(transactions.getData());

            if (saveRes.isSuccess()) {
                return Response.success("Transactions deleted successfully");
            } else {
                return Response.failure(saveRes.getMessage());
            }
        } else {
            return Response.failure(transactions.getMessage());
        }
    }

    private static Response<Void> match(Map<String, Object> query, Transactions transaction) {
        boolean match = true;

        for (Map.Entry<String, Object> entry : query.entrySet()) {
            String attributeName = entry.getKey();
            Object expectedValue = entry.getValue();

            Response<Object> actualValue = transaction.getAttributeValue(attributeName);

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
            return Response.success("User found");
        } else {
            return Response.failure("User not found");
        }
    }

    private static Response<Void> saveAllTransactions(ArrayList<Transactions> transactions) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Transactions transaction : transactions) {
                writer.println(transaction);
            }
            return Response.success("Transactions saved successfully");
        } catch (IOException e) {
            return Response.failure("Failed to save transactions: " + e.getMessage());
        }
    }

}
