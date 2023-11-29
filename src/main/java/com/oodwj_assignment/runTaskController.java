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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;

public class runTaskController {

    @FXML private TableView<OrderFoods> orderTableView;
    @FXML private GridPane taskGrid;
    @FXML private Label taskIdLabel;
    @FXML private Label orderIdLabel;
    @FXML private Label resNameLabel;
    @FXML private Label cusNameLabel;
    @FXML private Label phoneNumberLabel;
    @FXML private Label deliveryFeeLabel;
    @FXML private TableColumn<OrderFoods, String> itemColumn;
    @FXML private TableColumn<OrderFoods, String> priceColumn;
    @FXML private TableColumn<OrderFoods, Integer> quantityColumn;
    @FXML private TableColumn<OrderFoods, String> amountColumn;
    @FXML private Pane taskInfoPane;
    @FXML private Pane noTaskPane;
    private UUID orderId;
    private UUID taskId;
    private UUID storeId;
    private UUID runnerId;
    private  Double deliveryFee;
    private Map<UUID, Set<UUID>> taskRejections = new HashMap<>();

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
        taskInfoPane.setVisible(false);

        runnerId = runMainController.runnerId;
        // Create a list to hold the order panes
        List<VBox> taskPanes = new ArrayList<>();

        Map<String, Object> query = Map.of("status", Tasks.taskStatus.Pending);
        Response<ArrayList<Tasks>> response = DaoFactory.getTaskDao().read(query);

        if (response.isSuccess()) {
            ArrayList<Tasks> pendingTasks = response.getData();

            for (Tasks task : pendingTasks) {
                // Load the order pane and set up its controller
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("runTaskPane.fxml"));
                VBox taskPane = fxmlLoader.load();
                runTaskPaneController taskPaneController = fxmlLoader.getController();
                taskPaneController.setRunTaskController(this);
                taskPaneController.populateTaskData(task);
                taskPane.setUserData(fxmlLoader);
                taskPanes.add(taskPane); // Add the task pane to the list
            }
        }

        // Add the order panes to the grid
        int row = 0;
        for (VBox taskPane : taskPanes) {
            taskGrid.add(taskPane, 0, row);
            row++;
            GridPane.setMargin(taskPane, new Insets(2));
        }
    }

    public void setTaskInfo(UUID taskId) {
        this.taskId = taskId;
        taskInfoPane.setVisible(true);

        // Fetch task information
        Map<String, Object> taskQuery = Map.of("Id", taskId);
        Response<ArrayList<Tasks>> taskResponse = DaoFactory.getTaskDao().read(taskQuery);

        if (taskResponse.isSuccess()) {
            ArrayList<Tasks> task = taskResponse.getData();
            orderId = task.get(0).getOrderId();

            // Fetch order food information
            Map<String, Object> orderFoodQuery = Map.of("orderId", orderId);
            Response<ArrayList<OrderFoods>> orderFoodResponse = DaoFactory.getOrderFoodDao().read(orderFoodQuery);

            if (orderFoodResponse.isSuccess()) {
                ArrayList<OrderFoods> orderFoodsInfo = orderFoodResponse.getData();

                // Fetch order information
                Map<String, Object> orderQuery = Map.of("Id", orderId);
                Response<ArrayList<Orders>> orderResponse = DaoFactory.getOrderDao().read(orderQuery);
                UUID userId = orderResponse.getData().get(0).getUserId();

                // Fetch store information
                Map<String, Object> foodQuery = Map.of("Id", orderFoodsInfo.get(0).getFoodId());
                Response<ArrayList<Foods>> foodResponse = DaoFactory.getFoodDao().read(foodQuery);
                ArrayList<Foods> food = foodResponse.getData();
                Map<String, Object> storeQuery = Map.of("Id", food.get(0).getStoreId());
                Response<ArrayList<Stores>> storeResponse = DaoFactory.getStoreDao().read(storeQuery);
                String resName = storeResponse.getData().get(0).getName();
                storeId = storeResponse.getData().get(0).getId();

                // Fetch user information
                Map<String, Object> userQuery = Map.of("Id", userId);
                Response<ArrayList<Users>> userResponse = DaoFactory.getUserDao().read(userQuery);
                String cusName = userResponse.getData().get(0).getName();
                String phoneNumber = userResponse.getData().get(0).getPhoneNumber();

                DecimalFormat currencyFormat = new DecimalFormat("RM #,##0.00");

                for (OrderFoods orderFood : orderFoodsInfo) {
                    double price = orderFood.getFoodPrice();
                    int quantity = orderFood.getFoodQuantity();
                    double amount = price * quantity;
                    orderFood.setAmount(amount);
                }

                deliveryFee = task.get(0).getDeliveryFee();
                taskIdLabel.setText(taskId.toString());
                orderIdLabel.setText(orderId.toString());
                resNameLabel.setText(resName);
                cusNameLabel.setText(cusName);
                phoneNumberLabel.setText(phoneNumber);
                deliveryFeeLabel.setText(currencyFormat.format(deliveryFee));
                orderTableView.getItems().setAll(orderFoodsInfo);
            }
        } else {
            taskInfoPane.setVisible(false);
        }
    }

    public void acceptButtonClicked(ActionEvent event) throws Exception {
        updateTaskStatus(Tasks.taskStatus.Accepted, "Task accepted.");

        // Create a new transaction for delivery fee
        Transactions newTransaction = new Transactions(UUID.randomUUID(), deliveryFee, Transactions.transactionType.Payment, Transactions.transactionStatus.Pending, storeId, runnerId, LocalDateTime.now(), LocalDateTime.now());
        Response<UUID> transactionResponse = DaoFactory.getTransactionDao().create(newTransaction);
        if (!transactionResponse.isSuccess()) {
            runMainController.showAlert("Error", "Failed to create transaction: " + transactionResponse.getMessage());
            return;
        }
        UUID transactionId = transactionResponse.getData();

        // Update transactionId into task
        Map<String, Object> query = Map.of("Id", taskId);
        Map<String, Object> newValue = Map.of("transactionId", transactionId, "updatedAt", LocalDateTime.now());
        Response<Void> taskResponse = DaoFactory.getTaskDao().update(query, newValue);
        if (!taskResponse.isSuccess()) {
            runMainController.showAlert("Error", "Failed to update transaction to task: " + taskResponse.getMessage());
        }

        Map<String, Object> orderQuery = Map.of("Id", orderId);
        UUID userId = DaoFactory.getOrderDao().read(orderQuery).getData().get(0).getUserId();
        notificationController notificationController = new notificationController();
        notificationController.sendNotification(userId, "A runner accepted the task: " + taskId, Notifications.notificationType.Information);
        notificationController.sendNotification(storeId, "A runner accepted the task: " + taskId, Notifications.notificationType.Information);
    }

    public void rejectButtonClicked(ActionEvent event) throws Exception {
        recordRejectionByRunner();

        if (!haveAllRunnersRejected(taskId)) {
            updateTaskStatus(Tasks.taskStatus.Pending, "You have rejected the task. Waiting for other runners.");
            return;
        }

        updateTaskStatus(Tasks.taskStatus.Declined, "Task declined by all runners.");

        Map<String, Object> orderQuery = Map.of("Id", orderId);
        UUID userId = DaoFactory.getOrderDao().read(orderQuery).getData().get(0).getUserId();
        notificationController notificationController = new notificationController();
        notificationController.sendNotification(userId, "Unable to find a runner for the order ID:" + orderId, Notifications.notificationType.Warning);
        notificationController.sendNotification(storeId, "Unable to find a runner for the order ID:" + orderId + ". Waiting customer to make decision.", Notifications.notificationType.Information);
    }

    private void recordRejectionByRunner() {
        taskRejections.computeIfAbsent(taskId, k -> new HashSet<>()).add(runnerId);
    }

    private boolean haveAllRunnersRejected(UUID taskId) {
        Set<UUID> rejections = taskRejections.get(taskId);
        int totalRunners = getTotalRunners();
        return rejections != null && rejections.size() == totalRunners;
    }

    private int getTotalRunners() {
        Map<String, Object> query = Map.of("role", Users.Role.Delivery_Runner);
        Response<ArrayList<Users>> response = DaoFactory.getUserDao().read(query);

        if (response.isSuccess()) {
            return response.getData().size();
        } else {
            // Return a default value
            return 1;
        }
    }

    private void updateTaskStatus(Tasks.taskStatus newStatus, String message) throws IOException {
        // Update the order status
        Map<String, Object> query = Map.of("Id", taskId);
        Map<String, Object> newValue = null;
        if (newStatus.equals(Tasks.taskStatus.Accepted)){
            newValue = Map.of("status", newStatus, "runnerId", runnerId, "updatedAt", LocalDateTime.now());
        } else {
            newValue = Map.of("status", newStatus, "updatedAt", LocalDateTime.now());
        }

        Response<Void> response = DaoFactory.getTaskDao().update(query, newValue);
        if (response.isSuccess()) {
            // Remove the order pane from the grid
            removeTaskPane(taskId);
            taskInfoPane.setVisible(false);
            runMainController.showAlert("Success", message);
        } else {
            runMainController.showAlert("Update Error", "Failed to update task status: " + response.getMessage());
        }
    }

    private void removeTaskPane(UUID taskId) {
        List<Node> nodesToRemove = new ArrayList<>();
        for (Node node : taskGrid.getChildren()) {
            if (node instanceof VBox) {
                FXMLLoader loader = (FXMLLoader) node.getUserData();
                runTaskPaneController taskPaneController = loader.getController();
                UUID taskPaneTaskId = taskPaneController.getTaskId();

                if (taskPaneTaskId.equals(taskId)) {
                    nodesToRemove.add(node);
                }
            }
        }

        taskGrid.getChildren().removeAll(nodesToRemove);
    }
}
