package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.OrderFoods;
import com.oodwj_assignment.models.Orders;
import com.oodwj_assignment.models.Users;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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
    @FXML private Label subtotalLabel;
    @FXML private Label discountLabel;
    @FXML private Label totalLabel;
    @FXML private TableColumn<OrderFoods, String> itemColumn;
    @FXML private TableColumn<OrderFoods, String> priceColumn;
    @FXML private TableColumn<OrderFoods, Integer> quantityColumn;
    @FXML private TableColumn<OrderFoods, String> amountColumn;
    @FXML private Pane orderInfoPane;
    @FXML private Pane noOrderPane;
    private UUID orderId;

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

        // Fetch order food information
        Map<String, Object> foodQuery = Map.of("orderId", orderId);
        Response<ArrayList<OrderFoods>> foodResponse = DaoFactory.getOrderFoodDao().read(foodQuery);

        if (foodResponse.isSuccess()) {
            ArrayList<OrderFoods> orderFoodsInfo = foodResponse.getData();

            // Fetch order information
            Map<String, Object> orderQuery = Map.of("Id", orderId);
            Response<ArrayList<Orders>> orderResponse = DaoFactory.getOrderDao().read(orderQuery);
            ArrayList<Orders> order = orderResponse.getData();
            UUID userId = order.get(0).getUserId();
            Orders.orderType type = order.get(0).getType();

            // Fetch user information
            Map<String, Object> userQuery = Map.of("Id", userId);
            Response<ArrayList<Users>> userResponse = DaoFactory.getUserDao().read(userQuery);
            ArrayList<Users> user = userResponse.getData();
            String name = user.get(0).getName();

            DecimalFormat currencyFormat = new DecimalFormat("RM #,##0.00");
            double subtotal = 0.0;
            double discount = 5.0;

            for (OrderFoods orderFood : orderFoodsInfo) {
                double price = orderFood.getFoodPrice();
                int quantity = orderFood.getFoodQuantity();
                double amount = price * quantity;
                orderFood.setAmount(amount);
                subtotal += amount;
            }

            double total = subtotal - discount;

            LocalDateTime date = orderFoodsInfo.get(0).getCreatedAt();
            nameLabel.setText(name);
            typeLabel.setText(type.toString());
            orderIdLabel.setText(orderId.toString());
            dateLabel.setText(date.withNano(0).toString());
            subtotalLabel.setText(currencyFormat.format(subtotal));
            discountLabel.setText(currencyFormat.format(discount));
            totalLabel.setText(currencyFormat.format(total));
            orderInfoTableView.getItems().setAll(orderFoodsInfo);
        } else {
            orderInfoPane.setVisible(false);
            venMainController.showAlert("Error", "Failed to read order foods: " + foodResponse.getMessage());
        }
    }

    public void acceptButtonClicked(ActionEvent event) throws Exception {
        updateOrderStatus(Orders.orderStatus.Accepted, "Order accepted.");
    }

    public void rejectButtonClicked(ActionEvent event) throws Exception {
        updateOrderStatus(Orders.orderStatus.Declined, "Order rejected.");
    }

    private void updateOrderStatus(Orders.orderStatus newStatus, String message) throws IOException {
        // Update the order status
        Map<String, Object> query = Map.of("Id", orderId);
        Map<String, Object> newValue = Map.of("status", newStatus);

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
