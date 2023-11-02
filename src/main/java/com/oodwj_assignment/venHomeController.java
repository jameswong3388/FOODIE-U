package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Orders;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class venHomeController {

    @FXML private TableView<Orders> ordersTableView;
    @FXML private TableColumn<Orders, UUID> orderIdColumn;
    @FXML private TableColumn<Orders, UUID> userIdColumn;
    @FXML private TableColumn<Orders, String> totalPriceColumn;
    @FXML private TableColumn<Orders, Integer> totalQuantityColumn;
    @FXML private TableColumn<Orders, Orders.orderStatus> statusColumn;
    @FXML private TableColumn<Orders, Orders.orderType> typeColumn;
    @FXML private TableColumn<Orders, LocalDateTime> updatedAtColumn;
    @FXML private TableColumn<Orders, LocalDateTime> createdAtColumn;
    @FXML private ComboBox<UUID> orderIdComboBox;
    @FXML private TextField orderStatusTextField;
    private ArrayList<Orders> allOrders;

    public void initialize() throws IOException {
        // Initialize column data factories
        orderIdColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        userIdColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getUserId()));
        totalPriceColumn.setCellValueFactory(cellData -> {
            double totalPrice = cellData.getValue().getTotalPrice();
            String formattedPrice = "RM " + String.format("%.2f", totalPrice);
            return new SimpleStringProperty(formattedPrice);
        });
        totalQuantityColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTotalQuantity()));
        statusColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStatus()));
        typeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getType()));
        updatedAtColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getUpdatedAt()));
        createdAtColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCreatedAt()));

        // Add listener to the ComboBox to show the next status
        orderIdComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Orders selectedOrder = allOrders.stream()
                        .filter(order -> order.getId().equals(newValue))
                        .findFirst()
                        .orElse(null);

                if (selectedOrder != null) {
                    Orders.orderStatus currentStatus = selectedOrder.getStatus();
                    Orders.orderStatus nextStatus = getNextStatus(currentStatus);
                    orderStatusTextField.setText(String.valueOf(nextStatus));
                }
            }
        });

        loadOrders();
    }

    private void loadOrders() {
        List<UUID> orderIds = venMainController.getOrderIds();

        if (!orderIds.isEmpty()) {
            allOrders = new ArrayList<>();

            for (UUID orderId : orderIds) {
                Map<String, Object> query = Map.of("Id", orderId);
                Response<ArrayList<Orders>> response = DaoFactory.getOrderDao().read(query);

                if (response.isSuccess()) {
                    allOrders.addAll(response.getData());
                } else {
                    venMainController.showAlert("Read Error", "Failed to read orders for order ID: " + response.getMessage());
                }
            }

            // Filter order IDs with active statuses and set them in the ComboBox
            List<UUID> activeOrderIds = allOrders.stream()
                    .filter(order -> order.getStatus() == Orders.orderStatus.Accepted ||
                            order.getStatus() == Orders.orderStatus.FoodsReady)
                    .map(Orders::getId)
                    .toList();

            // Filter the table to display orders
            List<Orders> filtered = filterOrders(Orders.orderStatus.Accepted, Orders.orderStatus.Declined,
                    Orders.orderStatus.FoodsReady, Orders.orderStatus.Completed, Orders.orderStatus.Cancelled);

            ordersTableView.getItems().setAll(filtered);
            orderIdComboBox.getItems().clear();
            orderIdComboBox.getItems().addAll(activeOrderIds);
        } else {
            venMainController.showAlert("Error", "No order IDs to retrieve orders for.");
        }
    }

    private List<Orders> filterOrders(Orders.orderStatus... allowedStatuses) {
        return allOrders.stream()
                .filter(order -> Arrays.asList(allowedStatuses).contains(order.getStatus()))
                .collect(Collectors.toList());
    }

    private Orders.orderStatus getNextStatus(Orders.orderStatus currentStatus) {
        Orders.orderStatus nextStatus = null;

        switch (currentStatus) {
            case Accepted:
                nextStatus = Orders.orderStatus.FoodsReady;
                break;
            case FoodsReady:
                nextStatus = Orders.orderStatus.Completed;
                break;
            default:
                break;
        }

        return nextStatus;
    }

    public void updateButtonClicked(ActionEvent event) throws IOException {
        UUID orderId = orderIdComboBox.getValue();
        String statusText = orderStatusTextField.getText();
        // Parse the status text to get the selected status
        Orders.orderStatus status = parseStatusText(statusText);

        if (orderId == null || status == null) {
            venMainController.showAlert("Validation Error", "Please select an order ID and order status.");
            clearFields();
        } else {
            Map<String, Object> query = Map.of("Id", orderId);
            Map<String, Object> newValue = Map.of("status", status);
            Response<Void> response = DaoFactory.getOrderDao().update(query, newValue);
            if (response.isSuccess()) {
                loadOrders();
                clearFields();
                venMainController.showAlert("Success", "Order status updated successfully.");
            } else {
                venMainController.showAlert("Update Error", "Failed to update order status: " + response.getMessage());
            }
        }
    }

    private Orders.orderStatus parseStatusText(String statusText) {
        try {
            return Orders.orderStatus.valueOf(statusText);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public void cancelButtonClicked(ActionEvent event) { clearFields(); }

    public void allToggleClicked(ActionEvent event) throws IOException {
        loadOrders();
    }

    public void activeToggleClicked(ActionEvent event) throws IOException {
        List<Orders> filtered = filterOrders(Orders.orderStatus.Accepted, Orders.orderStatus.FoodsReady);
        ordersTableView.getItems().setAll(filtered);
    }

    public void completedToggleClicked(ActionEvent event) throws IOException {
        List<Orders> filtered = filterOrders(Orders.orderStatus.Completed);
        ordersTableView.getItems().setAll(filtered);
    }

    public void rejectedToggleClicked(ActionEvent event) throws IOException {
        List<Orders> filtered = filterOrders(Orders.orderStatus.Declined, Orders.orderStatus.Cancelled);
        ordersTableView.getItems().setAll(filtered);
    }

    private void clearFields() {
        orderIdComboBox.setValue(null);
        orderStatusTextField.clear();
    }
}
