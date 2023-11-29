package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class cusOrderController {

    @FXML private TableView<OrderFoods> orderFoodsTableView;
    @FXML private TableColumn<OrderFoods, String> itemColumn;
    @FXML private TableColumn<OrderFoods, String> priceColumn;
    @FXML private TableColumn<OrderFoods, Integer> quantityColumn;
    @FXML private TableColumn<OrderFoods, String> amountColumn;
    @FXML private ComboBox<UUID> orderIdComboBox;
    @FXML private Circle xshape1;
    @FXML private Rectangle xshape2;
    @FXML private Circle xshape3;
    @FXML private Rectangle xshape4;
    @FXML private Circle xshape5;
    @FXML private Rectangle xshape6;
    @FXML private Circle xshape7;
    @FXML private Circle yshape1;
    @FXML private Rectangle yshape2;
    @FXML private Circle yshape3;
    @FXML private Rectangle yshape4;
    @FXML private Circle yshape5;
    @FXML private Rectangle yshape6;
    @FXML private Circle yshape7;
    @FXML private Circle zshape1;
    @FXML private Rectangle zshape2;
    @FXML private Circle zshape3;
    @FXML private Rectangle zshape4;
    @FXML private Circle zshape5;
    @FXML private Rectangle zshape6;
    @FXML private Circle zshape7;
    @FXML private Label xstatus1;
    @FXML private Label xstatus2;
    @FXML private Label xstatus3;
    @FXML private Label xstatus4;
    @FXML private Label ystatus1;
    @FXML private Label ystatus2;
    @FXML private Label ystatus3;
    @FXML private Label ystatus4;
    @FXML private Label zstatus1;
    @FXML private Label zstatus2;
    @FXML private Label zstatus3;
    @FXML private Label zstatus4;
    @FXML private Label resNameLabel;
    @FXML private Label typeLabel;
    @FXML private Label priceLabel;
    @FXML private Label quantityLabel;
    @FXML private AnchorPane xpane;
    @FXML private AnchorPane ypane;
    @FXML private Button cancelButton;
    private UUID storeId;
    private String defaultColor = "#d9dfe9";
    private String updatedColor = "#ffc75a";

    public void initialize(){
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
        xpane.toFront();

        setOrderIdComboBox();
    }

    private void setOrderIdComboBox(){
        orderIdComboBox.getItems().clear();

        UUID userId = cusMainController.userId;
        Map<String, Object> query = Map.of("userId", userId);
        Response<ArrayList<Orders>> response = DaoFactory.getOrderDao().read(query);

        if (response.isSuccess()) {
            ArrayList<Orders> orders = response.getData();

            for (Orders order : orders) {
                if (isActiveOrder(order)) {
                    UUID orderId = order.getId();
                    orderIdComboBox.getItems().add(orderId);
                }
            }
        }

        // Set a listener on the ComboBox
        orderIdComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fetchOrderInfo(newValue);
            }
        });

        // Display the first order's details initially
        if (!orderIdComboBox.getItems().isEmpty()) {
            orderIdComboBox.getSelectionModel().selectFirst();
            fetchOrderInfo(orderIdComboBox.getValue());
        }
    }
    private void fetchOrderInfo(UUID orderId) {
        // Fetch order food information
        Map<String, Object> orderFoodQuery = Map.of("orderId", orderId);
        Response<ArrayList<OrderFoods>> orderFoodResponse = DaoFactory.getOrderFoodDao().read(orderFoodQuery);

        if (orderFoodResponse.isSuccess()) {
            ArrayList<OrderFoods> orderFoodsInfo = orderFoodResponse.getData();

            // Fetch order information
            Map<String, Object> orderQuery = Map.of("Id", orderId);
            Response<ArrayList<Orders>> orderResponse = DaoFactory.getOrderDao().read(orderQuery);
            Orders order = orderResponse.getData().get(0);
            Orders.orderType type = order.getType();
            Double totalPrice = order.getTotalPrice();
            Integer totalQuantity = order.getTotalQuantity();

            // Fetch store information
            UUID foodId = orderFoodResponse.getData().get(0).getFoodId();
            Response<ArrayList<Foods>> foodResponse = DaoFactory.getFoodDao().read(Map.of("Id", foodId));
            storeId = foodResponse.getData().get(0).getStoreId();
            Response<ArrayList<Stores>> storeResponse = DaoFactory.getStoreDao().read(Map.of("Id", storeId));
            String name = storeResponse.getData().get(0).getName();

            DecimalFormat currencyFormat = new DecimalFormat("RM #,##0.00");

            for (OrderFoods orderFood : orderFoodsInfo) {
                double price = orderFood.getFoodPrice();
                int quantity = orderFood.getFoodQuantity();
                double amount = price * quantity;
                orderFood.setAmount(amount);
            }

            resNameLabel.setText(name);
            typeLabel.setText(type.toString());
            priceLabel.setText(currencyFormat.format(totalPrice));
            quantityLabel.setText(totalQuantity.toString());
            orderFoodsTableView.getItems().setAll(orderFoodsInfo);
        } else {
            cusMainController.showAlert("Error", "Failed to read order foods: " + orderFoodResponse.getMessage());
        }

        // Set Status Pane
        setOrderStatusPane(orderId);
    }

    private boolean isActiveOrder(Orders order) {
        if (order.getType() == Orders.orderType.Delivery) {
            Map<String, Object> taskQuery = Map.of("orderId", order.getId());
            Response<ArrayList<Tasks>> taskResponse = DaoFactory.getTaskDao().read(taskQuery);

            if (taskResponse.isSuccess() && !taskResponse.getData().isEmpty()) {
                Tasks task = taskResponse.getData().get(0);

                // Check if order is declined or cancelled
                boolean isOrderInactive = order.getStatus() == Orders.orderStatus.Completed ||
                        order.getStatus() == Orders.orderStatus.Declined ||
                        order.getStatus() == Orders.orderStatus.Cancelled;

                // Check if task is delivered or declined
                boolean isTaskInactive = task.getStatus() == Tasks.taskStatus.Delivered ||
                        task.getStatus() == Tasks.taskStatus.Declined;

                return !(isOrderInactive && isTaskInactive);
            }
            // If there's no task found or any error, consider it as an active order
            if (order.getStatus() == Orders.orderStatus.OrderPlaced) {
                return true; // OrderPlaced without task data is considered active
            }

            return false; // Other statuses without task data are considered inactive
        }

        // For non-delivery orders, active orders are those not marked as completed, declined, or cancelled
        return !(order.getStatus() == Orders.orderStatus.Completed ||
                order.getStatus() == Orders.orderStatus.Declined ||
                order.getStatus() == Orders.orderStatus.Cancelled);
    }


    private void setOrderStatusPane(UUID orderId) {
        // Set initial state
        initializeShapesAndLabels();

        Map<String, Object> orderQuery = Map.of("Id", orderId);
        Orders order = DaoFactory.getOrderDao().read(orderQuery).getData().get(0);
        Orders.orderStatus orderStatus = order.getStatus();

        if (order.getType() == Orders.orderType.Delivery) {
            ypane.toFront();

            // Handle order status
            switch (orderStatus) {
                case OrderPlaced:
                    updateShapesAndStatusLabels(yshape1, ystatus1);
                    return; // No need to check task status for OrderPlaced
                case Accepted:
                    updateShapesAndStatusLabels(yshape1, yshape2, yshape3, ystatus1, ystatus2);
                    break;
                case FoodsReady:
                    updateShapesAndStatusLabels(yshape1, yshape2, yshape3, yshape4, yshape5, ystatus1, ystatus2, ystatus3);
                    break;
                case Completed:
                    updateShapesAndStatusLabels(yshape1, yshape2, yshape3, yshape4, yshape5, yshape6, yshape7, ystatus1, ystatus2, ystatus3, ystatus4);
                    break;
            }

            // Read task data
            Map<String, Object> taskQuery = Map.of("orderId", orderId);
            Response<ArrayList<Tasks>> taskResponse = DaoFactory.getTaskDao().read(taskQuery);

            if (!taskResponse.isSuccess() || taskResponse.getData().isEmpty()) {
                // No task data, consider it is waiting store to accept or decline
                return;
            }

            // Handle task status
            Tasks task = taskResponse.getData().get(0);
            Tasks.taskStatus taskStatus = task.getStatus();

            switch (taskStatus) {
                case Pending:
                    updateShapesAndStatusLabels(zshape1, zstatus1);
                    break;
                case Accepted:
                    updateShapesAndStatusLabels(zshape1, zshape2, zshape3, zstatus1, zstatus2);
                    break;
                case PickedUp:
                    updateShapesAndStatusLabels(zshape1, zshape2, zshape3, zshape4, zshape5, zstatus1, zstatus2, zstatus3);
                    break;
            }
        } else {
            xpane.toFront();

            // Handle order status for non-delivery orders
            switch (orderStatus) {
                case OrderPlaced:
                    updateShapesAndStatusLabels(xshape1, xstatus1);
                    break;
                case Accepted:
                    updateShapesAndStatusLabels(xshape1, xshape2, xshape3, xstatus1, xstatus2);
                    break;
                case FoodsReady:
                    updateShapesAndStatusLabels(xshape1, xshape2, xshape3, xshape4, xshape5, xstatus1, xstatus2, xstatus3);
                    break;
            }
        }
    }


    private void initializeShapesAndLabels() {
        // Set initial colors and disable status labels
        xshape1.setFill(Paint.valueOf(defaultColor));
        xshape2.setFill(Paint.valueOf(defaultColor));
        xshape3.setFill(Paint.valueOf(defaultColor));
        xshape4.setFill(Paint.valueOf(defaultColor));
        xshape5.setFill(Paint.valueOf(defaultColor));
        xshape6.setFill(Paint.valueOf(defaultColor));
        xshape7.setFill(Paint.valueOf(defaultColor));
        yshape1.setFill(Paint.valueOf(defaultColor));
        yshape2.setFill(Paint.valueOf(defaultColor));
        yshape3.setFill(Paint.valueOf(defaultColor));
        yshape4.setFill(Paint.valueOf(defaultColor));
        yshape5.setFill(Paint.valueOf(defaultColor));
        yshape6.setFill(Paint.valueOf(defaultColor));
        yshape7.setFill(Paint.valueOf(defaultColor));
        zshape1.setFill(Paint.valueOf(defaultColor));
        zshape2.setFill(Paint.valueOf(defaultColor));
        zshape3.setFill(Paint.valueOf(defaultColor));
        zshape4.setFill(Paint.valueOf(defaultColor));
        zshape5.setFill(Paint.valueOf(defaultColor));
        zshape6.setFill(Paint.valueOf(defaultColor));
        zshape7.setFill(Paint.valueOf(defaultColor));

        xstatus1.setDisable(true);
        xstatus2.setDisable(true);
        xstatus3.setDisable(true);
        xstatus4.setDisable(true);
        ystatus1.setDisable(true);
        ystatus2.setDisable(true);
        ystatus3.setDisable(true);
        ystatus4.setDisable(true);
        zstatus1.setDisable(true);
        zstatus2.setDisable(true);
        zstatus3.setDisable(true);
        zstatus4.setDisable(true);
    }

    private void updateShapesAndStatusLabels(Node... nodes) {
        for (Node node : nodes) {
            if (node instanceof Circle) {
                ((Circle) node).setFill(Paint.valueOf(updatedColor));
            } else if (node instanceof Rectangle) {
                ((Rectangle) node).setFill(Paint.valueOf(updatedColor));
            } else if (node instanceof Label) {
                ((Label) node).setDisable(false);
            }
        }
    }

    public void cancelButtonClicked(ActionEvent event) {
        UUID orderId = orderIdComboBox.getValue();

        // Check if the order can be cancelled
        if (canCancelOrder(orderId)) {
            Map<String, Object> query = Map.of("Id", orderId);
            Map<String, Object> newStatus = Map.of("status", Orders.orderStatus.Cancelled, "updatedAt", LocalDateTime.now());
            DaoFactory.getOrderDao().update(query, newStatus);

            // Update transaction and wallet
            updateTransactionAndWallet(orderId);

            // Refresh UI
            setOrderIdComboBox();

            cusMainController.showAlert("Success", "Order cancelled successfully.");
            notificationController notificationController = new notificationController();
            notificationController.sendNotification(storeId, "Customer has cancelled this order: " + orderId, Notifications.notificationType.Information);
        } else {
            // Display an error message to the user
            cusMainController.showAlert("Error", "Order cannot be cancelled at this time.");
        }
    }

    private boolean canCancelOrder(UUID orderId) {
        // Check if the order status allows cancellation
        Map<String, Object> orderQuery = Map.of("Id", orderId);
        Orders order = DaoFactory.getOrderDao().read(orderQuery).getData().get(0);

        if (order.getStatus() == Orders.orderStatus.OrderPlaced) {
            // Check if it's a delivery order and there's no task data
            if (order.getType() == Orders.orderType.Delivery) {
                Map<String, Object> taskQuery = Map.of("orderId", orderId);
                Response<ArrayList<Tasks>> taskResponse = DaoFactory.getTaskDao().read(taskQuery);

                if (!taskResponse.isSuccess() || taskResponse.getData().isEmpty()) {
                    // No task data, consider it is waiting for the store to accept or decline
                    return true;
                }
            }
            return true;
        }
        return false;
    }

    private void updateTransactionAndWallet(UUID orderId) {
        // Fetch the transaction associated with the order
        Map<String, Object> orderQuery = Map.of("Id", orderId);
        Response<ArrayList<Orders>> orderResponse = DaoFactory.getOrderDao().read(orderQuery);

        if (orderResponse.isSuccess()) {
            UUID transactionId = orderResponse.getData().get(0).getTransactionId();
            Map<String, Object> transactionQuery = Map.of("Id", transactionId);
            Response<ArrayList<Transactions>> transactionResponse = DaoFactory.getTransactionDao().read(transactionQuery);

            if (transactionResponse.isSuccess()){
                // Update transaction status to Cancelled
                Map<String, Object> newStatus = Map.of("status", Transactions.transactionStatus.Cancelled, "updatedAt", LocalDateTime.now());
                DaoFactory.getTransactionDao().update(transactionQuery, newStatus);

                double amount = transactionResponse.getData().get(0).getAmount();
                refundAmountToWallet(cusMainController.userId, amount);
            }
        }
    }

    private void refundAmountToWallet(UUID userId, Double amount) {
        Map<String, Object> query = Map.of("userId", userId);
        Response<ArrayList<Wallets>> response = DaoFactory.getWalletDao().read(query);
        if (response.isSuccess()) {
            Wallets userWallet = response.getData().get(0);
            double newBalance = userWallet.getCredit() + amount;
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
}
