package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class admCreditController {

    @FXML private GridPane topUpGrid;
    @FXML private TextField amountTextField;
    @FXML private TextField nameTextField;
    @FXML private ComboBox<UUID> userIdComboBox;

    public void initialize() throws IOException {
        clearFields();
        // Create a list to hold the credit panes
        List<VBox> creditPanes = new ArrayList<>();

        Map<String, Object> query = Map.of("type", Transactions.transactionType.TopUp, "status", Transactions.transactionStatus.Pending);
        Response<ArrayList<Transactions>> response = DaoFactory.getTransactionDao().read(query);

        if (response.isSuccess()){
            ArrayList<Transactions> topUpTransactions = response.getData();

            for (Transactions transaction : topUpTransactions) {
                // Load the credit pane and set up its controller
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("admCreditPane.fxml"));
                VBox creditPane = fxmlLoader.load();
                admCreditPaneController creditPaneController = fxmlLoader.getController();
                creditPaneController.setAdmCreditController(this);
                creditPaneController.populateTopUpData(transaction);
                creditPane.setUserData(fxmlLoader);
                creditPanes.add(creditPane); // Add the credit pane to the list
            }
        }

        // Add the credit panes to the grid
        int row = 0;
        for (VBox creditPane : creditPanes) {
            topUpGrid.add(creditPane, 0, row);
            row++;
            GridPane.setMargin(creditPane, new Insets(2));
        }

        // Fetch all customer ID into userIdComboBox
        Map<String, Object> userQuery = Map.of("role", Users.Role.Customer);
        Response<ArrayList<Users>> userResponse = DaoFactory.getUserDao().read(userQuery);

        if (userResponse.isSuccess()) {
            ArrayList<Users> customers = userResponse.getData();

            for (Users customer : customers) {
                UUID userId = customer.getId();
                userIdComboBox.getItems().add(userId);
            }
        }

        // Fetch name
        userIdComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (userResponse.isSuccess()) {
                    String name = userResponse.getData().get(0).getName();
                    nameTextField.setText(name);
                } else {
                    nameTextField.clear();
                }
            }
        });
    }

    public void removeCreditPane(UUID transactionId) {
        List<Node> nodesToRemove = new ArrayList<>();
        for (Node node : topUpGrid.getChildren()) {
            if (node instanceof VBox) {
                FXMLLoader loader = (FXMLLoader) node.getUserData();
                admCreditPaneController creditPaneController = loader.getController();
                UUID creditPaneTransactionId = creditPaneController.getTransactionId();

                if (creditPaneTransactionId.equals(transactionId)) {
                    nodesToRemove.add(node);
                }
            }
        }
        topUpGrid.getChildren().removeAll(nodesToRemove);
    }

    public void topUpButtonClicked(ActionEvent event) {
        UUID userId = userIdComboBox.getValue();
        String unformattedAmount = amountTextField.getText();
        String name = nameTextField.getText();

        if (userId == null || unformattedAmount.isEmpty()) {
            admMainController.showAlert("Missing Information", "Please enter values for all fields.");
            return;
        }

        Double amount = validateAndFormatPrice(unformattedAmount);

        if (amount == null) {
            admMainController.showAlert("Invalid Amount", "Invalid amount format. Please use 'RM 7.00' or '7.00' format.");
            return;
        }

        Transactions transaction = new Transactions(UUID.randomUUID(), amount, Transactions.transactionType.TopUp, Transactions.transactionStatus.Completed, userId, userId, LocalDateTime.now(), LocalDateTime.now());
        Response<UUID> newTransaction = DaoFactory.getTransactionDao().create(transaction);

        if (newTransaction.isSuccess()) {
            admMainController.showAlert("Success", "A new Top Up to " + name + "'s wallet successfully.");
            notificationController notificationController = new notificationController();
            notificationController.generateReceiptAndSendNotification(userId, newTransaction.getData());
            topUpToWallet(userId, amount);
            clearFields();
        } else {
            admMainController.showAlert("Error", "Failed to Top Up: " + newTransaction.getMessage());
        }
    }

    public void clearButtonClicked(ActionEvent event) { clearFields(); }

    private Double validateAndFormatPrice(String input) {
        String formattedInput = input.replaceAll("\\s", "").toLowerCase();
        String regexRM = "^(rm)?(\\d+(\\.\\d{0,2})?)$";
        String regexRMWithSpace = "^(rm\\s)?(\\d+(\\.\\d{0,2})?)$";

        if (formattedInput.matches(regexRM) || formattedInput.matches(regexRMWithSpace)) {
            formattedInput = formattedInput.replaceAll("[^0-9.]+", "");
            if (formattedInput.isEmpty()) {
                return 0.00; // Handle invalid inputs as 0.00
            }
            return Double.parseDouble(formattedInput);
        } else {
            return null; // Invalid input, can't be formatted to a Double
        }
    }

    public void topUpToWallet(UUID userId, Double amount) {
        Map<String, Object> query = Map.of("userId", userId);
        Response<ArrayList<Wallets>> response = DaoFactory.getWalletDao().read(query);
        if (response.isSuccess()) {
            Wallets userWallet = response.getData().get(0);
            double newBalance = userWallet.getCredit() + amount;
            newBalance = Double.parseDouble(String.format("%.2f", newBalance));

            Map<String, Object> newValue = Map.of("credit", newBalance, "updatedAt", LocalDateTime.now());
            Response<Void> updateResponse = DaoFactory.getWalletDao().update(query, newValue);
            if (!updateResponse.isSuccess()) {
                admMainController.showAlert("Error", "Failed to update wallet balance: " + updateResponse.getMessage());
            }
        } else {
            admMainController.showAlert("Error", "Failed to access wallet for updating balance: " + response.getMessage());
        }
    }

    private void clearFields() {
        userIdComboBox.setValue(null);
        nameTextField.clear();
        amountTextField.clear();
    }
}
