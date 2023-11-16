package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.OrderFoods;
import com.oodwj_assignment.models.Orders;
import com.oodwj_assignment.models.Transactions;
import com.oodwj_assignment.models.Wallets;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.TilePane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class cusWalletController {

    @FXML private TableView<Transactions> transactionTableView;
    @FXML private TableColumn<Transactions, UUID> transactionIdColumn;
    @FXML private TableColumn<Transactions, LocalDateTime> dateColumn;
    @FXML private TableColumn<Transactions, Transactions.transactionType> typeColumn;
    @FXML private TableColumn<Transactions, Transactions.transactionStatus> statusColumn;
    @FXML private TableColumn<Transactions, UUID> payeeIdColumn;
    @FXML private TableColumn<Transactions, String> amountColumn;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private ComboBox<String> optionComboBox;
    @FXML private Label creditLabel;
    private UUID userId;

    public void initialize(){
        // Initialize the transaction table view columns
        transactionIdColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        dateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCreatedAt()));
        typeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getType()));
        statusColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStatus()));
        payeeIdColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPayeeId()));
        amountColumn.setCellValueFactory(cellData -> {
            double amount = cellData.getValue().getAmount();
            String formattedAmount = "RM " + String.format("%.2f", amount);
            return new SimpleStringProperty(formattedAmount);
        });

        // Fetch wallet credit
        userId = cusMainController.userId;
        Map<String, Object> query = Map.of("userId", userId);
        Response<ArrayList<Wallets>> response = DaoFactory.getWalletDao().read(query);

        if (response.isSuccess()) {
            Wallets wallets = response.getData().get(0);
            creditLabel.setText(String.format("RM %.2f", wallets.getCredit()));
        }

        typeComboBox.getItems().addAll("Date", "Type", "Status");
        typeComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateOptionComboBox(newVal));
        loadTransactions();
    }

    private void loadTransactions() {
        Map<String, Object> query = Map.of("payerId", userId);
        Response<ArrayList<Transactions>> response = DaoFactory.getTransactionDao().read(query);

        if (response.isSuccess()) {
            ArrayList<Transactions> transactions = response.getData();
            transactionTableView.getItems().setAll(transactions);
        }
        else {
            System.out.println("Fails to get transaction history.");
        }
    }

    private void updateOptionComboBox(String filterType) {
        optionComboBox.getItems().clear();
        if ("Date".equals(filterType)) {
            optionComboBox.getItems().addAll("All", "Today", "This Week", "This Month");
        } else if ("Type".equals(filterType)) {
            optionComboBox.getItems().addAll("All");
            optionComboBox.getItems().addAll(Arrays.stream(Transactions.transactionType.values()).map(Enum::name).collect(Collectors.toList()));
        } else if ("Status".equals(filterType)) {
            optionComboBox.getItems().addAll("All");
            optionComboBox.getItems().addAll(Arrays.stream(Transactions.transactionStatus.values()).map(Enum::name).collect(Collectors.toList()));
        }
    }

    public void topUpButtonClicked(ActionEvent s) throws IOException {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Request Top Up");
        dialog.setHeaderText(null);
        dialog.setContentText("Please enter the top up amount:");
        Optional<String> result = dialog.showAndWait();

        if (result.isEmpty()){
            return;
        } else if (result.get().isEmpty()) {
            cusMainController.showAlert("Missing Information", "Please enter an amount for top up.");
            return;
        }
        String input = result.get();
        Double amount = validateAndFormatPrice(input);
        if (amount == null) {
            cusMainController.showAlert("Invalid Amount", "Invalid price format. Please use 'RM 7.00' or '7.00' format.");
            return;
        }

        Transactions transaction = new Transactions(UUID.randomUUID(), amount, Transactions.transactionType.TopUp, Transactions.transactionStatus.Pending, userId, userId, LocalDateTime.now(), LocalDateTime.now());
        Response<UUID> topUpResponse = DaoFactory.getTransactionDao().create(transaction);

        if (topUpResponse.isSuccess()) {
            UUID transactionId = topUpResponse.getData();
            cusMainController.showAlert("Success", "Top-up request submitted successfully. Transaction ID: " + transactionId);
            loadTransactions();
        } else {
            cusMainController.showAlert("Error", "Failed to submit top-up request: " + topUpResponse.getMessage());
        }

    }

    public void resetButtonClicked(ActionEvent event) {
        typeComboBox.getSelectionModel().clearSelection();
        optionComboBox.getItems().clear();
        loadTransactions();
    }

    public void applyButtonClicked(ActionEvent event) {
        String selectedFilter = typeComboBox.getSelectionModel().getSelectedItem();
        String selectedOption = optionComboBox.getSelectionModel().getSelectedItem();

        if (selectedFilter == null || selectedOption == null) {
            cusMainController.showAlert("Error", "Please select both a filter type and an option.");
            return;
        }

        applyFilter(selectedFilter, selectedOption);
    }

    private void applyFilter(String filterType, String filterOption) {
        // Fetch transactions as before
        Response<ArrayList<Transactions>> response = DaoFactory.getTransactionDao().read(Map.of("payerId", userId));
        if (!response.isSuccess()) {
            System.out.println("Fails to get transaction history.");
            return;
        }

        ArrayList<Transactions> transactions = response.getData();
        Stream<Transactions> filteredStream = transactions.stream();

        // Apply filters based on the selected type and option
        if ("Date".equals(filterType)) {
            filteredStream = applyDateFilter(filteredStream, filterOption);
        } else if ("Type".equals(filterType)) {
            filteredStream = applyTypeFilter(filteredStream, filterOption);
        } else if ("Status".equals(filterType)) {
            filteredStream = applyStatusFilter(filteredStream, filterOption);
        }

        // Set filtered transactions to the table view
        transactionTableView.getItems().setAll(filteredStream.collect(Collectors.toList()));
    }

    private Stream<Transactions> applyDateFilter(Stream<Transactions> stream, String option) {
        LocalDateTime now = LocalDateTime.now();
        switch (option) {
            case "Today":
                return stream.filter(t -> t.getCreatedAt().toLocalDate().equals(now.toLocalDate()));
            case "This Week":
                return stream.filter(t -> t.getCreatedAt().isAfter(now.minusWeeks(1)));
            case "This Month":
                return stream.filter(t -> t.getCreatedAt().isAfter(now.minusMonths(1)));
            default: // "All"
                return stream;
        }
    }

    private Stream<Transactions> applyTypeFilter(Stream<Transactions> stream, String option) {
        if ("All".equals(option)) {
            return stream;
        }
        return stream.filter(t -> t.getType().name().equals(option));
    }

    private Stream<Transactions> applyStatusFilter(Stream<Transactions> stream, String option) {
        if ("All".equals(option)) {
            return stream;
        }
        return stream.filter(t -> t.getStatus().name().equals(option));
    }

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

}
