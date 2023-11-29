package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class cusPaymentController {


    @FXML private TableView<OrderFoods> orderFoodsTableView;
    @FXML private TableColumn<OrderFoods, String> itemColumn;
    @FXML private TableColumn<OrderFoods, String> priceColumn;
    @FXML private TableColumn<OrderFoods, Integer> quantityColumn;
    @FXML private TableColumn<OrderFoods, String> amountColumn;
    @FXML private Button closeButton;
    @FXML private Label subtotalLabel;
    @FXML private Label deliveryLabel;
    @FXML private Label totalLabel;
    private double total;
    private double subtotal = 0.0;
    private double deliveryFee = 0.0;
    private Orders.orderType orderType;
    private UUID userId;
    private UUID storeId;
    private cusMenuController menuController;

    public void initialize() throws IOException {
        userId = cusMainController.userId;
        itemColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFoodName()));
        priceColumn.setCellValueFactory(cellData -> {double price = cellData.getValue().getFoodPrice();
            String formattedPrice = "RM " + String.format("%.2f", price);
            return new SimpleStringProperty(formattedPrice);
        });
        quantityColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFoodQuantity()));
        amountColumn.setCellValueFactory(cellData -> {double amount = cellData.getValue().getAmount();
            String formattedAmount = "RM " + String.format("%.2f", amount);
            return new SimpleStringProperty(formattedAmount);
        });
    }

    public void setOrderFoodsData(ObservableList<OrderFoods> orderFoodsList) {
        orderFoodsTableView.setItems(orderFoodsList);
        generateOrderSummary();
    }

    public void setOrderType(Orders.orderType orderType) { this.orderType = orderType; }
    public void setStoreId(UUID storeId) { this.storeId = storeId; }

    private void generateOrderSummary() {
        for (OrderFoods orderFood : orderFoodsTableView.getItems()) {
            String amountString = amountColumn.getCellData(orderFood).replace("RM ", "");
            subtotal += Double.parseDouble(amountString);
        }
        DecimalFormat currencyFormat = new DecimalFormat("RM #,##0.00");
        subtotalLabel.setText(currencyFormat.format(subtotal));
        if (orderType == Orders.orderType.Delivery){
            // To round to the nearest whole number
            deliveryFee = Math.round(subtotal * 0.15);
            // Set the minimum delivery fee to 3 and maximum to 10
            deliveryFee = Math.max(Math.min(deliveryFee, 10), 3);
        }
        // Take only two decimals
        subtotal = Double.parseDouble(String.format("%.2f", subtotal));
        total = Double.parseDouble(String.format("%.2f", (subtotal + deliveryFee)));
        deliveryLabel.setText(currencyFormat.format(deliveryFee));
        totalLabel.setText(currencyFormat.format(total));
    }

    public void closeButtonClicked(ActionEvent event) {
        Stage paymentStage = (Stage) closeButton.getScene().getWindow();
        paymentStage.close();
    }

    public void placeOrderButtonClicked(ActionEvent event) {
        // Check credit balance before proceeding
        if (!checkCreditBalance()) {
            return;
        }

        // Create a new transaction
        Transactions newTransaction = new Transactions(UUID.randomUUID(), total, Transactions.transactionType.Payment, Transactions.transactionStatus.Pending, userId, storeId, LocalDateTime.now(), LocalDateTime.now());
        Response<UUID> transactionResponse = DaoFactory.getTransactionDao().create(newTransaction);
        if (!transactionResponse.isSuccess()) {
            cusMainController.showAlert("Error", "Failed to process payment: " + transactionResponse.getMessage());
            return;
        }
        UUID transactionId = transactionResponse.getData();
        int totalQuantity = orderFoodsTableView.getItems().stream().mapToInt(OrderFoods::getFoodQuantity).sum();

        // Create a new order
        Orders newOrder = new Orders(UUID.randomUUID(), userId, subtotal, totalQuantity, Orders.orderStatus.OrderPlaced, orderType, transactionId, LocalDateTime.now(), LocalDateTime.now());
        Response<UUID> orderResponse = DaoFactory.getOrderDao().create(newOrder);
        if (!orderResponse.isSuccess()) {
            // Rollback transaction here
            DaoFactory.getTransactionDao().deleteOne(Map.of("Id", transactionId));
            cusMainController.showAlert("Error", "Failed to create order: " + orderResponse.getMessage());
            return;
        }
        UUID orderId = orderResponse.getData();

        for (OrderFoods orderFood : orderFoodsTableView.getItems()) {
            String foodName = orderFood.getFoodName();
            double price = orderFood.getFoodPrice();
            int quantity = orderFood.getFoodQuantity();
            Map<String, Object> query = Map.of("storeId", storeId, "foodName", foodName);
            UUID foodId = DaoFactory.getFoodDao().read(query).getData().get(0).getId();

            OrderFoods newOrderFood = new OrderFoods(UUID.randomUUID(), foodId, orderId, foodName, price, quantity, LocalDateTime.now(), LocalDateTime.now());
            Response<UUID> orderFoodResponse = DaoFactory.getOrderFoodDao().create(newOrderFood);
            if (!orderFoodResponse.isSuccess()) {
                cusMainController.showAlert("Error", "Failed to create order food: " + orderFoodResponse.getMessage());
                // Rolling back the order and transaction
                DaoFactory.getOrderDao().deleteOne(Map.of("Id", orderId));
                DaoFactory.getTransactionDao().deleteOne(Map.of("Id", transactionId));
                return;
            }
        }

        // Deduct the total amount from user's wallet
        deductAmountFromWallet();

        cusMainController.showAlert("Success", "Order placed and payment processed successfully. Order ID: " + orderId);
        notificationController notificationController = new notificationController();
        notificationController.sendNotification(storeId, "New order has been placed. Order ID: " + orderId, Notifications.notificationType.Information);
        closePaymentStage();
        closeMenuStage(event);
    }

    private boolean checkCreditBalance() {
        Response<ArrayList<Wallets>> response = DaoFactory.getWalletDao().read(Map.of("userId", userId));
        if (response.isSuccess()) {
            double availableCredit = response.getData().get(0).getCredit();
            if (availableCredit < total) {
                cusMainController.showAlert("Insufficient Balance", "Please top up your wallet before proceeding with the payment.");
                return false; // Insufficient funds
            }
        } else {
            cusMainController.showAlert("Error", "Failed to check wallet balance: " + response.getMessage());
            return false; // Unable to verify balance
        }
        return true; // Sufficient funds
    }

    private void deductAmountFromWallet() {
        Map<String, Object> query = Map.of("userId", userId);
        Response<ArrayList<Wallets>> response = DaoFactory.getWalletDao().read(query);
        if (response.isSuccess()) {
            Wallets userWallet = response.getData().get(0);
            double newBalance = userWallet.getCredit() - total;
            newBalance = Double.parseDouble(String.format("%.2f", newBalance));

            Map<String, Object> newValue = Map.of("credit", newBalance, "updatedAt", LocalDateTime.now());
            Response<Void> updateResponse = DaoFactory.getWalletDao().update(query, newValue);
            if (!updateResponse.isSuccess()) {
                cusMainController.showAlert("Error", "Failed to update wallet balance: " + updateResponse.getMessage());
            }
        } else {
            cusMainController.showAlert("Error", "Failed to access wallet for updating balance: " + response.getMessage());
        }
    }

    public void setCusMenuController(cusMenuController menuController){ this.menuController = menuController; }

    public void cancelButtonClicked(ActionEvent event) {
        closePaymentStage();
    }

    private void closePaymentStage(){
        Stage paymentStage = (Stage) closeButton.getScene().getWindow();
        paymentStage.close();
    }

    private void closeMenuStage(ActionEvent event){
        menuController.backButtonClicked(event);
    }
}
