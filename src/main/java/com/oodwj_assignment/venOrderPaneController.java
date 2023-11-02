package com.oodwj_assignment;

import com.oodwj_assignment.models.Orders;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.UUID;

public class venOrderPaneController {
    @FXML private Label orderId;
    @FXML private Label total;
    @FXML private Label date;
    private venOrderController venOrderController;

    public void populateOrderData(Orders orders){
        orderId.setText(String.valueOf(orders.getId()));
        total.setText(String.format("RM %.2f", orders.getTotalPrice()));
        date.setText(String.valueOf(orders.getCreatedAt().withNano(0)));
    }

    public void viewButtonClicked(ActionEvent event) throws IOException {
        UUID orderId = UUID.fromString(this.orderId.getText());
        if (venOrderController != null) {
            venOrderController.setOrderInfo(orderId);
        }
    }

    // Set the parent venOrderController for communication
    public void setVenOrderController(venOrderController controller) {
        this.venOrderController = controller;
    }

    public UUID getOrderId() {
        return UUID.fromString(orderId.getText());
    }
}
