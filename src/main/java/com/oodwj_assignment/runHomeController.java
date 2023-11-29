package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Notifications;
import com.oodwj_assignment.models.Orders;
import com.oodwj_assignment.models.Tasks;
import com.oodwj_assignment.models.Transactions;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


public class runHomeController {

    @FXML private TableView<Tasks> tasksTableView;
    @FXML private TableColumn<Tasks, UUID> taskIdColumn;
    @FXML private TableColumn<Tasks, UUID> orderIdColumn;
    @FXML private TableColumn<Tasks, String> deliveryFeesColumn;
    @FXML private TableColumn<Tasks, Tasks.taskStatus> statusColumn;
    @FXML private TableColumn<Tasks, LocalDateTime> updatedAtColumn;
    @FXML private TableColumn<Tasks, LocalDateTime> createdAtColumn;
    @FXML private ComboBox<UUID> taskIdComboBox;
    @FXML private TextField tasksStatusTextField;
    private ArrayList<Tasks> allTasks;

    public void initialize() throws IOException {
        // Initialize column data factories
        taskIdColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        orderIdColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getOrderId()));
        deliveryFeesColumn.setCellValueFactory(cellData -> {
            double deliveryFees = cellData.getValue().getDeliveryFee();
            String formattedPrice = "RM " + String.format("%.2f", deliveryFees);
            return new SimpleStringProperty(formattedPrice);
        });
        statusColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStatus()));
        updatedAtColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getUpdatedAt()));
        createdAtColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCreatedAt()));

        // Add listener to the ComboBox to show the next status
        taskIdComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Tasks selectedTask = allTasks.stream()
                        .filter(task -> task.getId().equals(newValue))
                        .findFirst()
                        .orElse(null);

                if (selectedTask != null) {
                    Tasks.taskStatus currentStatus = selectedTask.getStatus();
                    Tasks.taskStatus nextStatus = getNextStatus(currentStatus);
                    tasksStatusTextField.setText(String.valueOf(nextStatus));
                }
            }
        });

        loadTasks();
    }

    private void loadTasks() {
        UUID runnerId = runMainController.runnerId;

        Map<String, Object> query = Map.of("runnerId", runnerId);
        Response<ArrayList<Tasks>> response = DaoFactory.getTaskDao().read(query);

        if (response.isSuccess()) {
            allTasks = response.getData();

            // Filter tasks with active statuses and set them in the ComboBox
            List<UUID> activeTaskIds = allTasks.stream()
                    .filter(task -> task.getStatus() == Tasks.taskStatus.Accepted ||
                            task.getStatus() == Tasks.taskStatus.PickedUp)
                    .map(Tasks::getId)
                    .toList();

            // Filter the table to display tasks with specific statuses
            List<Tasks> filtered = allTasks.stream() // Use 'allTasks' for filtering
                    .filter(task -> Arrays.asList(
                            Tasks.taskStatus.Accepted,
                            Tasks.taskStatus.Declined,
                            Tasks.taskStatus.PickedUp,
                            Tasks.taskStatus.Delivered
                    ).contains(task.getStatus()))
                    .toList();

            tasksTableView.getItems().setAll(filtered);
            taskIdComboBox.getItems().clear();
            taskIdComboBox.getItems().addAll(activeTaskIds);
        }
    }


    private List<Tasks> filterTasks(Tasks.taskStatus... allowedStatuses) {
        return allTasks.stream()
                .filter(task -> Arrays.asList(allowedStatuses).contains(task.getStatus()))
                .collect(Collectors.toList());
    }

    private Tasks.taskStatus getNextStatus(Tasks.taskStatus currentStatus) {
        Tasks.taskStatus nextStatus = null;

        switch (currentStatus) {
            case Accepted:
                nextStatus = Tasks.taskStatus. PickedUp;
                break;
            case  PickedUp:
                nextStatus = Tasks.taskStatus.Delivered;
                break;
            default:
                break;
        }

        return nextStatus;
    }

    public void updateButtonClicked(ActionEvent event) throws IOException {
        UUID taskId = taskIdComboBox.getValue();
        String statusText = tasksStatusTextField.getText();
        // Parse the status text to get the selected status
        Tasks.taskStatus status = parseStatusText(statusText);

        if (taskId == null || status == null) {
            runMainController.showAlert("Validation Error", "Please select an task ID and task status.");
            clearFields();
            return;
        }

        // Update Task Status
        Map<String, Object> taskQuery = Map.of("Id", taskId);
        Map<String, Object> newValue = Map.of("status", status, "updatedAt", LocalDateTime.now());
        Response<Void> taskResponse = DaoFactory.getTaskDao().update(taskQuery, newValue);

        if (!taskResponse.isSuccess()) {
            runMainController.showAlert("Update Error", "Failed to update tasks status: " + taskResponse.getMessage());
            return;
        }

        // Update Transaction Status if Task is Delivered
        if (status == Tasks.taskStatus.Delivered) {
            UUID transactionId = DaoFactory.getTaskDao().read(taskQuery).getData().get(0).getTransactionId();
            if (transactionId != null) {
                Map<String, Object> transactionQuery = Map.of("Id", transactionId);
                Map<String, Object> newTransactionValue = Map.of("status", Transactions.transactionStatus.Completed, "updatedAt", LocalDateTime.now());
                Response<Void> transactionResponse = DaoFactory.getTransactionDao().update(transactionQuery, newTransactionValue);

                if (!transactionResponse.isSuccess()) {
                    runMainController.showAlert("Transaction Update Error", "Failed to update transaction status: " + transactionResponse.getMessage());
                }
            }
        }

        // Reload tasks and clear fields
        loadTasks();
        clearFields();
        runMainController.showAlert("Success", "task status updated successfully.");

        // Read User ID
        UUID orderId = DaoFactory.getTaskDao().read(taskQuery).getData().get(0).getOrderId();
        Map<String, Object> orderQuery = Map.of("Id", orderId);
        UUID userId = DaoFactory.getOrderDao().read(orderQuery).getData().get(0).getUserId();

        notificationController notificationController = new notificationController();
        notificationController.sendNotification(userId, "Task status has been updated. Current task status: " + status, Notifications.notificationType.Information);
    }

    private Tasks.taskStatus parseStatusText(String statusText) {
        try {
            return Tasks.taskStatus.valueOf(statusText);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public void cancelButtonClicked(ActionEvent event) { clearFields(); }

    public void allToggleClicked(ActionEvent event) throws IOException {
        loadTasks();
    }

    public void activeToggleClicked(ActionEvent event) throws IOException {
        if (allTasks == null){
            return;
        }
        List<Tasks> filtered = filterTasks(Tasks.taskStatus.Accepted, Tasks.taskStatus.PickedUp);
        tasksTableView.getItems().setAll(filtered);
    }

    public void completedToggleClicked(ActionEvent event) throws IOException {
        if (allTasks == null){
            return;
        }
        List<Tasks> filtered = filterTasks(Tasks.taskStatus.Delivered);
        tasksTableView.getItems().setAll(filtered);
    }

    public void rejectedToggleClicked(ActionEvent event) throws IOException {
        if (allTasks == null){
            return;
        }
        List<Tasks> filtered = filterTasks(Tasks.taskStatus.Declined);
        tasksTableView.getItems().setAll(filtered);
    }

    private void clearFields() {
        taskIdComboBox.setValue(null);
        tasksStatusTextField.clear();
    }
}
