package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Attachments;
import com.oodwj_assignment.models.Notifications;
import com.oodwj_assignment.models.Orders;
import com.oodwj_assignment.models.Transactions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class notificationPaneController {

    @FXML private ImageView typeImage;
    @FXML private Circle statusCircle;
    @FXML private Label typeLabel;
    @FXML private Label dateLabel;
    @FXML private Button actionButton;
    @FXML private TextArea messageTextArea;
    private notificationController notificationController;
    private String readColor = "#AAAAAA";
    private String unreadColor = "#23B6AC";
    private UUID notificationId;
    private Notifications.notificationType type;
    private String message;
    private UUID userId;

    public void populateNotification(Notifications notification){
        actionButton.setVisible(false);
        notificationId = notification.getId();

        type = notification.getType();
        Notifications.notificationStatus status = notification.getStatus();
        message = notification.getMessage();
        LocalDateTime date = notification.getCreatedAt();

        setImageAndLabel(type);
        setStatusCircleColor(status);

        messageTextArea.setText(message);
        dateLabel.setText(String.valueOf(date.withNano(0)));

        if (type == Notifications.notificationType.Receipt) {
            checkForAttachments();
        } else if (type == Notifications.notificationType.Warning) {
            actionButton.setVisible(true);
        }
    }

    private void setImageAndLabel(Notifications.notificationType type) {
        String imagePath = "";
        switch (type){
            case Information:
                imagePath = "/images/information.png";
                break;
            case Receipt:
                imagePath = "/images/receipt.png";
                break;
            case Warning:
                imagePath = "/images/warning.png";
                break;
        }
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        typeImage.setImage(image);
        typeLabel.setText(type + " Notification");
    }

    private void setStatusCircleColor(Notifications.notificationStatus status) {
        String color = (status == Notifications.notificationStatus.Read) ? readColor : unreadColor;
        statusCircle.setFill(Paint.valueOf(color));
    }

    private void checkForAttachments() {
        Map<String, Object> query = Map.of("notificationId", notificationId);
        Response<ArrayList<Attachments>> response = DaoFactory.getAttachmentDao().read(query);
        if (response.isSuccess()) {
            actionButton.setVisible(true);
        }
    }

    public void actionButtonClicked(ActionEvent event) throws IOException {
        if (type == Notifications.notificationType.Receipt) {
            showReceipt();
        } else if (type == Notifications.notificationType.Warning) {
            showWarningDialog();
        }
    }

    private void showReceipt() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("receipt.fxml"));
        Parent receiptRoot = loader.load();
        receiptController receiptController = loader.getController();
        receiptController.fetchTransactionData(notificationId);
        Stage receipt = new Stage();
        receipt.setTitle("Receipt");
        receipt.initStyle(StageStyle.UNDECORATED);
        receipt.setScene(new Scene(receiptRoot));
        receipt.initModality(Modality.APPLICATION_MODAL);
        receipt.showAndWait();
    }

    private UUID getOrderIdFromMessage(){
        // Split the message by ":"
        String[] parts = message.split(":");

        // Check if there are enough parts
        if (parts.length >= 2) {
            String orderIdString = parts[parts.length - 1].trim();
            return UUID.fromString(orderIdString);
        }
        return null;
    }

    private void showWarningDialog() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Order Issue");
        alert.setHeaderText("Unable to Find a Runner");
        alert.setContentText("Would you like to cancel the order and get a refund, or change the order type?");

        ButtonType buttonTypeRefund = new ButtonType("Cancel and Refund");
        ButtonType buttonTypeChangeOrder = new ButtonType("Change Order Type");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeRefund, buttonTypeChangeOrder, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            // Check if the order issue has been resolved before
            if (!isResolved()){
                if (result.get() == buttonTypeRefund) {
                    // Cancelling the order and refunding
                    cancelAndRefund();
                } else if (result.get() == buttonTypeChangeOrder) {
                    // Changing the order type
                    showOrderTypeSelectionDialog();
                }
            } else {
                cusMainController.showAlert("Order Resolved", "The order issues has been resolved before.");
            }
        }
    }

    private void showOrderTypeSelectionDialog() {
        List<String> choices = new ArrayList<>();
        choices.add("Takeaway");
        choices.add("Dine-In");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("Takeaway", choices);
        dialog.setTitle("Change Order Type");
        dialog.setHeaderText("Select New Order Type");
        dialog.setContentText("Choose your order type:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(orderType -> {
            if ("Takeaway".equals(orderType)) {
                try {
                    updateOrderType(Orders.orderType.TakeAway, "Takeaway");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    updateOrderType(Orders.orderType.DineIn, "Dine-In");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void updateOrderType(Orders.orderType newType, String message) throws IOException {
        // Update the order type
        UUID orderId = getOrderIdFromMessage();
        if (orderId == null){
            return;
        }
        Map<String, Object> query = Map.of("Id", orderId);
        Map<String, Object> newValue = Map.of("type", newType, "updatedAt", LocalDateTime.now());

        Response<Void> response = DaoFactory.getOrderDao().update(query, newValue);
        if (response.isSuccess()) {
            cusMainController.showAlert("Success"," The order type has been updated to " + message);
        } else {
            cusMainController.showAlert("Update Error", "Failed to update order type: " + response.getMessage());
        }
    }

    public void updateOrderStatus(UUID orderId, Orders.orderStatus newStatus, String message) throws IOException {
        // Update the order status
        Map<String, Object> query = Map.of("Id", orderId);
        Map<String, Object> newValue = Map.of("status", newStatus, "updatedAt", LocalDateTime.now());

        Response<Void> response = DaoFactory.getOrderDao().update(query, newValue);
        if (response.isSuccess()) {
            cusMainController.showAlert("Success", message + " The order status has been updated.");
        } else {
            cusMainController.showAlert("Update Error", "Failed to update order status: " + response.getMessage());
        }
    }

    private void cancelAndRefund() throws IOException {
        UUID orderId = getOrderIdFromMessage();
        if (orderId == null){
            return;
        }
        updateOrderStatus(orderId, Orders.orderStatus.Declined, "Order rejected.");
        venOrderController orderController = new venOrderController();
        UUID transactionId = DaoFactory.getOrderDao().read(Map.of("Id", orderId)).getData().get(0).getTransactionId();
        orderController.updateTransactionStatus(transactionId, Transactions.transactionStatus.Cancelled);

        double amount = DaoFactory.getTransactionDao().read(Map.of("Id", transactionId)).getData().get(0).getAmount();
        // Refund back to user
        orderController.refundAmountToWallet(userId, amount);
    }

    private boolean isResolved() {
        UUID orderId = getOrderIdFromMessage();
        if (orderId == null) {
            return false; // If orderId is not available, it's not resolved
        }

        // Check if there is an order with the specified conditions
        Map<String, Object> query = Map.of("Id", orderId);
        Response<ArrayList<Orders>> response = DaoFactory.getOrderDao().read(query);

        if (response.isSuccess() && !response.getData().isEmpty()) {
            Orders order = response.getData().get(0); // Get the first (and only) matching order
            if (order.getStatus() == Orders.orderStatus.Cancelled ||
                    (order.getType() == Orders.orderType.TakeAway || order.getType() == Orders.orderType.DineIn)) {
                return true; // Order is resolved
            }
        }

        return false; // No matching order found or order doesn't meet conditions, not resolved
    }


    public void statusButtonClicked() {
        Map<String, Object> query = Map.of("Id", notificationId);
        Map<String, Object> newValue = Map.of("status", Notifications.notificationStatus.Read, "updatedAt", LocalDateTime.now());
        Response<Void> response = DaoFactory.getNotificationDao().update(query, newValue);
        if (response.isSuccess()){
            statusCircle.setFill(Paint.valueOf(readColor));
        }
    }

    public void setNotificationController(notificationController notificationController) {
        this.notificationController = notificationController;
    }

    public void setUserId(UUID userId) { this.userId = userId; }
}
