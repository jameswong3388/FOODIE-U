package com.oodwj_assignment.APIs;

import com.oodwj_assignment.Models.Wallets;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class Wallet {
    private static final String FILE_NAME = "src/main/java/com/oodwj_assignment/Databases/wallets.txt";

    public static Response<Void> create(Wallets wallet) {
        UUID walletId = UUID.randomUUID();
        wallet.setWalletId(walletId);

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            writer.println(wallet);
            return Response.success("Wallet created successfully");
        } catch (IOException e) {
            return Response.failure("Failed to create wallet: " + e.getMessage());
        }
    }

    public static Response<ArrayList<Wallets>> read(Map<String, Object> query) {
        ArrayList<Wallets> wallets = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 5) {
                    UUID walletId = UUID.fromString(parts[0]);
                    UUID userId = UUID.fromString(parts[1]);
                    double balance = Double.parseDouble(parts[2]);
                    LocalDate createdAt = LocalDate.parse(parts[3]);
                    LocalDate updatedAt = LocalDate.parse(parts[4]);

                    wallets.add(new Wallets(walletId, userId, balance, createdAt, updatedAt));
                }
            }
        } catch (IOException e) {
            return Response.failure("Failed to read wallets: " + e.getMessage());
        }

        if (query.isEmpty()) {
            return Response.success("Wallets read successfully", wallets);
        }

        ArrayList<Wallets> matchWallets = new ArrayList<>();

        for (Wallets wallet : wallets) {
            Response<Void> matchRes = match(query, wallet);

            if (matchRes.isSuccess()) {
                matchWallets.add(wallet);
            }
        }

        return Response.success("Wallets read successfully", matchWallets);
    }

    public static Response<Void> update(Map<String, Object> query, Map<String, Object> newValue) {
        // query is used to find the wallet to update.
        // newValue is used to update the wallet. key is the attribute name, value is the new value.

        Response<ArrayList<Wallets>> wallets = read(query);

        System.out.println(wallets.getData());

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


}
