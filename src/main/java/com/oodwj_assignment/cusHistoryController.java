package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.OrderFoods;
import com.oodwj_assignment.models.Orders;
import com.oodwj_assignment.models.Tasks;
import com.oodwj_assignment.models.Users;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class cusHistoryController {

    @FXML private TableView<OrderFoods> orderInfoTableView;
    @FXML private GridPane historyGrid;
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
    @FXML private Pane noHistoryPane;
    private UUID orderId;
    private String restaurant;

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
        
        UUID userId = cusMainController.userId;

        // Create a list to hold the order panes
        List<VBox> historyPanes = new ArrayList<>();

        Map<String, Object> query = Map.of("userId", userId, "status", Orders.orderStatus.Completed);
        //Might need to add task status for delivery order
        Response<ArrayList<Orders>> response = DaoFactory.getOrderDao().read(query);

        if (response.isSuccess()) {
            ArrayList<Orders> completedOrders = response.getData();

            for (Orders order : completedOrders) {
                // Decide whether the order is completed based on its type and status
                if (isCompletedOrder(order)) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cusHistoryPane.fxml"));
                    VBox historyPane = fxmlLoader.load();
                    cusHistoryPaneController historyPaneController = fxmlLoader.getController();
                    historyPaneController.setCusHistoryController(this);
                    historyPaneController.populateOrderData(order);
                    historyPane.setUserData(fxmlLoader);
                    historyPanes.add(historyPane); // Add the history pane to the list
                }
            }
        }

        // Add the order panes to the grid
        int row = 0;
        for (VBox historyPane : historyPanes) {
            historyGrid.add(historyPane, 0, row);
            row++;
            GridPane.setMargin(historyPane, new Insets(2));
        }
    }

    public void setOrderInfo(UUID orderId, String restaurant) {
        this.orderId = orderId;
        this.restaurant = restaurant;
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
            double total = 0.0;

            for (OrderFoods orderFood : orderFoodsInfo) {
                double price = orderFood.getFoodPrice();
                int quantity = orderFood.getFoodQuantity();
                double amount = price * quantity;
                orderFood.setAmount(amount);
                total += amount;
            }

            LocalDateTime date = orderFoodsInfo.get(0).getCreatedAt();
            nameLabel.setText(restaurant);
            typeLabel.setText(type.toString());
            orderIdLabel.setText(orderId.toString());
            dateLabel.setText(date.withNano(0).toString());
            totalLabel.setText(currencyFormat.format(total));
            orderInfoTableView.getItems().setAll(orderFoodsInfo);
        } else {
            orderInfoPane.setVisible(false);
            cusMainController.showAlert("Error", "Failed to read order foods: " + foodResponse.getMessage());
        }
    }

    public void reorderButtonClicked(ActionEvent event) throws Exception {
        if (orderId == null) {
            return;
        }
        Map<String, Object> foodQuery = Map.of("orderId", orderId);
        Response<ArrayList<OrderFoods>> foodResponse = DaoFactory.getOrderFoodDao().read(foodQuery);

        if (foodResponse.isSuccess()) {
            ArrayList<OrderFoods> orderFoods = foodResponse.getData();
            // Pass the order details to the menuController
            cusMenuController menuController = new cusMenuController(restaurant, orderFoods);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cusMenu.fxml"));
            fxmlLoader.setController(menuController);
            menuController.setCusMenuController(menuController);
            Parent menuRoot = fxmlLoader.load();
            menuController.setOrderItems();

            Stage menu = new Stage();
            menu.setTitle("Menu Page");
            menu.initStyle(StageStyle.UNDECORATED);
            menu.setScene(new Scene(menuRoot));
            menu.initModality(Modality.APPLICATION_MODAL);
            menu.showAndWait();
        } else {
            cusMainController.showAlert("Error", "Failed to read order foods: " + foodResponse.getMessage());
        }
    }

    private boolean isCompletedOrder(Orders order) {
        if (order.getType() == Orders.orderType.Delivery) {
            Map<String, Object> taskQuery = Map.of("orderId", order.getId());
            Response<ArrayList<Tasks>> taskResponse = DaoFactory.getTaskDao().read(taskQuery);

            if (taskResponse.isSuccess() && !taskResponse.getData().isEmpty()) {
                Tasks task = taskResponse.getData().get(0);
                return order.getStatus() == Orders.orderStatus.Completed && task.getStatus() == Tasks.taskStatus.Delivered;
            }
            return false; // If there's no task found or any error, consider it as not completed
        }
        return order.getStatus() == Orders.orderStatus.Completed; // For non-delivery orders, completion is based on order status alone
    }
}
