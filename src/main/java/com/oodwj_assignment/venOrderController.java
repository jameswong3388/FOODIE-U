package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;

public class venOrderController {

    @FXML private TableView<OrderFoods> orderInfoTableView;
    @FXML private GridPane orderGrid;
    @FXML private Label orderIdLabel;
    @FXML private Label nameLabel;
    @FXML private Label typeLabel;
    @FXML private Label dateLabel;
    @FXML private Label totalLabel;
    @FXML private TableColumn<OrderFoods, String> itemColumn;
    @FXML private TableColumn<OrderFoods, String> priceColumn;
    @FXML private TableColumn<OrderFoods, Integer> quantityColumn;
    @FXML private TableColumn<OrderFoods, String> amountColumn;
    @FXML private Pane orderInfoPane;
    @FXML private Pane noOrderPane;
    private double total;
    private UUID orderId;
    private UUID userId;

    public void initialize() throws IOException {
        // Initialize the order table view columns
        itemColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFoodName()));
        priceColumn.setCellValueFactory(cellData -> {
            double price = cellData.getValue().getFoodPrice();
            String formattedPrice = "RM " + String.format("%.2f", price);
            return new SimpleStringProperty(formattedPrice);
        });
        quantityColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFoodQuantity()));
        amountColumn.setCellValueFactory(cellData -> {
            double amount = cellData.getValue().getAmount();
            String formattedAmount = "RM " + String.format("%.2f", amount);
            return new SimpleStringProperty(formattedAmount);
        });
        orderInfoPane.setVisible(false);

        // Fetch a list of order IDs
        List<UUID> orderIds = venMainController.getOrderIds();

        // Create a list to hold the order panes
        List<VBox> orderPanes = new ArrayList<>();

        if (!orderIds.isEmpty()) {
            for (UUID orderId : orderIds) {
                Map<String, Object> query = Map.of("Id", orderId, "status", Orders.orderStatus.OrderPlaced);
                Response<ArrayList<Orders>> response = DaoFactory.getOrderDao().read(query);

                if (response.isSuccess()) {
                    ArrayList<Orders> pendingOrders = response.getData();

                    for (Orders order : pendingOrders) {
                        // Load the order pane and set up its controller
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("venOrderPane.fxml"));
                        VBox orderPane = fxmlLoader.load();
                        venOrderPaneController orderPaneController = fxmlLoader.getController();
                        orderPaneController.setVenOrderController(this);
                        orderPaneController.populateOrderData(order);
                        orderPane.setUserData(fxmlLoader);
                        orderPanes.add(orderPane); // Add the order pane to the list
                    }
                }
            }
        }

        // Add the order panes to the grid
        int row = 0;
        for (VBox orderPane : orderPanes) {
            orderGrid.add(orderPane, 0, row);
            row++;
            GridPane.setMargin(orderPane, new Insets(2));
        }
    }

    public void setOrderInfo(UUID orderId) {
        this.orderId = orderId;
        orderInfoPane.setVisible(true);
        total = 0.0;

        // Fetch order food information
        Map<String, Object> foodQuery = Map.of("orderId", orderId);
        Response<ArrayList<OrderFoods>> foodResponse = DaoFactory.getOrderFoodDao().read(foodQuery);

        if (foodResponse.isSuccess()) {
            ArrayList<OrderFoods> orderFoodsInfo = foodResponse.getData();

            // Fetch order information
            Map<String, Object> orderQuery = Map.of("Id", orderId);
            Response<ArrayList<Orders>> orderResponse = DaoFactory.getOrderDao().read(orderQuery);
            userId = orderResponse.getData().get(0).getUserId();
            Orders.orderType type = orderResponse.getData().get(0).getType();

            // Fetch user information
            Map<String, Object> userQuery = Map.of("Id", userId);
            Response<ArrayList<Users>> userResponse = DaoFactory.getUserDao().read(userQuery);
            String name = userResponse.getData().get(0).getName();

            DecimalFormat currencyFormat = new DecimalFormat("RM #,##0.00");

            for (OrderFoods orderFood : orderFoodsInfo) {
                double price = orderFood.getFoodPrice();
                int quantity = orderFood.getFoodQuantity();
                double amount = price * quantity;
                orderFood.setAmount(amount);
                total += amount;
            }

            LocalDateTime date = orderFoodsInfo.get(0).getCreatedAt();
            nameLabel.setText(name);
            typeLabel.setText(type.toString());
            orderIdLabel.setText(orderId.toString());
            dateLabel.setText(date.withNano(0).toString());
            totalLabel.setText(currencyFormat.format(total));
            orderInfoTableView.getItems().setAll(orderFoodsInfo);
        } else {
            orderInfoPane.setVisible(false);
            venMainController.showAlert("Error", "Failed to read order foods: " + foodResponse.getMessage());
        }
    }

    public void acceptButtonClicked(ActionEvent event) throws Exception {
        updateOrderStatus(Orders.orderStatus.Accepted, "Order accepted.");

        // Check if it is a delivery order
        Response<ArrayList<Orders>> orderResponse = DaoFactory.getOrderDao().read(Map.of("Id", orderId, "type", Orders.orderType.Delivery));
        if (!orderResponse.isSuccess()) {
            return;
        }

        double deliveryFee = Math.round(total * 0.15);;
        // Set the minimum delivery fee to 3 and maximum to 10
        deliveryFee = Math.max(Math.min(deliveryFee, 10), 3);

        // Assign runnerId and transactionId with a default UUID
        UUID defaultId = new UUID(0L, 0L);

        // Create a delivery task
        Tasks newTask = new Tasks(UUID.randomUUID(), defaultId, orderId, deliveryFee, Tasks.taskStatus.Pending, defaultId, LocalDateTime.now(), LocalDateTime.now());
        Response<UUID> taskResponse = DaoFactory.getTaskDao().create(newTask);
        if (!taskResponse.isSuccess()) {
            venMainController.showAlert("Error", "Failed to create delivery task: " + taskResponse.getMessage());
        }

        notificationController notificationController = new notificationController();
        notificationController.sendNotification(userId, "Store has accepted your order. Order ID: " + orderId, Notifications.notificationType.Information);
    }

    public void rejectButtonClicked(ActionEvent event) throws Exception {
        updateOrderStatus(Orders.orderStatus.Declined, "Order rejected.");
        UUID transactionId = DaoFactory.getOrderDao().read(Map.of("Id", orderId)).getData().get(0).getTransactionId();
        updateTransactionStatus(transactionId, Transactions.transactionStatus.Cancelled);

        double amount = DaoFactory.getTransactionDao().read(Map.of("Id", transactionId)).getData().get(0).getAmount();
        // Refund back to user
        refundAmountToWallet(userId, amount);

        notificationController notificationController = new notificationController();
        notificationController.sendNotification(userId, "Store has rejected your order. Order ID: " + orderId, Notifications.notificationType.Information);
    }

    private void updateOrderStatus(Orders.orderStatus newStatus, String message) throws IOException {
        // Update the order status
        Map<String, Object> query = Map.of("Id", orderId);
        Map<String, Object> newValue = Map.of("status", newStatus, "updatedAt", LocalDateTime.now());

        Response<Void> response = DaoFactory.getOrderDao().update(query, newValue);
        if (response.isSuccess()) {
            // Remove the order pane from the grid
            removeOrderPane(orderId);
            orderInfoPane.setVisible(false);
            venMainController.showAlert("Success", message + " The order status has been updated.");
        } else {
            venMainController.showAlert("Update Error", "Failed to update order status: " + response.getMessage());
        }
    }

    public void updateTransactionStatus(UUID transactionId, Transactions.transactionStatus newStatus) throws IOException {
        // Update the transaction status
        Map<String, Object> query = Map.of("Id", transactionId);
        Map<String, Object> newValue = Map.of("status", newStatus, "updatedAt", LocalDateTime.now());

        Response<Void> response = DaoFactory.getTransactionDao().update(query, newValue);
        if (!response.isSuccess()) {
            venMainController.showAlert("Update Error", "Failed to update transaction status: " + response.getMessage());
        }
    }

    public void refundAmountToWallet(UUID userId, Double amount) {
        Map<String, Object> query = Map.of("userId", userId);
        Response<ArrayList<Wallets>> response = DaoFactory.getWalletDao().read(query);
        if (response.isSuccess()) {
            Wallets userWallet = response.getData().get(0);
            double newBalance = userWallet.getCredit() + amount;
            newBalance = Double.parseDouble(String.format("%.2f", newBalance));

            Map<String, Object> newValue = Map.of("credit", newBalance, "updatedAt", LocalDateTime.now());
            Response<Void> updateResponse = DaoFactory.getWalletDao().update(query, newValue);
            if (!updateResponse.isSuccess()) {
                venMainController.showAlert("Error", "Failed to update wallet balance: " + updateResponse.getMessage());
            }
        } else {
            venMainController.showAlert("Error", "Failed to access wallet for updating balance: " + response.getMessage());
        }
    }

    private void removeOrderPane(UUID orderId) {
        List<Node> nodesToRemove = new ArrayList<>();
        for (Node node : orderGrid.getChildren()) {
            if (node instanceof VBox) {
                FXMLLoader loader = (FXMLLoader) node.getUserData();
                venOrderPaneController orderPaneController = loader.getController();
                UUID orderPaneOrderId = orderPaneController.getOrderId();

                if (orderPaneOrderId.equals(orderId)) {
                    nodesToRemove.add(node);
                }
            }
        }

        orderGrid.getChildren().removeAll(nodesToRemove);
    }
}
