package com.oodwj_assignment.APIs;

import com.oodwj_assignment.Models.Wallets;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class Wallet {
    private static final String FILE_NAME = "database/wallets.txt";

    public static Response<UUID> create(Wallets wallet) {
        UUID walletId = UUID.randomUUID();
        wallet.setWalletId(walletId);

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            writer.println(wallet);
            return Response.success("Wallet created successfully", walletId);
        } catch (IOException e) {
            return Response.failure("Failed to create wallet: " + e.getMessage());
        }
    }

    public static Response<ArrayList<Wallets>> read(Map<String, Object> query) {
        ArrayList<Wallets> matchWallets = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");

                if (parts.length != 5) {
                    continue;
                }

                Wallets wallet = parseWallet(parts);

                if (wallet != null) {
                    if (query.isEmpty() || match(query, wallet).isSuccess()) {
                        matchWallets.add(wallet);
                    }
                }
            }
        } catch (IOException e) {
            return Response.failure("Failed to read wallets.txt: " + e.getMessage());
        }

        if (matchWallets.isEmpty()) {
            return Response.failure("No wallets match the query", matchWallets);
        }

        return Response.success("Wallets read successfully", matchWallets);
    }

    public static Response<Void> update(Map<String, Object> query, Map<String, Object> newValue) {
        Response<ArrayList<Wallets>> wallets = read(query);

        if (wallets.isSuccess()) {
            for (Wallets wallet : wallets.getData()) {
                for (Map.Entry<String, Object> entry : newValue.entrySet()) {
                    String attributeName = entry.getKey();
                    Object expectedValue = entry.getValue();

                    Response<Void> updateRes = wallet.setAttributeValue(attributeName, expectedValue);

                    if (!updateRes.isSuccess()) {
                        return Response.failure(updateRes.getMessage());
                    }
                }
            }

            Response<Void> saveRes = saveAllWallets(wallets.getData());

            if (saveRes.isSuccess()) {
                return Response.success("Wallets updated successfully");
            } else {
                return Response.failure(saveRes.getMessage());
            }
        } else {
            return Response.failure(wallets.getMessage());
        }

    }

    public static Response<Void> delete(Map<String, Object> query) {
        Response<ArrayList<Wallets>> wallets = read(Map.of());

        if (wallets.isSuccess()) {
            for (Wallets wallet : wallets.getData()) {
                Response<Void> matchRes = match(query, wallet);

                if (matchRes.isSuccess()) {
                    wallets.getData().remove(wallet);
                    Response<Void> saveRes = saveAllWallets(wallets.getData());

                    if (saveRes.isSuccess()) {
                        return Response.success("Wallet deleted successfully");
                    } else {
                        return Response.failure(saveRes.getMessage());
                    }
                }
            }
            return Response.failure("Wallet not found");

        } else {
            return Response.failure(wallets.getMessage());
        }
    }

    private static Response<Void> match(Map<String, Object> query, Wallets wallet) {
        boolean match = true;

        for (Map.Entry<String, Object> entry : query.entrySet()) {
            String attributeName = entry.getKey();
            Object expectedValue = entry.getValue();

            Response<Object> actualValue = wallet.getAttributeValue(attributeName);

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
            return Response.success("Wallet found");
        } else {
            return Response.failure("Wallet not found");
        }
    }


    // Utility method to save all Wallets' data to the text file.
    private static Response<Void> saveAllWallets(ArrayList<Wallets> Wallets) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Wallets wallet : Wallets) {
                writer.println(wallet.toString());
            }
        } catch (IOException e) {
            return Response.failure(e.getMessage());
        }

        return Response.success("Wallets saved successfully");
    }

    private static Wallets parseWallet(String[] parts) {
        try {
            UUID walletId = UUID.fromString(parts[0]);
            UUID userId = UUID.fromString(parts[1]);
            double balance = Double.parseDouble(parts[2]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[3]);
            LocalDateTime updatedAt = LocalDateTime.parse(parts[4]);

            return new Wallets(walletId, userId, balance, createdAt, updatedAt);
        } catch (Exception e) {
            return null;
        }
    }
}
