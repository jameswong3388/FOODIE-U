package com.oodwj_assignment;

import com.oodwj_assignment.models.OrderFoods;
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
    private boolean isDelivery;

    public void initialize() throws IOException {
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

    public void setIsDelivery(boolean isDelivery) { this.isDelivery = isDelivery; }

    private void generateOrderSummary() {
        double subtotal = 0.0;
        for (OrderFoods orderFood : orderFoodsTableView.getItems()) {
            String amountString = amountColumn.getCellData(orderFood).replace("RM ", "");
            subtotal += Double.parseDouble(amountString);
        }
        double deliveryFee = 0.0;
        DecimalFormat currencyFormat = new DecimalFormat("RM #,##0.00");
        subtotalLabel.setText(currencyFormat.format(subtotal));
        if (isDelivery){
            // To round to the nearest whole number
            deliveryFee = Math.round(subtotal * 0.15);
            // Set the minimum delivery fee to 3 and maximum to 10
            deliveryFee = Math.max(Math.min(deliveryFee, 10), 3);
        }
        deliveryLabel.setText(currencyFormat.format(deliveryFee));
        totalLabel.setText(currencyFormat.format((subtotal + deliveryFee)));
    }

    public void closeButtonClicked(ActionEvent event) {
        Stage paymentStage = (Stage) closeButton.getScene().getWindow();
        paymentStage.close();
    }

    public void placeOrderButtonClicked(ActionEvent event) {

    }

    public void cancelButtonClicked(ActionEvent event) {
        Stage paymentStage = (Stage) closeButton.getScene().getWindow();
        paymentStage.close();
    }
}
